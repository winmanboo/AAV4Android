package com.winmanboo.aav4android.sample

import android.content.Context
import java.io.File

/**
 * @Author wzm
 * @Date 2022/5/20 11:19
 */

const val AUDIO_SUB_DIR = "audio"

val Context.parentSavedPath: String
  get() = this.filesDir.absolutePath + File.separator + AUDIO_SUB_DIR + File.separator

fun generateFileName(): String {
  return "${System.currentTimeMillis()}_ar.aac"
}

