package com.example.travelokaocr.ui.ocr

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivityOcrscreenBinding
import com.example.travelokaocr.utils.imageanalysis.ImageAnalyzer
import com.example.travelokaocr.utils.imageanalysis.createTempFile
import com.example.travelokaocr.viewmodel.OCRScreenViewModel
import com.google.gson.Gson
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min

class OCRScreenActivity : AppCompatActivity() {

    //BINDING
    private lateinit var binding: ActivityOcrscreenBinding

    // image controller stuff
    private lateinit var cameraController: CameraControl
    private var toggleTorch: Boolean = false
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    // photo file stuff
    private lateinit var photoFile: File
    private lateinit var currentPhotoFilePath: String

    // image analyzer stuff
    private var imageAnalyzer: ImageAnalysis? = null
    private lateinit var cameraAnalysisExecutor: ExecutorService

    private val viewModel: OCRScreenViewModel by viewModels()

    companion object{

        // We only need to analyze and capture a section, so we set crop percentages
        // to avoid analyze the entire image from the live camera feed.
        const val DESIRED_WIDTH_CROP_PERCENT = 8
        const val DESIRED_HEIGHT_CROP_PERCENT = 60

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val TAG = "OCRScreenActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcrscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (!allPermissionGranted()){
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }else{

            cameraAnalysisExecutor = Executors.newSingleThreadExecutor()

            photoFile = createTempFile(this).also {
                currentPhotoFilePath = it.absolutePath
            }

            binding.viewFinder.post {

                bindCameraUseCases()

                binding.overlay.apply {

                    setZOrderOnTop(true)
                    holder.setFormat(PixelFormat.TRANSPARENT)
                    holder.addCallback(object : SurfaceHolder.Callback {

                        override fun surfaceCreated(p0: SurfaceHolder) {
                            holder?.let {
                                drawOverlay(it, DESIRED_HEIGHT_CROP_PERCENT, DESIRED_WIDTH_CROP_PERCENT)
                            }
                        }

                        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

                        }

                        override fun surfaceDestroyed(p0: SurfaceHolder) {

                        }

                    })
                }

                binding.switchTorch.setOnClickListener{

                    if (toggleTorch){

                        toggleTorch = false
                        binding.switchTorch.setImageResource(R.drawable.ic_flash_off)

                    }else{

                        toggleTorch = true
                        binding.switchTorch.setImageResource(R.drawable.ic_flash_on)
                    }

                    cameraController.enableTorch(toggleTorch)

                }

                binding.switchCamera.setOnClickListener {

                    lensFacing = if (lensFacing.equals(CameraSelector.LENS_FACING_BACK)) CameraSelector.LENS_FACING_FRONT
                    else CameraSelector.LENS_FACING_BACK

                    bindCameraUseCases()

                }

            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraAnalysisExecutor.shutdown()
    }

    private fun bindCameraUseCases(){

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({

            // Camera provider is now guaranteed to be available
            val cameraProvider = cameraProviderFuture.get()

            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { binding.viewFinder.display.getRealMetrics(it)}
            Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

            val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
            Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

            // Set up the view finder use case to display camera preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .build()
                .also {
                    // Use the camera object to link our preview use case with the view
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            viewModel.imageCropPercentages.observe(this) {
                drawOverlay(
                    binding.overlay.holder,
                    it.first,
                    it.second
                )
            }

            // Create a new camera selector each time, enforcing lens facing
            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            // Build the image analysis use case and instantiate our analyzer
            imageAnalyzer = ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(binding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        cameraAnalysisExecutor
                        , ImageAnalyzer(
                            this,
                            photoFile,
                            viewModel.imageCropPercentages
                        ){ detectedObjectList ->
                            Log.d("asiapRahasia", Gson().toJson(detectedObjectList))
                        }
                    )
                }

            try {
                // Apply declared configs to CameraX using the same lifecycle owner
                cameraProvider.unbindAll()

                val camera = cameraProvider.bindToLifecycle(
                    this as LifecycleOwner, cameraSelector, preview, imageAnalyzer
                )

                cameraController = camera.cameraControl
                cameraController.enableTorch(false)

            } catch (exc: Exception){
                Toast.makeText(
                    this,
                    "Unable to show camera",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun drawOverlay(
        holder: SurfaceHolder,
        heightCropPercent: Int,
        widthCropPercent: Int,
    ) {
        val canvas = holder.lockCanvas()

        val rectPaint = Paint()
        rectPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        rectPaint.style = Paint.Style.FILL
        rectPaint.color = Color.WHITE
        val outlinePaint = Paint()
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.color = Color.WHITE
        outlinePaint.strokeWidth = 4f
        val surfaceWidth = holder.surfaceFrame.width()
        val surfaceHeight = holder.surfaceFrame.height()

        val cornerRadius = 25f
        // Set rect centered in frame
        val rectTop = surfaceHeight * heightCropPercent / 2 / 100f
        val rectLeft = surfaceWidth * widthCropPercent / 2 / 100f
        val rectRight = surfaceWidth * (1 - widthCropPercent / 2 / 100f)
        val rectBottom = surfaceHeight * (1 - heightCropPercent / 2 / 100f)
        val rect = RectF(rectLeft, rectTop, rectRight, rectBottom)

        binding.viewFinderBackground.setViewFinderRect(rect)

        canvas.drawRoundRect(
            rect, cornerRadius, cornerRadius, rectPaint
        )
        canvas.drawRoundRect(
            rect, cornerRadius, cornerRadius, outlinePaint
        )

        holder.unlockCanvasAndPost(canvas)
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = ln(max(width, height).toDouble() / min(width, height))
        if (abs(previewRatio - ln(RATIO_4_3_VALUE))
            <= abs(previewRatio - ln(RATIO_16_9_VALUE))
        ) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionGranted()){
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

}