package com.yelysei.hobbyharbor.ui.screens.main.hobbydetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemUserExperienceBinding
import com.yelysei.hobbyharbor.model.userhobbies.entities.Experience
import com.yelysei.hobbyharbor.utils.DateFormat
import com.yelysei.hobbyharbor.utils.DisplayedDateTime

interface HobbyDetailsActionListener {
    fun openExperience(experienceId: Int)

    fun editExperience(experience: Experience)
}

class UserExperiencesAdapter(
    private val hobbyDetailsActionListener: HobbyDetailsActionListener
) : RecyclerView.Adapter<UserExperiencesAdapter.ProgressHistoryViewHolder>(), View.OnClickListener {

    var userExperiences: List<Experience> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ProgressHistoryViewHolder(
        val binding: ItemUserExperienceBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserExperienceBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.buttonEditExperience.setOnClickListener(this)

        return ProgressHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = userExperiences.size

    override fun onBindViewHolder(holder: ProgressHistoryViewHolder, position: Int) {
        val userExperience = userExperiences[position]
        with(holder.binding) {
            holder.itemView.tag = userExperience
            buttonEditExperience.tag = userExperience
            val startTime = userExperience.startTime
            val endTime = userExperience.endTime
            if (DisplayedDateTime.getDateFormat(startTime, endTime) == DateFormat.SINGLE_DATE) {
                tvFirstDateField.text = DisplayedDateTime.displayedSingleDate(startTime, endTime)
                tvSecondDateField.text = DisplayedDateTime.displayedTime(startTime, endTime)
                tvHyphen.visibility = View.GONE
            } else {
                tvFirstDateField.text = DisplayedDateTime.displayedDateTime(startTime)
                tvSecondDateField.text = DisplayedDateTime.displayedDateTime(endTime)
                tvHyphen.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(v: View) {
        val userExperience = v.tag as Experience
        when (v.id) {
            R.id.experienceContainer -> {
                hobbyDetailsActionListener.openExperience(userExperience.id)
            }
            R.id.buttonEditExperience -> {
                hobbyDetailsActionListener.editExperience(userExperience)
            }
        }
    }

}