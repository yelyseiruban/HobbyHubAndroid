package com.yelysei.hobbyharbor.ui.screens.main.hobbydetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemUserActionBinding
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.utils.getDate
import com.yelysei.hobbyharbor.utils.getTime

interface HobbyDetailsActionListener {
    fun editAction(userAction: Action)
}

class UserActionsAdapter(
    private val hobbyDetailsActionListener: HobbyDetailsActionListener
) : RecyclerView.Adapter<UserActionsAdapter.ProgressHistoryViewHolder>(), View.OnClickListener {

    var userActions: List<Action> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ProgressHistoryViewHolder(
        val binding: ItemUserActionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserActionBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ProgressHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = userActions.size

    override fun onBindViewHolder(holder: ProgressHistoryViewHolder, position: Int) {
        val userAction = userActions[position]
        with(holder.binding) {
            holder.itemView.tag = userAction
            val startDate = getDate(userAction.startTime)
            val endDate = getDate(userAction.endTime)
            var displayedDate = startDate
            if (startDate != endDate) {
                displayedDate = "$startDate - $endDate"
            }
            tvActionDate.text = displayedDate
            val timeBorders = "${getTime(userAction.startTime)} - ${getTime(userAction.endTime)}"
            tvTimeBorders.text = timeBorders
        }
    }

    override fun onClick(v: View) {
        val userAction = v.tag as Action
        when (v.id) {
            R.id.experienceContainer -> {
                hobbyDetailsActionListener.editAction(userAction)
            }
        }
    }

}