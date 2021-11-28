package com.syed.budgettracker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.syed.budgettracker.R

class SplashScreenActivity : AppCompatActivity() {
        private lateinit var kerismaker :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        kerismaker=findViewById(R.id.tv2)

        kerismaker.setMovementMethod(LinkMovementMethod.getInstance());

        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        },2500)
    }
}