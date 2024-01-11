package com.yelysei.hobbyharbor.ui.screens.main.hobbydetails

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.SharedPreferences
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.model.UserHobbyIsNotLoadedException
import com.yelysei.hobbyharbor.model.results.takeSuccess
import com.yelysei.hobbyharbor.model.userhobbies.entities.Experience
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.ui.dialogs.OnExperienceTimeSubmitClickListener
import com.yelysei.hobbyharbor.ui.dialogs.OnRemoveNotificationClickListener
import com.yelysei.hobbyharbor.ui.dialogs.OnSubmitSetNotificationClickListener
import com.yelysei.hobbyharbor.ui.dialogs.SetGoalDialog.ProgressState
import com.yelysei.hobbyharbor.ui.dialogs.SetNotificationDialog
import com.yelysei.hobbyharbor.ui.dialogs.prepareDialog
import com.yelysei.hobbyharbor.ui.dialogs.showExperienceDialogs
import com.yelysei.hobbyharbor.ui.fab.setMovableBehavior
import com.yelysei.hobbyharbor.ui.screens.main.BaseFragment
import com.yelysei.hobbyharbor.utils.CustomTypeface
import com.yelysei.hobbyharbor.utils.notifications.NotificationBroadcast
import com.yelysei.hobbyharbor.utils.permisions.PermissionsSettingsUtils
import com.yelysei.hobbyharbor.utils.resources.AttributeUtils
import com.yelysei.hobbyharbor.utils.viewModelCreator
import java.util.Calendar

class UserHobbyDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentUserHobbyDetailsBinding

    private val args: UserHobbyDetailsFragmentArgs by navArgs()

    private val viewModel by viewModelCreator {
        UserHobbyDetailsViewModel(
            args.uhId,
            Repositories.userHobbiesRepository,
            SharedPreferences.sharedStorage
        )
    }

    private val buttonNotificationResourceActive = R.drawable.ic_notification_active
    private val buttonNotificationResourceDeActive = R.drawable.ic_notification_deactive

    private val requestPostNotificationsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onGotPostNotificationsResult
    )

    private val permissionsSettingsUtils by lazy {
        PermissionsSettingsUtils(requireActivity(), requireContext())
    }

    private fun onGotPostNotificationsResult(granted: Boolean) {
        if (granted) {
            openDialogForNotifications()
        } else {
            // example of handling 'Deny & don't ask again' user choice
            if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                permissionsSettingsUtils.showSettingsDialog()
            } else {
                uiActions.toast(stringResources.getString(R.string.permission_denied))
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUserHobbyDetailsBinding.inflate(inflater, container, false)
        val hobbyName = args.hobbyName
        binding.toolbarTitle.text = CustomTypeface.capitalizeEachWord(hobbyName)
        val adapter = UserExperiencesAdapter(object : HobbyDetailsActionListener {
            override fun openExperience(experienceId: Int) {
                findNavController().navigate(
                    UserHobbyDetailsFragmentDirections.actionUserHobbyDetailsFragmentToExperienceDetailsFragment(
                        experienceId,
                        args.hobbyName
                    )
                )
            }

            override fun editExperience(experience: Experience) {
                onEditExperienceClick(experience)
            }
        })
        binding.recyclerViewUserExperiences.adapter = adapter

        viewModel.userHobby.observe(viewLifecycleOwner) { result ->
            com.yelysei.hobbyharbor.ui.screens.renderSimpleResult(
                binding.root,
                result
            ) { userHobby ->

                if (userHobby.getProgressInHours() >= userHobby.progress.goal) {
                    uiActions.toast(stringResources.getString(R.string.reached_goal_message))
                }

                binding.tvGoalValue.text = userHobby.progress.goal.toString()
                binding.tvTotalValue.text = userHobby.getProgressInHours().toString()
                adapter.userExperiences = userHobby.progress.experiences

                binding.buttonEditGoal.setOnClickListener {
                    val setGoalDialog = prepareDialog(
                        onSubmitClickListener = {
                            viewModel.updateProgress(it)
                        },
                        progressState = ProgressState(
                            userHobby.progress.goal,
                            userHobby.getProgressInHours().toInt()
                        )
                    )
                    setGoalDialog.show()
                }

                binding.buttonNotification.setImageResource(
                    if (isNotificationSet()) {
                        buttonNotificationResourceActive
                    } else {
                        buttonNotificationResourceDeActive
                    }
                )

            }
        }



        com.yelysei.hobbyharbor.ui.screens.recyclerViewConfigureView(
            com.yelysei.hobbyharbor.ui.screens.Configuration(
                recyclerView = binding.recyclerViewUserExperiences,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 64,
            )
        )

        binding.buttonAddExperience.setMovableBehavior()
        val attributeUtils = AttributeUtils(binding.root, R.styleable.FabView)
        val fabColorStateList =
            ColorStateList.valueOf(attributeUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        attributeUtils.onClear()
        binding.buttonAddExperience.supportImageTintList = fabColorStateList
        binding.buttonAddExperience.setOnClickListener {
            onAddExperienceClick()
        }

        binding.buttonNavigationUp.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonNotification.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPostNotificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                openDialogForNotifications()
            }
        }

        return binding.root
    }

    private fun isNotificationSet(): Boolean {
        val intent = Intent(context, NotificationBroadcast::class.java).apply {
            putExtra(NotificationBroadcast.EXTRA_CHANNEL_ID, getChannelId())
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_ID, viewModel.getNotificationId())
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_TITLE, CustomTypeface.capitalizeEachWord(args.hobbyName))
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_CONTENT, "It is time to do ${CustomTypeface.capitalizeEachWord(args.hobbyName)}")
        }
        val pendingIntent = PendingIntent.getBroadcast(context, viewModel.userHobby.value.takeSuccess()?.hobby?.id ?: throw IllegalStateException(), intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent != null
    }

    private fun updateNotification(intervalInMilliseconds: Long) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationBroadcast::class.java).apply {
            putExtra(NotificationBroadcast.EXTRA_CHANNEL_ID, getChannelId())
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_ID, viewModel.getNotificationId())
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_TITLE, CustomTypeface.capitalizeEachWord(args.hobbyName))
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_CONTENT, "It is time to do ${CustomTypeface.capitalizeEachWord(args.hobbyName)}")
        }
        val pendingIntent = PendingIntent.getBroadcast(context, viewModel.userHobby.value.takeSuccess()?.hobby?.id ?: throw IllegalStateException(), intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalInMilliseconds,
            pendingIntent
        )
    }

    private fun removeNotification() {
        val intent = Intent(context, NotificationBroadcast::class.java).apply {
            putExtra(NotificationBroadcast.EXTRA_CHANNEL_ID, getChannelId())
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_ID, viewModel.getNotificationId())
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_TITLE, CustomTypeface.capitalizeEachWord(args.hobbyName))
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_CONTENT, "It is time to do ${CustomTypeface.capitalizeEachWord(args.hobbyName)}")
        }
        val pendingIntent = PendingIntent.getBroadcast(context, viewModel.userHobby.value.takeSuccess()?.hobby?.id ?: throw IllegalStateException(), intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun createNotificationChannel() {
        val name = "Reminder ${args.hobbyName}"
        val description = "Channel for reminder ${args.hobbyName}"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(getChannelId(), name, importance)
        channel.description = description
        val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun onAddExperienceClick() {
        val dialogTitle = "Add new experience:"
        val submitClickListener: OnExperienceTimeSubmitClickListener = { from: Long, till: Long ->
            try {
                viewModel.addUserExperience(from, till)
                uiActions.toast(stringResources.getString(R.string.experience_added_confirmation))
            } catch (e: UserHobbyIsNotLoadedException) {
                uiActions.toast(stringResources.getString(R.string.experience_added_error))
            }
        }
        showExperienceDialogs(dialogTitle, submitClickListener)
    }

    private fun onEditExperienceClick(experience: Experience) {
        val dialogTitle = "Edit your experience:"
        val submitClickListener = { till: Long, from: Long ->
            try {
                viewModel.editUserExperience(till, from, experience.id)
                uiActions.toast(stringResources.getString(R.string.change_user_experience_confirmation))
            } catch (e: UserHobbyIsNotLoadedException) {
                uiActions.toast(stringResources.getString(R.string.change_user_experience_error))
            }
        }
        val previousFrom = experience.startTime
        val previousTill = experience.endTime
        showExperienceDialogs(
            dialogTitle,
            submitClickListener,
            previousFrom,
            previousTill
        )
    }


    private fun openDialogForNotifications() {
        val hobbyName = CustomTypeface.capitalizeEachWord(viewModel.userHobby.value.takeSuccess()!!.hobby.hobbyName).toString()
        val onPositiveButtonClickListener: OnSubmitSetNotificationClickListener = {
            uiActions.toast(
                stringResources.getString(R.string.notification_added, hobbyName)
            )
            viewModel.activeNotification(it)
            updateNotification(it)
            binding.buttonNotification.setImageResource(buttonNotificationResourceActive)
        }

        val onNegativeButtonClickListener: OnRemoveNotificationClickListener = {
            uiActions.toast(
                stringResources.getString(R.string.notificaiton_removed, hobbyName)
            )
            viewModel.deActiveNotification()
            removeNotification()
            binding.buttonNotification.setImageResource(buttonNotificationResourceDeActive)
        }
        val previousInternalTimeInMilliseconds = viewModel.previousInternalTimeInMilliseconds()
        SetNotificationDialog(
            requireContext(),
            isNotificationSet(),
            previousInternalTimeInMilliseconds,
            onPositiveButtonClickListener,
            onNegativeButtonClickListener
        ).show()
    }

    private fun getChannelId() = "channelId ${args.hobbyName}"
}
