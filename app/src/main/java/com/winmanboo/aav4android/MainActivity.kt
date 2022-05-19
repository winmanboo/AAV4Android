package com.winmanboo.aav4android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.winmanboo.aav4android.databinding.ActivityMainBinding
import com.winmanboo.aav4android.sample.AudioRecordActivity

class MainActivity : AppCompatActivity() {
  private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

  private val mOnClickListener = View.OnClickListener {
    when (it.id) {
      R.id.btn_ar -> start<AudioRecordActivity>()
      R.id.btn_vr -> TODO("to VideoRecord Activity")
      R.id.btn_aav -> TODO("to AudioAndVideoActivity")
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    binding.btnAr.setOnClickListener(mOnClickListener)
    binding.btnVr.setOnClickListener(mOnClickListener)
    binding.btnAav.setOnClickListener(mOnClickListener)
  }

  private inline fun <reified T : Activity> Context.start() {
    val intent = Intent()
    intent.setClass(this, T::class.java)
    startActivity(intent)
  }
}