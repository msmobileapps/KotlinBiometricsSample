# Kotlin Biometrics Sample


to check if Device Biometrics Enable

    if (BiometricsManager.isDeviceBiometricsEnable(this@LoginActivity)) {
            
            
to check if can login (Already Sign IN)
         
    if (BiometricsManager.canLoginWithBiometrics(this@LoginActivity)) {
               
to use Sig In or Login With Biometrics

                if (BiometricsManager.canLoginWithBiometrics(this@LoginActivity)) {
                    BiometricsManager.showBiometricPromptForDecryption(this@LoginActivity, bioCallback)
                } else {
                    BiometricsManager.showBiometricPromptForEncryption(
                        this@LoginActivity,
                        password.text.toString(),
                        bioCallback
                    )
                }
  
you need to provide success callback

        private val bioCallback = object : BiometricsCallback {
               override fun onSuccess(encryptedText: String) {
                   startActivity(Intent(this@LoginActivity, MainActivity::class.java))
               }
         }          