package com.yelysei.hobbyharbor.ui.screens.main.experiencedetails

import android.Manifest
import android.app.Activity
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentExperienceDetailsBinding
import com.yelysei.hobbyharbor.model.userhobbies.entities.ImageReference
import com.yelysei.hobbyharbor.ui.dialogs.ConfirmRemoveItemsDialog
import com.yelysei.hobbyharbor.ui.screens.HorizontalSpaceItemDecoration
import com.yelysei.hobbyharbor.ui.screens.main.BaseFragment
import com.yelysei.hobbyharbor.ui.screens.renderExperienceDetailsResult
import com.yelysei.hobbyharbor.utils.CustomTypeface
import com.yelysei.hobbyharbor.utils.DateFormat
import com.yelysei.hobbyharbor.utils.DisplayedDateTime
import com.yelysei.hobbyharbor.utils.UriUtils.getImageUrlWithAuthority
import com.yelysei.hobbyharbor.utils.permisions.PermissionsSettingsUtils
import com.yelysei.hobbyharbor.utils.resources.AttributeUtils
import com.yelysei.hobbyharbor.utils.viewModelCreator


class ExperienceDetailsFragment : BaseFragment() {

    private val args: ExperienceDetailsFragmentArgs by navArgs()

    private val viewModel by viewModelCreator {
        ExperienceDetailsViewModel(
            args.experienceId,
            Repositories.userHobbiesRepository
        )
    }

    private lateinit var binding: FragmentExperienceDetailsBinding
    private lateinit var previewImageAdapter: PreviewImageAdapter
    private lateinit var shownImageAdapter: ShownImageAdapter
    private lateinit var fabColorStateList: ColorStateList
    private var notToggledMainButtonIconResource: Int = R.drawable.ic_add
    private var currentMainButtonIconResource: Int = notToggledMainButtonIconResource
    private val toggledMainButtonIconResource: Int = R.drawable.ic_remove

    private var selectedImageUris: List<Uri> = listOf()
    private var currentFabAction: FABAction = FABAction.Observe

