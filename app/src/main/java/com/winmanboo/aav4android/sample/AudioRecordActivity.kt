package com.winmanboo.aav4android.sample

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.winmanboo.aav4android.BaseActivity
import com.winmanboo.aav4android.R
import com.winmanboo.aav4android.databinding.ActivityAudioRecordBinding
import java.io.File

/**
 * @Author wzm
 * @Date 2022/5/19 11:24
 */
class AudioRecordActivity : BaseActivity() {
  private val permissions =
    arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
  private val binding by lazy { ActivityAudioRecordBinding.inflate(layoutInflater) }

  private var controller: RecorderController? = null

  private var allGranted: Boolean = false

  private val launcher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) {
    if (it.values.contains(false)) {
      Toast.makeText(this, "Not all permissions granted", Toast.LENGTH_SHORT).show()
    } else {
      allGranted = true
      controller = RecorderController(this)
      adapter.submitList(loadFiles())
    }
  }

  private val adapter = SimpleAudioRecordAdapter()

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
        RecorderController.State.PREPARED, RecorderController.State.IDLE -> it.startRecord()
        RecorderController.State.STARTED -> it.stopRecord()
        RecorderController.State.STOPPED -> it.startRecord()
        RecorderController.State.DESTROYED -> {
          it.reset()
          it.startRecord()
        }
      }
    }

    binding.rvRecord.adapter = adapter
    binding.rvRecord.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
  }

  private fun loadFiles(): List<File> {
    val file = controller?.savedPath?.let { File(it) } ?: return emptyList()

    if (!file.exists()) {
      log.info("files is empty.")
      return emptyList()
    }

    if (!file.isDirectory) {
      log.warn("saved path is not directory.")
      return emptyList()
    }

    return file.listFiles()?.toList() ?: emptyList()
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
    adapter.submitList(loadFiles())
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

    controller?.stop()
  }

  override fun onDestroy() {
    super.onDestroy()

    controller?.release()
  }
}