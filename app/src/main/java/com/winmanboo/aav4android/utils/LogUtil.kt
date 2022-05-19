package com.winmanboo.aav4android.utils

import android.util.Log

/**
 * @Author wzm
 * @Date 2022/5/19 15:21
 */

class LogUtil {
  private var tag: String? = null

  fun initialize(tag: String) {
    this.tag = tag
  }

  fun info(message: String) {
    Log.d(tag, "info: $message")
  }

  fun debug(message: String) {
    Log.d(tag, "debug: $message")
  }

  fun error(message: String) {
    Log.d(tag, "error: $message")
  }

  fun warn(message: String) {
    Log.d(tag, "warn: $message")
  }
}