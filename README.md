# Kotlin Biometrics Sample


check if Device Biometrics Enable (Device Got Biometrics Sensor And User Enable this feature)

    if (BiometricsManager.isDeviceBiometricsEnable(this@LoginActivity)) {
            
            
check ability to login (Already Sign IN)
         
    if (BiometricsManager.canLoginWithBiometrics(this@LoginActivity)) {
               
Sign In or Login With Biometrics

    if (BiometricsManager.canLoginWithBiometrics(this@LoginActivity)) {
        BiometricsManager.showBiometricPromptForDecryption(this@LoginActivity, biometricsCallback)       
     } else {
        BiometricsManager.showBiometricPromptForEncryption(
             this@LoginActivity,
             password.text.toString(),
             biometricsCallback
         )
     }
  
you need to provide success callback

    private val biometricsCallback = object : BiometricsCallback {
        override fun onSuccess(encryptedText: String) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }       