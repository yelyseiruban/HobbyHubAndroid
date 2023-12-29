import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.ItemUserHobbyBinding
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.utils.AttributeUtils
import com.yelysei.hobbyharbor.utils.CustomTypeface

interface UserHobbyActionListener {
    fun onUserHobbyDetails(userHobby: UserHobby)
    fun onChangeSelectedHobbiesListener(userHobbies: List<UserHobby>)

    fun onToggleListener(userHobbies: List<UserHobby>)
    fun onUnToggleListener()
}

class UserHobbiesAdapter(
    private val context: Context,
    private val actionListener: UserHobbyActionListener,
) : RecyclerView.Adapter<UserHobbiesAdapter.UserHobbiesViewHolder>(), View.OnClickListener {

    data class UserHobbyItem(
        val userHobby: UserHobby,
        var isSelected: Boolean
    )

    private var userHobbyItems: List<UserHobbyItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun proceedUserHobbies(userHobbies: List<UserHobby>) {
        this.userHobbyItems = userHobbies.indices.map {
            UserHobbyItem(
                userHobbies[it],
                false
            )
        }
    }

    class UserHobbiesViewHolder(
        val binding: ItemUserHobbyBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var isToggleEnabled: Boolean = false
        get() = field
        private set

    private val longPressHandler = Handler(Looper.getMainLooper())
    private val longPressRunnable = Runnable {
        if (longPressedItemPosition != RecyclerView.NO_POSITION) {
            val userHobbyItem = userHobbyItems[longPressedItemPosition]
            userHobbyItem.isSelected = true
            actionListener.onToggleListener(listOf(userHobbyItem.userHobby))
            isToggleEnabled = true
            notifyItemChanged(longPressedItemPosition)
        }
    }
    private var longPressedItemPosition: Int = RecyclerView.NO_POSITION

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHobbiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserHobbyBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return UserHobbiesViewHolder(binding)
    }

    override fun onClick(v: View) {
        val userHobbyItem = v.tag as UserHobbyItem
        val position = userHobbyItems.indexOf(userHobbyItem)

        when (v.id) {
            R.id.userHobbyContainer -> {
                if (isToggleEnabled) {
                    // Toggle the item and notify data set changed
                    userHobbyItem.isSelected = !userHobbyItem.isSelected
                    if (userHobbyItem.isSelected) {
                        actionListener.onChangeSelectedHobbiesListener(selectedUserHobbies())
                    }
                    notifyItemChanged(position)
                    if (selectedUserHobbies().isEmpty()) {
                        this.isToggleEnabled = false
                        actionListener.onUnToggleListener()
                    }
                } else {
                    // Handle other click actions (e.g., show details)
                    actionListener.onUserHobbyDetails(userHobbyItem.userHobby)
                }
            }
        }
    }

    private fun selectedUserHobbies(): List<UserHobby> {
        val selectedUserHobbies = mutableListOf<UserHobby>()
        userHobbyItems.forEach { userHobbyItem ->
            if (userHobbyItem.isSelected) {
                selectedUserHobbies += userHobbyItem.userHobby
            }
        }
        return selectedUserHobbies
    }

    override fun getItemCount(): Int = userHobbyItems.size

    override fun onBindViewHolder(holder: UserHobbiesViewHolder, position: Int) {
        val uHobbyItem = userHobbyItems[position]
        val uHobby = uHobbyItem.userHobby
        val progressPercent = (uHobby.getProgressInHours() / uHobby.progress.goal.toDouble()) * 100
        holder.itemView.setOnLongClickListener {
            if (!isToggleEnabled) {
                longPressedItemPosition = holder.bindingAdapterPosition
                longPressHandler.postDelayed(longPressRunnable, LONG_PRESS_DURATION)
                true // Consume the long click
            } else {
                false
            }
        }
        with(holder.binding) {
            holder.itemView.tag = uHobbyItem
            hobbyName.text = CustomTypeface.capitalizeEachWord(uHobby.hobby.hobbyName)
            categoryName.text =
                context.getString(
                    R.string.wrappedInBrackets,
                    CustomTypeface.capitalizeEachWord(uHobby.hobby.categoryName)
                )
            currentProgress.text = uHobby.getProgressInHours().toString()
            progressGoal.text = uHobby.progress.goal.toString()
            progressBar.progress = progressPercent.toInt()
            val attributeUtils = AttributeUtils(holder.binding.root, R.styleable.UserHobbyItem)

            if (isToggleEnabled) {
                if (uHobbyItem.isSelected) {
                    this.root.background =
                        attributeUtils.getDrawableFromAttribute(R.styleable.UserHobbyItem_selectedBackground)
                } else {
                    this.root.background =
                        attributeUtils.getDrawableFromAttribute(R.styleable.UserHobbyItem_defaultBackground)
                }
            } else {
                this.root.background =
                    attributeUtils.getDrawableFromAttribute(R.styleable.UserHobbyItem_defaultBackground)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun unselectUserHobbies() {
        this.userHobbyItems.forEach { it.isSelected = false }
        this.isToggleEnabled = false
        actionListener.onUnToggleListener()
        notifyDataSetChanged()
    }

    companion object {
        private const val LONG_PRESS_DURATION = 300L // 0.3 second
    }
}