package com.winmanboo.aav4android.sample

import android.content.Context
import android.media.MediaRecorder
import com.winmanboo.aav4android.utils.LogUtil
import java.io.File

/**
 * @Author wzm
 * @Date 2022/5/19 13:54
 */
class MediaRecorderController(private val context: Context) {
  private lateinit var recorder: MediaRecorder
  var state: State = State.IDLE
    private set

  private val log: LogUtil = LogUtil()

  companion object {
    private const val TAG = "MediaRecordController"
  }

  init {
    log.initialize(TAG)
    init()

    recorder.setOnErrorListener { mr, what, extra ->
      if (what == MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN) {
        log.error("unknown media record error.")
      } else if (what == MediaRecorder.MEDIA_ERROR_SERVER_DIED) {
        log.error("media server died error.")
        release()
        init()
        prepare()
      }
    }
    recorder.setOnInfoListener { mr, what, extra ->
      when (what) {
        MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN -> {
          log.info("unknown media recorder info.")
        }
        MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED -> {
          log.info("max duration already set.")
        }
        MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED -> {
          log.info("max file size already set.")
        }
      }
    }
  }

  private fun init() {
    recorder = MediaRecorder()

    recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
    recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS) // .aac
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD)

    log.info("init now.")
  }

  private fun prepare() {
    val fileName = "${System.currentTimeMillis()}_ar.aac"
    val path = context.filesDir?.absolutePath + File.separator + fileName
    log.info("file path: $path")
    recorder.setOutputFile(path)
    recorder.prepare()
    state = State.PREPARED

    log.info("prepared now.")
  }

  fun start() {
    if (state == State.STARTED) {
      log.info("recorder has started!")
      return
    }

    if (state < State.PREPARED) {
      prepare()
    }

    recorder.start()
    state = State.STARTED
    log.info("start now.")
  }

  fun stop() {
    when (state) {
      State.IDLE -> log.info("not yet prepare.")
      State.PREPARED -> log.info("not yet start.")
      State.STARTED -> {
        recorder.stop()
        state = State.STOPPED
        log.info("stop now.")
      }
      State.STOPPED -> log.info("recorder has stopped!")
      State.DESTROYED -> log.error("recorder has destroyed! do not reuse the recorder!")
    }
  }

  fun reset() {
    recorder.reset()
    log.info("reset now.")
    init()
  }

  fun release() {
    if (state == State.DESTROYED) {
      log.info("recorder has destroyed!")
      return
    }

    recorder.release()
    log.info("release now.")
  }

  enum class State {
    IDLE,
    PREPARED,
    STARTED,
    STOPPED,
    DESTROYED
  }
}