package com.winmanboo.aav4android.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.winmanboo.aav4android.R
import com.winmanboo.aav4android.databinding.ItemAudioRecordBinding
import java.io.File

/**
 * @Author wzm
 * @Date 2022/5/19 18:05
 */
class SimpleAudioRecordAdapter :
  ListAdapter<File, SimpleAudioRecordAdapter.SimpleAudioRecordVH>(
    object : DiffUtil.ItemCallback<File>() {
      override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.nameWithoutExtension == newItem.nameWithoutExtension
      }

      override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.name == newItem.name
      }
    }
  ) {

  private var playerListener: OnPlayerListener? = null

  class SimpleAudioRecordVH(private val binding: ItemAudioRecordBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val format = binding.root.context.getString(R.string.file_size_format)

    fun bind(file: File) {
      binding.tvFileName.text = file.name
      binding.tvFileSize.text = format.format(calculateFileSize(file.length()))
    }

    private fun calculateFileSize(byteLength: Long): Float {
      return byteLength / 1024F
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleAudioRecordVH {
    val binding = ItemAudioRecordBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )

    val vh = SimpleAudioRecordVH(binding)

    binding.btnOp.setOnCheckedChangeListener { _, isChecked ->
      playerListener?.onPlayed(isChecked, getItem(vh.layoutPosition))
    }

    return vh
  }

  override fun onBindViewHolder(holder: SimpleAudioRecordVH, position: Int) {
    holder.bind(getItem(position))
  }

  fun setPlayListener(onPlayerListener: OnPlayerListener) {
    this.playerListener = onPlayerListener
  }

  fun interface OnPlayerListener {
    fun onPlayed(isChecked: Boolean, data: File)
  }
}