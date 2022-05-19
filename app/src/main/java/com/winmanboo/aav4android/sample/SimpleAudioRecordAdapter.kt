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
    return SimpleAudioRecordVH(
      ItemAudioRecordBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: SimpleAudioRecordVH, position: Int) {
    holder.bind(getItem(position))
  }
}