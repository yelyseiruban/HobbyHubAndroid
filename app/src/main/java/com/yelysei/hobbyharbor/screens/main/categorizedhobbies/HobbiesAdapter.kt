package com.yelysei.hobbyharbor.screens.main.categorizedhobbies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemHobbyBinding
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby

class HobbiesAdapter(
    private val onHobbyClickListener: (hobby: Hobby) -> Unit
) : RecyclerView.Adapter<HobbiesAdapter.HobbiesViewHolder>(), View.OnClickListener {

    var hobbies: List<Hobby> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HobbiesViewHolder(
        val binding: ItemHobbyBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHobbyBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return HobbiesViewHolder(binding)
    }

    override fun getItemCount(): Int = hobbies.size

    override fun onBindViewHolder(holder: HobbiesViewHolder, position: Int) {
        val hobby = hobbies[position]
        holder.itemView.tag = hobby
        holder.binding.tvHobbyName.text = hobby.hobbyName
    }

    override fun onClick(v: View) {
        val hobby = v.tag as Hobby
        if (v.id == R.id.itemHobby) {
            onHobbyClickListener(hobby)
        }
    }
}