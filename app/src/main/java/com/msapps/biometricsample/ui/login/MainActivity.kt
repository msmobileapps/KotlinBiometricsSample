package com.msapps.biometricsample.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.msapps.bio.BiometricsManager
import com.msapps.biometricsample.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.logout).setOnClickListener {
            if (BiometricsManager.isDeviceBiometricsEnable(this@MainActivity)) {
                BiometricsManager.logout(this@MainActivity)
            }
            finish()
//            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }

}