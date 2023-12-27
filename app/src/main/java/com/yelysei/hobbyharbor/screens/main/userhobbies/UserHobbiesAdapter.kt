package com.yelysei.hobbyharbor.screens.main.userhobbies

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemUserHobbyBinding
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.utils.CustomTypeface

interface UserHobbyActionListener {
    fun onUserHobbyDetails(userHobby: UserHobby)
    fun onRemoveListener(userHobby: UserHobby)
}

class UserHobbiesAdapter(
    recyclerView: RecyclerView,
    private val context: Context,
    private val actionListener: UserHobbyActionListener
) : RecyclerView.Adapter<UserHobbiesAdapter.UserHobbiesViewHolder>(), View.OnClickListener {

    var swipeHandler: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val swipedPosition = viewHolder.absoluteAdapterPosition
            val swipedHobby = userHobbies[swipedPosition]
            // Notify the listener about the removed hobby
            actionListener.onRemoveListener(swipedHobby)
        }
    }

    init {
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    var userHobbies: List<UserHobby> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class UserHobbiesViewHolder(
        val binding: ItemUserHobbyBinding
    ) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHobbiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserHobbyBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return UserHobbiesViewHolder(binding)
    }

    override fun onClick(v: View) {
        val userHobby = v.tag as UserHobby
        when(v.id) {
            R.id.userHobbyContainer -> {
                actionListener.onUserHobbyDetails(userHobby)
            }
        }
    }
    override fun getItemCount(): Int = userHobbies.size

    override fun onBindViewHolder(holder: UserHobbiesViewHolder, position: Int) {
        val uHobby = userHobbies[position]
        val progressPercent = (uHobby.getProgressInHours() / uHobby.progress.goal.toDouble()) * 100
        with(holder.binding) {
            holder.itemView.tag = uHobby
            hobbyName.text = CustomTypeface.capitalizeEachWord(uHobby.hobby.hobbyName)
            categoryName.text = context.getString(R.string.wrappedInBrackerts, CustomTypeface.capitalizeEachWord(uHobby.hobby.categoryName))
            currentProgress.text = uHobby.getProgressInHours().toString()
            progressGoal.text = uHobby.progress.goal.toString()
            progressBar.progress = progressPercent.toInt()
        }
    }

    fun resetSwipeState(position: Int) {
        notifyItemChanged(position)
    }

}