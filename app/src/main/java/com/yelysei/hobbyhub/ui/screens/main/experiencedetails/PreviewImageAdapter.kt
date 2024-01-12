package com.yelysei.hobbyhub.ui.screens.main.experiencedetails

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yelysei.hobbyhub.R
import com.yelysei.hobbyhub.databinding.ItemImageBinding
import com.yelysei.hobbyhub.ui.screens.main.experiencedetails.PreviewImageAdapter.ImageViewHolder
import com.yelysei.hobbyhub.utils.resources.AttributeUtils

interface PreviewImagesActionListener {
    fun onToggleListener()

    fun onUnToggleListener()
}

class PreviewImageAdapter(
    private val actionListener: PreviewImagesActionListener
) : RecyclerView.Adapter<ImageViewHolder>(), View.OnClickListener {

    data class ImageUriItem(
        val imageUri: Uri,
        var isSelected: Boolean
    )

    private var imageUriItems: List<ImageUriItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun areImageUriItems() = imageUriItems.isNotEmpty()

    fun proceedImages(selectedUris: List<Uri>) {
        this.imageUriItems += selectedUris.indices.map {
            ImageUriItem(
                selectedUris[it],
                false
            )
        }
    }

    fun currentImageUris(): List<Uri> {
        return imageUriItems.map {
            it.imageUri
        }
    }

    class ImageViewHolder(
        val binding: ItemImageBinding
    ) : ViewHolder(binding.root)

    var isToggleEnabled: Boolean = false
    private var selectingAvailable: Boolean = true

    private val longPressHandler = Handler(Looper.getMainLooper())
    private val longPressRunnable = Runnable {
        if (longPressedItemPosition != RecyclerView.NO_POSITION) {
            val imageUriItem = imageUriItems[longPressedItemPosition]
            imageUriItem.isSelected = true
            actionListener.onToggleListener()
            isToggleEnabled = true
            notifyItemChanged(longPressedItemPosition)
        }
    }
    private var longPressedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ImageViewHolder(binding)
    }

    private fun selectedImageUris(): List<Uri> {
        val selectedImageUris = mutableListOf<Uri>()
        imageUriItems.forEach { imageUriItem ->
            if (imageUriItem.isSelected) {
                selectedImageUris += imageUriItem.imageUri
            }
        }
        return selectedImageUris
    }

    override fun getItemCount(): Int = imageUriItems.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUriItem = imageUriItems[position]
        val imageUri: Uri = imageUriItem.imageUri
        val imageView = holder.binding.root

        holder.itemView.tag = imageUriItem

        holder.itemView.setOnLongClickListener {
            if (!isToggleEnabled && selectingAvailable) {
                longPressedItemPosition = holder.bindingAdapterPosition
                longPressHandler.postDelayed(longPressRunnable, LONG_PRESS_DURATION)
                true // Consume the long click
            } else {
                false
            }
        }

        val attributeUtils = AttributeUtils(holder.binding.root, R.styleable.PinItem)

        with(holder.binding.root) {
            background =
                if (isToggleEnabled) {
                    if (imageUriItem.isSelected) {
                        attributeUtils.getDrawableFromAttribute(R.styleable.PinItem_pinSelectedBackground)
                    } else {
                        attributeUtils.getDrawableFromAttribute(R.styleable.PinItem_pinDefaultBackground)
                    }
                } else {
                    attributeUtils.getDrawableFromAttribute(R.styleable.PinItem_pinDefaultBackground)
                }
        }
        imageView.setImageURI(imageUri)
    }

    override fun onClick(v: View) {
        val imageUriItem = v.tag as ImageUriItem
        val position = imageUriItems.indexOf(imageUriItem)

        when (v.id) {
            R.id.imageContainer -> {
                if (isToggleEnabled) {
                    // Toggle the item and notify data set changed
                    imageUriItem.isSelected = !imageUriItem.isSelected
                    notifyItemChanged(position)
                    if (selectedImageUris().isEmpty()) {
                        this.isToggleEnabled = false
                        actionListener.onUnToggleListener()
                    }
                }
            }
        }
    }

    fun blockSelectingImages() {
        selectingAvailable = false
    }

    fun unBlockSelectingImages() {
        selectingAvailable = true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun unselectImageUris() {
        imageUriItems.forEach { it.isSelected = false }
        this.isToggleEnabled = false
        actionListener.onUnToggleListener()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeSelectedImageUris() {
        imageUriItems = imageUriItems.filter {
            !it.isSelected
        }
        notifyDataSetChanged()
    }

    companion object {
        private const val LONG_PRESS_DURATION = 300L // 0.3 second
    }

}