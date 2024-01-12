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
import com.yelysei.hobbyhub.model.userhobbies.entities.ImageReference
import com.yelysei.hobbyhub.ui.screens.main.experiencedetails.ShownImageAdapter.ImageViewHolder
import com.yelysei.hobbyhub.utils.resources.AttributeUtils

interface ShownImagesActionListener {
    fun onToggleListener(imageReferences: List<ImageReference>)

    fun onUnToggleListener()

    fun onChangeSelectedImagesListener(imageReferences: List<ImageReference>)
}

class ShownImageAdapter(
    private val actionListener: ShownImagesActionListener
) : RecyclerView.Adapter<ImageViewHolder>(), View.OnClickListener {

    data class ImageReferenceItem(
        val imageReference: ImageReference,
        val uri: Uri,
        var isSelected: Boolean
    )

    private var imageReferenceItems: List<ImageReferenceItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @SuppressLint("NotifyDataSetChanged")
    fun proceedImageReferences(imageReferences: List<ImageReference>) {
        this.imageReferenceItems = imageReferences.map { imageReference ->
            ImageReferenceItem(
                imageReference,
                Uri.parse(imageReference.uriReference),
                false
            )
        }
    }

    private fun selectedImageReferences(): List<ImageReference> {
        val selectedImageReferences = mutableListOf<ImageReference>()
        imageReferenceItems.forEach { imageReferenceItem ->
            if (imageReferenceItem.isSelected) {
                selectedImageReferences += imageReferenceItem.imageReference
            }
        }
        return selectedImageReferences
    }

    class ImageViewHolder(
        val binding: ItemImageBinding
    ) : ViewHolder(binding.root)

    var isToggleEnabled: Boolean = false
    private var selectingAvailable: Boolean = true

    private val longPressHandler = Handler(Looper.getMainLooper())
    private val longPressRunnable = Runnable {
        if (longPressedItemPosition != RecyclerView.NO_POSITION) {
            val imageUriItem = imageReferenceItems[longPressedItemPosition]
            imageUriItem.isSelected = true
            actionListener.onToggleListener(selectedImageReferences())
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

    override fun getItemCount(): Int = imageReferenceItems.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUriItem = imageReferenceItems[position]
        val imageUri: Uri = imageUriItem.uri
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
        val imageUriItem = v.tag as ImageReferenceItem
        val position = imageReferenceItems.indexOf(imageUriItem)

        when (v.id) {
            R.id.imageContainer -> {
                if (isToggleEnabled) {
                    // Toggle the item and notify data set changed
                    imageUriItem.isSelected = !imageUriItem.isSelected
                    actionListener.onChangeSelectedImagesListener(selectedImageReferences())
                    notifyItemChanged(position)
                    if (selectedImageReferences().isEmpty()) {
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
        imageReferenceItems.forEach { it.isSelected = false }
        this.isToggleEnabled = false
        actionListener.onUnToggleListener()
        notifyDataSetChanged()
    }

    companion object {
        private const val LONG_PRESS_DURATION = 300L // 0.3 second
    }

}