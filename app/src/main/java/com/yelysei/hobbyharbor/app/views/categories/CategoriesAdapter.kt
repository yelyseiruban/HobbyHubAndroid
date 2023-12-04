package com.yelysei.hobbyharbor.app.views.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemCategoryBinding

interface CategoriesActionListener {
    fun onCategoryClick(categoryName: String)
}

class CategoriesAdapter(
    private val categoriesActionListener: CategoriesActionListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>(), View.OnClickListener {

    var categories: List<String> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class CategoryViewHolder(
        val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryName = categories[position]
        with(holder.binding) {
            holder.itemView.tag = categoryName
            categoryIcon.setImageResource(icons[categoryName] ?: defaultCategoryIcon)
            tvCategoryName.text = categoryName
        }
    }

    override fun onClick(v: View) {
        val categoryName = v.tag as String
        when(v.id) {
            R.id.itemCategory -> {
                categoriesActionListener.onCategoryClick(categoryName)
            }
        }
    }

    companion object {
        private val icons: Map<String, Int> = mapOf(

        )

        private val defaultCategoryIcon: Int = R.drawable.ic_default_category
    }

}