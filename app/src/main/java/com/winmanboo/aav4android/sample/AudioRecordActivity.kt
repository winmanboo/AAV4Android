package com.winmanboo.aav4android.sample

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.winmanboo.aav4android.BaseActivity
import com.winmanboo.aav4android.R
import com.winmanboo.aav4android.databinding.ActivityAudioRecordBinding

/**
 * @Author wzm
 * @Date 2022/5/19 11:24
 */
class AudioRecordActivity : BaseActivity() {
  private val permissions =
    arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
  private val binding by lazy { ActivityAudioRecordBinding.inflate(layoutInflater) }

  private var controller: MediaRecorderController? = null

  private var allGranted: Boolean = false

  private val launcher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) {
    if (it.values.contains(false)) {
      Toast.makeText(this, "Not all permissions granted", Toast.LENGTH_SHORT).show()
    } else {
      allGranted = true
      controller = MediaRecorderController(this)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    initView()
    launcher.launch(permissions)
  }

  private fun initView() {
    setSupportActionBar(binding.toolbar)

    binding.btnRecord.setOnClickListener {
      if (!allGranted) {
        showPermissionDialog()
        return@setOnClickListener
      }
      when (controller?.state) {
        MediaRecorderController.State.PREPARED, MediaRecorderController.State.IDLE -> it.startRecord()
        MediaRecorderController.State.STARTED -> it.stopRecord()
        MediaRecorderController.State.STOPPED -> it.startRecord()
        MediaRecorderController.State.DESTROYED -> {
          it.reset()
          it.startRecord()
        }
      }
    }

    // binding.rvRecord.adapter
  }

  private fun showPermissionDialog() {
    MaterialAlertDialogBuilder(this)
      .setMessage("permission is not fully granted. Do you want to re apply？")
      .setPositiveButton("yes") { _, _ ->
        launcher.launch(permissions)
      }
      .show()
  }

  private fun View.startRecord() {
    if (this.id != R.id.btn_record) return

    val record = (this as ImageView)
    record.setImageResource(R.drawable.ic_stop_circle_black)
    controller?.start()
  }

  private fun View.stopRecord() {
    if (this.id != R.id.btn_record) return

    val record = (this as ImageView)
    record.setImageResource(R.drawable.ic_album_black)
    controller?.stop()
  }

  private fun View.reset() {
    if (this.id != R.id.btn_record) return

    controller?.reset()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_audio_record, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      R.id.use_audio_record -> {
        TODO("use audio record")
        true
      }
      R.id.use_media_record -> {
        TODO("use media record")
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onPause() {
    super.onPause()

    binding.btnRecord.stopRecord()
  }

  override fun onDestroy() {
    super.onDestroy()

    controller?.release()
  }
}