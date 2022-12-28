package com.carbonesoftware.test.downloadfile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DownloadFileScreen() {
    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val launcherForActivityResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
    ) {
        it?.let { uri ->
            //Se obteniene al archivo como uri.
            //Se deberá obtener el outputstream del URI para escribir
            val outputStream = ctx.contentResolver.openOutputStream(uri)
            outputStream?.let {
                coroutineScope.launch {
                    val fileData = "Contenido hardcodeado del documento"
                    withContext(Dispatchers.IO) {
                        it.write(fileData.toByteArray())
                        it.close()
                    }
                }
            }
        }
    }

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            //Contenido que se muestra cuando aún no se han pedido los permisos
            Column {
                Button(onClick = {
                    //Pedir permisos
                    permissionState.launchPermissionRequest()
                }) {
                    Text(text = "Pedir permisos")
                }
            }

        },
        permissionNotAvailableContent = {
            //Contenido que se muestra cuando se DENEGARON los permisos
            Column() {
                Text(text = "Se denegaron los permisos")

                Button(onClick = {
                    //Abrir configuracion de la aplicacion
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", ctx.packageName, null)
                    intent.data = uri
                    ctx.startActivity(intent)
                }) {
                    Text(text = "Ir a configuracion de la aplicacion")
                }
            }
        }
    ) {
        //Contenido que se muestra cuando se APROBARON los permisos
        Column {
            Button(onClick = {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm")
                val fileName =
                    "TEST-ARCHIVO-${formatter.format(LocalDateTime.now())}.txt"
                //Se envia la peticion para crear un archivo
                launcherForActivityResult.launch(fileName)
            }) {
                Text(text = "Descargar archivo")
            }
        }
    }
}