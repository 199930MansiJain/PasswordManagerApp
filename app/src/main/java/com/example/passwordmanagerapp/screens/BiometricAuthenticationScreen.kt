package com.example.passwordmanagerapp.screens

import android.os.Build
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity

@Composable
fun BiometricAuthenticationScreen(onAuthenticated: (Boolean) -> Unit) {
    val context = LocalContext.current as FragmentActivity
    val biometricManager = androidx.biometric.BiometricManager.from(context)
    val canAuthenticateWithBiometrics =
        when (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> {
                Log.e("TAG", "Device does not support strong biometric authentication")
                false
            }
        }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (canAuthenticateWithBiometrics) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (canAuthenticateWithBiometrics) {
                            BiometricButton(
                                onClick = {
                                    authenticateWithBiometric(context){
                                        onAuthenticated(it)
                                    }
                                },
                                text = "Tap here to proceed by Authenticating with Biometric"
                            )
                        } else {
                            Text(text = "Biometric authentication is not available on this device.")
                        }
                    }
                }
            } else {
                Text(text = "Biometric authentication is not available on this device.")
            }
        }
    }
}

@Composable
fun BiometricButton(
    onClick: () -> Unit,
    text: String
) {
    Button(    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        onClick = onClick,
        modifier = androidx.compose.ui.Modifier.padding(8.dp)
    ) {
        Text(

            text = text,
            textAlign = TextAlign.Center
        )
    }
}

fun authenticateWithBiometric(context: FragmentActivity ,onAuthenticated: (Boolean) -> Unit) {
    val executor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        context.mainExecutor
    } else {
        TODO("VERSION.SDK_INT < P")
    }
    val biometricPrompt = BiometricPrompt(
        context,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                Log.d("TAG", "Authentication successful!!!")
                onAuthenticated(true)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Log.e("TAG", "onAuthenticationError")
                onAuthenticated(false)
            }

            override fun onAuthenticationFailed() {
                Log.e("TAG", "onAuthenticationFailed")
                onAuthenticated(false)
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setDescription("Place your finger the sensor to authenticate.")
        .setNegativeButtonText("Cancel")
        .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()

    biometricPrompt.authenticate(promptInfo)
}