package com.nezuko.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nezuko.domain.dto.FileDto
import com.nezuko.main.databinding.ItemFileBinding

class FilesAdapter(
    private val onItemClick: (FileDto) -> Unit
) : PagingDataAdapter<FileDto, FilesAdapter.FilesViewHolder>(DiffCallback) {
    private val TAG = "FilesAdapter"

    init {
        Log.i(TAG, "FilesAdapter: start")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilesViewHolder {
        val binding = ItemFileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FilesViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: FilesViewHolder,
        position: Int
    ) {
        val file = getItem(position)
        file?.let { holder.bind(it)}
    }


    inner class FilesViewHolder(
        private val binding: ItemFileBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: FileDto) {
            binding.fileName.text = file.fileName
            binding.root.setOnClickListener {
                onItemClick(file)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FileDto>() {
            override fun areItemsTheSame(oldItem: FileDto, newItem: FileDto) =
                oldItem.fileId == newItem.fileId

            override fun areContentsTheSame(oldItem: FileDto, newItem: FileDto) =
                oldItem == newItem
        }
    }
}