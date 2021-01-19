package com.msapps.bio

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.edit

/**
 * Created by Barak Halevi on 18/01/2021.
 */
class BiometricsManager {
    companion object{
        private lateinit var onSuccess: BiometricsCallback
        private lateinit var toEncript: String
        private lateinit var activity: AppCompatActivity
        private val cryptographyManager: CryptographyManager by lazy { CryptographyManager() }

        fun canLoginWithBiometrics(context:AppCompatActivity): Boolean {
            return getCiphertextWrapper(context) != null
        }

        fun isDeviceBiometricsEnable(context:AppCompatActivity): Boolean {
            return BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        }

        fun logout(context:AppCompatActivity) {
            context?.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)
                ?.edit {
                    remove(CIPHERTEXT_WRAPPER )
                    apply()
                }
        }

        private fun getCiphertextWrapper(context:AppCompatActivity): CiphertextWrapper? {
            return cryptographyManager.getCiphertextWrapperFromSharedPrefs(
                context,
                SHARED_PREFS_FILENAME,
                Context.MODE_PRIVATE,
                CIPHERTEXT_WRAPPER
            )
        }

        fun showBiometricPromptForEncryption(context:AppCompatActivity,passwordToEncrypt:String,onSuccess_:BiometricsCallback) {
            activity = context;
            toEncript = passwordToEncrypt;
            onSuccess = onSuccess_
            val canAuthenticate =
                BiometricManager.from(context).canAuthenticate()
            if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
                val secretKeyName = CIPHERTEXT_WRAPPER
                val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
                val biometricPrompt =
                    BiometricPromptUtils.createBiometricPrompt(
                        context,
                        ::encryptAndStorePassword,
                        ::processToMany
                    )
                val promptInfo = BiometricPromptUtils.createPromptInfo(context, "Sign Up")
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
            }
        }

         private fun encryptAndStorePassword(authResult: BiometricPrompt.AuthenticationResult) {
            authResult.cryptoObject?.cipher?.apply {
                toEncript.let { toEncript ->
                    val encryptedServerTokenWrapper = cryptographyManager.encryptData(Companion.toEncript, this)
                    cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                        encryptedServerTokenWrapper,
                        activity,
                        "biometric_prefs",
                        Context.MODE_PRIVATE,
                        CIPHERTEXT_WRAPPER
                    )
                }
                onSuccess.onSuccess("")
            }
        }
        fun showBiometricPromptForDecryption(context:AppCompatActivity,onSuccess_:BiometricsCallback) {
            activity = context
            onSuccess = onSuccess_
            getCiphertextWrapper(context)?.let { textWrapper ->
                val canAuthenticate = BiometricManager.from(activity).canAuthenticate()
                if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
                    val secretKeyName = CIPHERTEXT_WRAPPER
                    val cipher = cryptographyManager.getInitializedCipherForDecryption(
                        secretKeyName, textWrapper.initializationVector
                    )
                    val biometricPrompt =
                        BiometricPromptUtils.createBiometricPrompt(
                            context,
                            ::decryptServerTokenFromStorage,
                            ::processToMany
                        )
                    val promptInfo = BiometricPromptUtils.createPromptInfo(context,"Sign In")
                    biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
                }
            }
        }
        private fun processToMany(str: String) {
            Toast.makeText(activity, str , Toast.LENGTH_LONG).show()
            onSuccess.onSuccess("")
        }

        private fun decryptServerTokenFromStorage(authResult: androidx.biometric.BiometricPrompt.AuthenticationResult) {
            getCiphertextWrapper(activity)?.let { textWrapper ->
                authResult.cryptoObject?.cipher?.let {
                    val plaintext = cryptographyManager.decryptData(textWrapper.ciphertext, it)
                    onSuccess.onSuccess(plaintext)
                }
            }
        }
    }
}

interface BiometricsCallback {
 fun onSuccess(str:String)
}
