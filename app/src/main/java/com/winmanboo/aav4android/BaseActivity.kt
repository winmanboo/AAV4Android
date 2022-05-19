package com.winmanboo.aav4android

import androidx.appcompat.app.AppCompatActivity
import com.winmanboo.aav4android.utils.LogUtil

/**
 * @Author wzm
 * @Date 2022/5/19 12:44
 */
abstract class BaseActivity : AppCompatActivity() {
  protected val log: LogUtil =
    LogUtil().apply { initialize(this@BaseActivity::class.java.simpleName) }

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