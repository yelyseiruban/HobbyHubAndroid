package com.yelysei.hobbyharbor.views.userhobbies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemUserHobbyBinding
import com.yelysei.hobbyharbor.model.hobbies.UserHobby

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
        val progressPercent = (uHobby.progress.currentProgress.toDouble() / uHobby.progress.goal.toDouble()) * 100
        with(holder.binding) {
            holder.itemView.tag = uHobby
            hobbyName.text = uHobby.hobby.hobbyName
            currentProgress.text = uHobby.progress.currentProgress.toString()
            progressGoal.text = uHobby.progress.goal.toString()
            progressBar.progress = progressPercent.toInt()
        }
    }

}