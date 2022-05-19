package com.winmanboo.aav4android

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * @Author wzm
 * @Date 2022/5/19 12:44
 */
abstract class BaseActivity : AppCompatActivity() {
  /*companion object {
    const val PERMISSION_REQUEST_CODE = 1001
  }

  protected fun requestPermissions(vararg permissions: String) {
    val deniedPermissions = mutableListOf<String>()
    for (permission in permissions) {
      if (ActivityCompat.checkSelfPermission(
          this,
          permission
        ) == PackageManager.PERMISSION_DENIED
      ) {
        deniedPermissions += permission
      }
    }
    val allGranted = deniedPermissions.isEmpty()
    if (!allGranted) {
      ActivityCompat.requestPermissions(
        this,
        deniedPermissions.toTypedArray(),
        PERMISSION_REQUEST_CODE
      )
    }
  }*/
}