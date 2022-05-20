package com.winmanboo.aav4android.media_sample

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.winmanboo.aav4android.utils.LogUtil
import java.io.File

/**
 * @Author wzm
 * @Date 2022/5/20 10:53
 */
class PlayerController(private val context: Context) : MediaController {
  private var player: MediaPlayer? = null
  private var state = State.Idle
  private val log = LogUtil().apply { initialize(this@PlayerController::class.java.simpleName) }

  private var lastSavedPath: String? = null

  private fun init(filePath: String) {
    player = MediaPlayer().apply {
      val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        .build()
      setAudioAttributes(attributes)
      setDataSource(filePath)
      state = State.Initialized
      setOnPreparedListener {
        state = State.Prepared
        player?.start()
        state = State.Started
      }
      setOnCompletionListener {
        log.info("end of playback, $lastSavedPath")
        state = State.End
      }
      setOnErrorListener { mp, what, extra ->
        when (what) {
          MediaPlayer.MEDIA_ERROR_UNKNOWN -> log.info("unknown media error.")
          MediaPlayer.MEDIA_ERROR_SERVER_DIED -> {
            log.info("media server died.")
          }
          MediaPlayer.MEDIA_ERROR_IO -> log.info("io error.")
          MediaPlayer.MEDIA_ERROR_MALFORMED ->
            log.info("non compliance with document standards or specifications")
          MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> log.info("unsupported.")
          MediaPlayer.MEDIA_ERROR_TIMED_OUT -> log.info("timeout error.")
        }
        release()
        val fileName = generateFileName()
        init(context.parentSavedPath + fileName)
        prepare()
        true
      }
      setOnInfoListener { mp, what, extra ->
        when (what) {
          MediaPlayer.MEDIA_INFO_UNKNOWN -> log.info("unknown media info")
          MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING -> log.info("only audio can be played.")
        }
        true
      }

      // more listener
    }
  }

  private fun prepare() {
    player?.let {
      state = State.Preparing
      it.prepareAsync()
    }
  }

  override fun start(filePath: String) {
    if (player?.isPlaying == true) { // currently is playing, release now
      release()
    }

    val file = File(filePath)
    if (!file.exists()) {
      log.info("file not exists, can't play this media file.")
      return
    }

    if (state == State.Paused) {
      if (lastSavedPath == filePath) {
        log.info("resumed.")
        player?.start()
        return
      } else { // release and replay the new
        release()
      }
    }

    if (state > State.Preparing && state < State.Paused) {
      log.warn("player is ready.")
      return
    }

    lastSavedPath = filePath

    init(filePath)
    prepare()
  }

  fun pause() {
    if (player?.isPlaying == true) {
      player?.pause()
      state = State.Paused
    } else {
      log.warn("can't pause, state: $state, played: ${player?.isPlaying}")
    }
  }

  override fun stop() {
    if (state > State.Prepared && state < State.End) {
      player?.stop()
      state = State.Stopped
    } else {
      log.warn("can't stop, state: $state")
    }
  }

  override fun reset() {
    player?.reset()
    state = State.Idle
  }

  override fun release() {
    player?.release()
    player = null

    state = State.End
  }

  enum class State {
    Idle,
    Initialized,
    Preparing,
    Prepared,
    Started,
    Paused,
    Stopped,
    End
  }
}