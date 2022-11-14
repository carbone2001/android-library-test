package com.example.investigation

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import com.example.investigation.biometricAuth.BiometricAuthScreen
import com.example.investigation.crypting.CryptoManager
import com.example.investigation.downloadfile.DownloadFileScreen
import com.example.investigation.encryptDataStore.DataToEncryptClass
import com.example.investigation.encryptDataStore.DataToEncryptSerializer
import com.example.investigation.ui.theme.InvestigationTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class MainActivity : FragmentActivity() {
    //DataStore normal
    private val Context.dataStore by preferencesDataStore("settings")

    //DataStore con encryptacion
    private val Context.dataStoreEncrypt: DataStore<DataToEncryptClass> by dataStore(
        fileName = "settings-encrypt",
        serializer = DataToEncryptSerializer(CryptoManager())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Data store
        val readValue = MutableStateFlow("")

        //Biometric auth
        val isAuth = MutableStateFlow(false)
        setContent {
            InvestigationTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //CryptingScreen(this.getExternalFilesDir(null)!!, CryptoManager())
                    //NavigationScafold()
                    //TestScreen(this)
//                    DataStoreScreen({ key, value ->
//                        lifecycleScope.launch{
//                            save(key, value)
//                        }
//                    }, { key ->
//                        lifecycleScope.launch{
//                            val result = read(key)
//                            result?.let {
//                                readValue.value = it
//                            }
//                        }
//                    }, readValue)
                    //EncriptDataStoreScreen(dataStore = this.dataStoreEncrypt)
                    //DownloadFileScreen()
                    BiometricAuthScreen(isAuth){
                        authenticate {
                            isAuth.value = it
                        }
                    }

                }
            }
        }

        setupAuth()
    }

    //Si el usuario puede o no autenticarse
    private var canAuthenticate = false

    ///Informacion del propmt de autenticacion
    private lateinit var promptInfo: PromptInfo

    ///Configura la autenticacion
    fun setupAuth() {
        //Preguntar si el usuario puede autenticarse con algun metodo
        //BIOMETRIC_STRONG => Huella o cara
        //DEVICE CREDENTIAL => Patrón
        if (BiometricManager.from(this).canAuthenticate(
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
            == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            canAuthenticate = true

            promptInfo = PromptInfo.Builder()
                .setTitle("Atenticación biométrica")
                .setSubtitle("AUtentícate utilizando el sensor biométrico")
                .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                .build()
        }
    }

    //Realiza la autenticacion
    private fun authenticate(auth: (auth: Boolean) -> Unit) {
        if (canAuthenticate) {
            BiometricPrompt(
                this,//Utilizar activity actual
                ContextCompat.getMainExecutor(this),//Utilizar activity actual
                object : BiometricPrompt.AuthenticationCallback() {
                    //Si salio bien, se ejecutara lo siguiente
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        auth(true)
                    }
                }
            ).authenticate(promptInfo)
        } else {
            //Si no puede autenticarse, decir que la autenticacion fue exitosa
            auth(true)
        }
    }

    private suspend fun save(key: String, value: String) {
        var dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        var dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}