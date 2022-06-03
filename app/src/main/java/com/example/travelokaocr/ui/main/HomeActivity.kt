package com.example.travelokaocr.ui.main

import android.content.Context
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityHomeBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SETUP
        setUpView()
        //CONNECT BOTTOM NAVIGATION TO THIS ACTIVITY
        bottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.mainNavHostFragment)
        bottomNavigationView.setupWithNavController(navController)
    }

    @Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //the focus on edit text will be cleared when user touch anything outside the edittext
        if (ev?.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    @Suppress("DEPRECATION")
    private fun setUpView(){
        //hide the action bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}