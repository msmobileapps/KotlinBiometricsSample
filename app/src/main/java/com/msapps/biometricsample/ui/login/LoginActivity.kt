package com.msapps.biometricsample.ui.login

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.msapps.bio.BiometricsCallback
import com.msapps.bio.BiometricsManager
import com.msapps.biometricsample.R


class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private val bioCallback = object : BiometricsCallback {
        override fun onSuccess(encryptedText: String) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        loginButton = findViewById<Button>(R.id.login)



        loginButton.setOnClickListener {
            if (BiometricsManager.canLoginWithBiometrics(this@LoginActivity)) {
                BiometricsManager.showBiometricPromptForDecryption(this@LoginActivity, bioCallback)
            } else {
                BiometricsManager.showBiometricPromptForEncryption(
                    this@LoginActivity,
                    password.text.toString(),
                    bioCallback
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (BiometricsManager.isDeviceBiometricsEnable(this@LoginActivity)) {
            if (BiometricsManager.canLoginWithBiometrics(this@LoginActivity)) {
                loginButton.text = "Biometrics Sign In"
            } else {
                loginButton.text = "Biometrics Sign Up"
            }
        }else{
            loginButton.text = "Sign With Password"
        }
    }

}