    private val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    private val requestReadImagesPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onGotReadImagesResult
    )

    private val permissionsSettingsUtils by lazy {
        PermissionsSettingsUtils(requireActivity(), requireContext())
    }

    private fun onGotReadImagesResult(granted: Boolean) {
        if (granted) {
            openGalleryForImages()
        } else {
            // example of handling 'Deny & don't ask again' user choice
            if (!shouldShowRequestPermissionRationale(readImagePermission)) {
                permissionsSettingsUtils.showSettingsDialog()
            } else {
                uiActions.toast(stringResources.getString(R.string.permission_denied))
            }
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (previewImageAdapter.isToggleEnabled) {
                previewImageAdapter.unselectImageUris()
            } else if (shownImageAdapter.isToggleEnabled) {
                shownImageAdapter.unselectImageUris()
            } else if (viewModel.isEditState.value == true) {
                viewModel.changeEditState()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExperienceDetailsBinding.inflate(inflater, container, false)
        prepareGalleryRecyclerViews()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.experiencePin.observe(viewLifecycleOwner) { result ->
            renderExperienceDetailsResult(binding.constraintLayout, result) { experiencePin ->
                val startTime = experiencePin.experience.startTime
                val endTime = experiencePin.experience.endTime
                displayDateTitle(startTime, endTime)
                displayNoteText(experiencePin.experience.note)
                displayShownImages(experiencePin.imageReferences)
                val attributeUtils = AttributeUtils(binding.root, R.styleable.FabView)
                notToggledMainButtonIconResource =
                    if (experiencePin.imageReferences.isNotEmpty() || experiencePin.experience.note != null) {
                        //Modify
                        R.drawable.ic_edit
                    } else {
                        //Add
                        R.drawable.ic_add
                    }
                currentMainButtonIconResource = notToggledMainButtonIconResource
                displayFAB()
                attributeUtils.onClear()
            }
        }

        viewModel.isEditState.observe(viewLifecycleOwner) { isEditState ->
            if (isEditState) {
                shownImageAdapter.blockSelectingImages()
                previewImageAdapter.unBlockSelectingImages()
                binding.addPinContainer.visibility = View.VISIBLE
            } else {
                shownImageAdapter.unBlockSelectingImages()
                previewImageAdapter.blockSelectingImages()
                binding.addPinContainer.visibility = View.GONE
            }
        }

        binding.uploadImagesButton.setOnClickListener {
            requestReadImagesPermissionLauncher.launch(readImagePermission)
        }

        val attributeUtils = AttributeUtils(binding.root, R.styleable.FabView)
        fabColorStateList =
            ColorStateList.valueOf(attributeUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        attributeUtils.onClear()

        declareFABEditObserveClickListeners()

        binding.uploadImagesButton.supportImageTintList = fabColorStateList

        return binding.root
    }

    private fun displayFAB() {
        binding.fabAddPin.iconTint = fabColorStateList
        binding.fabAddPin.setTextColor(fabColorStateList)
        binding.fabAddPin.setIconResource(currentMainButtonIconResource)
    }

    private fun displayShownImages(imageReferences: List<ImageReference>) {
        if (imageReferences.isNotEmpty()) {
            binding.shownGalleryLayout.visibility = View.VISIBLE
            shownImageAdapter.proceedImageReferences(imageReferences)
        } else {
            binding.shownGalleryLayout.visibility = View.GONE
        }
    }

    private fun displayNoteText(noteText: String?) {
        if (noteText != null) {
            binding.noteTextLayout.visibility = View.VISIBLE
            binding.notesInput.setText(noteText)
            binding.noteText.text = noteText
        } else {
            binding.noteTextLayout.visibility = View.GONE
        }
    }

    private fun displayDateTitle(startTime: Long, endTime: Long) {
        if (DisplayedDateTime.getDateFormat(startTime, endTime) == DateFormat.SINGLE_DATE) {
            binding.dateHyphen.visibility = View.GONE
            binding.tvFirstDateField.text =
                DisplayedDateTime.displayedSingleDate(startTime, endTime)
            binding.tvSecondDateField.text = DisplayedDateTime.displayedTime(startTime, endTime)
        } else {
            binding.dateHyphen.visibility = View.VISIBLE
            binding.tvFirstDateField.text = DisplayedDateTime.displayedDateTime(startTime)
            binding.tvSecondDateField.text = DisplayedDateTime.displayedDateTime(endTime)
        }

    }

    private fun prepareGalleryRecyclerViews() {
        fun sharedOnUnToggleListener() {
            currentMainButtonIconResource = notToggledMainButtonIconResource
            binding.fabAddPin.setIconResource(currentMainButtonIconResource)
            declareFABEditObserveClickListeners()
        }

        val previewGalleryActionListener = object : PreviewImagesActionListener {
            override fun onToggleListener() {
                currentMainButtonIconResource = toggledMainButtonIconResource
                binding.fabAddPin.setOnClickListener {
                    removeSelectedImagesFromPreviewGallery()
                }
                updateFabTextAndIcon("Remove")
            }

            override fun onUnToggleListener() {
                sharedOnUnToggleListener()
                updateFabTextAndIcon("Submit")
                currentFabAction = FABAction.Edit
            }
        }

        val shownGalleryActionListener = object : ShownImagesActionListener {
            override fun onToggleListener(imageReferences: List<ImageReference>) {
                currentMainButtonIconResource = toggledMainButtonIconResource
                binding.fabAddPin.setOnClickListener {
                    removeSelectedImagesFromShownGallery(imageReferences)
                }
                updateFabTextAndIcon("Remove")
            }

            override fun onUnToggleListener() {
                sharedOnUnToggleListener()
                binding.fabAddPin.shrink()
                currentFabAction = FABAction.Observe
            }

            override fun onChangeSelectedImagesListener(imageReferences: List<ImageReference>) {
                binding.fabAddPin.setOnClickListener {
                    removeSelectedImagesFromShownGallery(imageReferences)
                }
            }
        }

        previewImageAdapter = PreviewImageAdapter(previewGalleryActionListener)
        shownImageAdapter = ShownImageAdapter(shownGalleryActionListener)
        prepareGalleryRecyclerView(binding.previewGallery)
        binding.previewGallery.adapter = previewImageAdapter
        prepareGalleryRecyclerView(binding.shownGallery)
        binding.shownGallery.adapter = shownImageAdapter
    }

    private fun prepareGalleryRecyclerView(
        galleryRecyclerView: RecyclerView
    ) {
        galleryRecyclerView.addItemDecoration(HorizontalSpaceItemDecoration(30))
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        galleryRecyclerView.layoutManager = layoutManager
    }

    private fun openGalleryForImages() {
        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        resultLauncher.launch(galleryIntent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val selectedImages: MutableList<Uri> = mutableListOf()

                if (data?.clipData != null) {
                    val clipData = data.clipData
                    for (i in 0 until clipData!!.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        selectedImages.add(uri)
                    }
                } else if (data?.data != null) {
                    val uri = data.data
                    selectedImages.add(uri!!)
                }

                Log.d("Selected Images", selectedImages.toString())
                previewImageAdapter.proceedImages(selectedImages)
                selectedImageUris += selectedImages
                binding.previewGalleryLayout.visibility = View.VISIBLE
            }
        }

    private fun declareFABEditObserveClickListeners() {
        binding.fabAddPin.setOnClickListener {
            if (currentFabAction == FABAction.Observe) {
                updateFabTextAndIcon("Submit")
                viewModel.changeEditState()
                currentFabAction = FABAction.Edit
            } else if (currentFabAction == FABAction.Edit) {
                val notesInputText = if (binding.notesInput.text.toString() == "") {
                    null
                } else {
                    binding.notesInput.text.toString()
                }
                val selectedImageUris = if (previewImageAdapter.currentImageUris().isEmpty()) {
                    null
                } else {
                    val selectedImageUrlsWithAuthority = mutableListOf<String>()
                    previewImageAdapter.currentImageUris().forEach { uri ->
                        val urlWithAuthority = getImageUrlWithAuthority(requireContext(), uri)
                        if (urlWithAuthority != null) {
                            selectedImageUrlsWithAuthority += urlWithAuthority
                        }
                    }
                    selectedImageUrlsWithAuthority.toList()
                }
                viewModel.savePin(notesInputText, selectedImageUris)
                binding.addPinContainer.visibility = View.GONE
                binding.fabAddPin.shrink()
                viewModel.changeEditState()
                currentFabAction = FABAction.Observe
            }
        }
    }

    private fun removeSelectedImagesFromPreviewGallery() {
        previewImageAdapter.removeSelectedImageUris()
        if (!previewImageAdapter.areImageUriItems()) {
            binding.previewGalleryLayout.visibility = View.GONE
        }
    }

    private fun removeSelectedImagesFromShownGallery(imageReferences: List<ImageReference>) {
        val onPositiveButtonClickListener = OnClickListener { _, _ ->
            viewModel.removeImageUris(imageReferences)
            uiActions.toast(
                stringResources.getString(
                    R.string.deleted_items_toast,
                    imageReferences.size.toString()
                )
            )
            shownImageAdapter.unselectImageUris()
        }
        val onNegativeButtonClickListener = OnClickListener { _, _ ->
            shownImageAdapter.unselectImageUris()
        }
        val removeImagesDialog = ConfirmRemoveItemsDialog(
            requireContext(),
            stringResources.getString(R.string.remove_images_title),
            stringResources.getString(R.string.remove_images_message),
            onPositiveButtonClickListener,
            onNegativeButtonClickListener
        )
        removeImagesDialog.show()
    }

    private fun updateFabTextAndIcon(fabText: String) {
        binding.fabAddPin.text = fabText
        binding.fabAddPin.shrink()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.fabAddPin.setIconResource(currentMainButtonIconResource)
            binding.fabAddPin.extend()
        }, 300)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hobbyName = arguments?.getString("hobbyName") ?: getString(R.string.app_name)
        requireActivity().findViewById<TextView>(R.id.toolbarTitle).text =
            CustomTypeface.capitalizeEachWord(hobbyName)
    }
}

enum class FABAction {
    Observe, Edit
}