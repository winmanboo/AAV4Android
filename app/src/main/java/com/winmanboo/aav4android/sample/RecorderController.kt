package com.winmanboo.aav4android.sample

import android.content.Context
import android.media.MediaRecorder
import com.winmanboo.aav4android.utils.LogUtil
import java.io.File

/**
 * @Author wzm
 * @Date 2022/5/19 13:54
 */
class RecorderController(context: Context) : MediaController {
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
        val fileName = generateFileName()
        prepare(context.parentSavedPath + fileName)
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
    recorder.setAudioChannels(2) // stereo
    // recorder.setAudioEncodingBitRate(48) // 码流
    recorder.setAudioSamplingRate(48000) // 48KHz

    log.info("init now.")
  }

  private fun prepare(filePath: String) {
    val file = File(filePath)
    if (file.parentFile == null) {
      file.parent?.let { File(it) }?.mkdir()
    }
    log.info("file path: $filePath")
    recorder.setOutputFile(filePath)
    recorder.prepare()
    state = State.PREPARED

    log.info("prepared now.")
  }

  override fun start(filePath: String) {
    if (state == State.STARTED) {
      log.info("recorder has started!")
      return
    }

    if (state < State.PREPARED) {
      prepare(filePath)
    }

    recorder.start()
    state = State.STARTED
    log.info("start now.")
  }

  override fun stop() {
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

  override fun reset() {
    recorder.reset()
    log.info("reset now.")
    init()
  }

  override fun release() {
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