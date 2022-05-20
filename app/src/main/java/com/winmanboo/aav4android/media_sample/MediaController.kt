package com.winmanboo.aav4android.media_sample

/**
 * @Author wzm
 * @Date 2022/5/20 11:04
 */
interface MediaController {
  fun start(filePath: String)

  fun stop()

  fun reset()

  fun release()
}