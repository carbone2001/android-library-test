package com.carbonesoftware.test

import android.app.Activity
import android.app.ActivityManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable


@Composable
fun TestScreen(activity: Activity){

    Box(){
      Button(onClick = {
          //Check if another is running
          try{

              val actvityManager = activity.getSystemService(ComponentActivity.ACTIVITY_SERVICE) as ActivityManager
              val procInfos = actvityManager.runningAppProcesses
              for (runningProInfo in procInfos) {
                  Log.d("Running Processes", "()()" + runningProInfo.processName)
              }


              /*val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
              activity.startActivity(intent)*/

              /*val am = getSystemService<Any>(activity,Context.ACTIVITY_SERVICE.javaClass) as ActivityManager?
              am?.let {
                  val runningAppProcessInfo = it.runningAppProcesses

              }*/

              /*for (i in runningAppProcessInfo.indices) {
                  //*if (runningAppProcessInfo[i].processName == "com.the.app.you.are.looking.for") {
                      // Do your stuff here.
                  }*/
              }*/

              //val activityManager = ContextCompat.getSystemService(this, Context.ACTIVITY_SERVICE::class.java)
              /*activityManager?.let {
                  Log.d("",it);
                  val recentTasks = it.getRunningTasks(Int.MAX_VALUE)
                  for (i in recentTasks.indices) {
                      Log.d(
                          "Executed app",
                          "Application executed : " + recentTasks[i].baseActivity!!.toShortString() + "\t\t ID: " + recentTasks[i].id + ""
                      )
                  }
              }*/

          }
          catch (error: Exception){
              Log.d("ERROR CHECKING ACTIVITY MANAGER", error.message ?: "")
          }
      }) {
          Text(text = "Check running apps")
      }
    }

}