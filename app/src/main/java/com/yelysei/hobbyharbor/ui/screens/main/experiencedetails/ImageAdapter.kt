package com.yelysei.hobbyharbor.ui.screens.main.experiencedetails

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yelysei.hobbyharbor.databinding.ItemImageBinding
import com.yelysei.hobbyharbor.ui.screens.main.experiencedetails.ImageAdapter.ImageViewHolder

class ImageAdapter(

) : RecyclerView.Adapter<ImageViewHolder>() {

    private var previewImageUris: List<Uri> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun selectImages(selectedUris: List<Uri>) {
        previewImageUris +=  selectedUris
        notifyDataSetChanged()
    }

    fun selectedImageUris(): List<Uri> {
        return previewImageUris
    }

    class ImageViewHolder(
        val binding: ItemImageBinding
    ) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)

//        binding.root.setOnClickListener(this)

        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = previewImageUris.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri: Uri = previewImageUris[position]
        val imageView = holder.binding.root
        imageView.setImageURI(imageUri)
    }

}