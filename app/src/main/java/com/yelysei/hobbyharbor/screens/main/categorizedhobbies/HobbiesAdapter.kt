package com.yelysei.hobbyharbor.screens.main.categorizedhobbies

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemHobbyBinding
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.utils.CustomTypeface

class HobbiesAdapter(
    private val context: Context,
    private val onHobbyClickListener: (hobby: Hobby) -> Unit
) : RecyclerView.Adapter<ViewHolder>(), View.OnClickListener {

    var hobbies: List<HobbyItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun processHobbies(hobbies: List<Hobby>) {
        var currentCategory = ""
        val hobbyItems: List<HobbyItem> = hobbies.map { hobby ->
            if (currentCategory != hobby.categoryName) {
                currentCategory = hobby.categoryName
                HobbyItem(hobby, true)
            } else {
                HobbyItem(hobby, false)
            }
        }
        this.hobbies = hobbyItems
    }

    class HobbyViewHolder(
        val binding: ItemHobbyBinding
    ) : ViewHolder(binding.root)

    class CategoryViewHolder(
        val binding: ItemHobbyBinding
    ) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHobbyBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return when (viewType) {
            WITHOUT_CATEGORY -> HobbyViewHolder(binding)
            WITH_CATEGORY -> CategoryViewHolder(binding)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hobbies[position].isCategoryShown) {
            WITH_CATEGORY
        } else {
            WITHOUT_CATEGORY
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hobbyItem = hobbies[position]
        holder.itemView.tag = hobbyItem

        when(holder) {
            is HobbyViewHolder -> {
                holder.binding.categoryContainer.visibility = View.GONE
                holder.binding.tvHobbyName.text = CustomTypeface.capitalizeEachWord(hobbyItem.hobby.hobbyName)
            }
            is CategoryViewHolder -> {
                holder.binding.categoryContainer.visibility = View.VISIBLE
                holder.binding.categoryContainer.text = CustomTypeface.capitalizeEachWord(hobbyItem.hobby.categoryName)
                holder.binding.categoryContainer.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(context, icons[hobbyItem.hobby.categoryName] ?: R.drawable.ic_default_category),
                    null,
                    null,
                    null
                )
                holder.binding.tvHobbyName.text = CustomTypeface.capitalizeEachWord(hobbyItem.hobby.hobbyName)
            }
        }
    }

    override fun getItemCount(): Int = hobbies.size

    override fun onClick(v: View) {
        val hobbyItem = v.tag as HobbyItem
        if (v.id == R.id.itemHobby) {
            onHobbyClickListener(hobbyItem.hobby)
        }
    }

    companion object {
        private const val WITHOUT_CATEGORY = 1
        private const val WITH_CATEGORY = 2

        /**
         * categoryName to icon resource
         */
        private val icons: Map<String, Int> = mapOf()
    }
    data class HobbyItem(
        val hobby: Hobby,
        val isCategoryShown: Boolean
    )
}