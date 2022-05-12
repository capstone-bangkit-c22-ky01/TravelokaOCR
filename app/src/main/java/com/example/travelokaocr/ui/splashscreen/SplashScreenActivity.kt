package com.example.travelokaocr.ui.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.travelokaocr.databinding.ActivitySplashScreenBinding
import com.example.travelokaocr.ui.homescreen.HomeActivity

class SplashScreenActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ANIMATION
        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator
            .ofFloat(
                binding.tvWelcomeTo,
                View.ALPHA, 1f)
            .apply {
                duration = 2000
            }.start()

        val ivMoon = ObjectAnimator.ofFloat(binding.ivMoon, View.ALPHA, 1f).setDuration(2000)
        val ivPlane = ObjectAnimator.ofFloat(binding.ivPlane, View.ALPHA, 1f).setDuration(2000)
        val ivCloud = ObjectAnimator.ofFloat(binding.ivCloud, View.ALPHA, 1f).setDuration(2000)
        val ivPerson = ObjectAnimator.ofFloat(binding.ivPersonSplashScreen, View.ALPHA, 1f).setDuration(2000)
        val tvLogoDesc = ObjectAnimator.ofFloat(binding.tvLogoDesc, View.ALPHA, 1f).setDuration(2000)
        val tvLogo = ObjectAnimator
            .ofFloat(
                binding.tvLogo,
                View.TRANSLATION_X, -20f, 20f)
            .apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }

        AnimatorSet().apply {
            playTogether(ivMoon, ivPlane, ivCloud, ivPerson, tvLogoDesc, tvLogo)
            start()
        }
    }

    @Suppress("DEPRECATION")
    private fun setupAction() {
        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}