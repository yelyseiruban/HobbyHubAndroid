package com.yelysei.hobbyharbor.screens.main.userhobbies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemUserHobbyBinding
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours

interface UserHobbyActionListener {
    fun onUserHobbyDetails(userHobby: UserHobby)
}

class UserHobbiesAdapter(
    private val actionListener: UserHobbyActionListener
) : RecyclerView.Adapter<UserHobbiesAdapter.UserHobbiesViewHolder>(), View.OnClickListener {

    var userHobbies: List<UserHobby> = emptyList()
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
            hobbyName.text = uHobby.hobby.hobbyName
            currentProgress.text = uHobby.getProgressInHours().toString()
            progressGoal.text = uHobby.progress.goal.toString()
            progressBar.progress = progressPercent.toInt()
        }
    }

}