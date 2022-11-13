package com.example.investigation

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.investigation.crypting.CryptingScreen
import com.example.investigation.crypting.CryptoManager
import com.example.investigation.datastore.DataStoreScreen
import com.example.investigation.ui.theme.InvestigationTheme
import com.example.investigation.navigation.NavigationScafold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val Context.dataStore by preferencesDataStore("settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Data store
        val readValue = MutableStateFlow<String>("")

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
                    DataStoreScreen({ key, value ->
                        lifecycleScope.launch{
                            save(key, value)
                        }
                    }, { key ->
                        lifecycleScope.launch{
                            val result = read(key)
                            result?.let {
                                readValue.value = it
                            }
                        }
                    }, readValue)
                    //EncriptDataStoreScreen()
                }
            }
        }
    }

    private suspend fun save(key: String, value: String){
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