package com.yelysei.hobbyharbor.ui.screens.main.experiencedetails

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentExperienceDetailsBinding
import com.yelysei.hobbyharbor.ui.fab.appear
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
    private lateinit var previewImageAdapter: ImageAdapter
    private lateinit var shownImageAdapter: ImageAdapter
    private lateinit var fabColorStateList: ColorStateList
    private var selectedImageUris: List<Uri> = listOf()
    private var isEditState: Boolean = false

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExperienceDetailsBinding.inflate(inflater, container, false)
        prepareGalleryRecyclerViews()

        viewModel.experiencePin.observe(viewLifecycleOwner) { result ->
            renderExperienceDetailsResult(binding.constraintLayout, result) { experiencePin ->
                val startTime = experiencePin.experience.startTime
                val endTime = experiencePin.experience.endTime
                if (DisplayedDateTime.getDateFormat(startTime, endTime) == DateFormat.SINGLE_DATE) {
                    binding.dateHyphen.visibility = View.GONE
                    binding.tvFirstDateField.text = DisplayedDateTime.displayedSingleDate(startTime, endTime)
                    binding.tvSecondDateField.text = DisplayedDateTime.displayedTime(startTime, endTime)
                } else {
                    binding.dateHyphen.visibility = View.VISIBLE
                    binding.tvFirstDateField.text = DisplayedDateTime.displayedDateTime(startTime)
                    binding.tvSecondDateField.text = DisplayedDateTime.displayedDateTime(endTime)
                }
                if (experiencePin.experience.note != null) {
                    binding.noteTextLayout.visibility = View.VISIBLE
                    binding.noteText.text = experiencePin.experience.note
                } else {
                    binding.noteTextLayout.visibility = View.GONE
                }
                if (experiencePin.uriReferences.isNotEmpty()) {
                    binding.shownGalleryLayout.visibility = View.VISIBLE
                    val uriReferences = experiencePin.uriReferences.map {
                        Uri.parse(it)
                    }
                    shownImageAdapter.selectImages(uriReferences)
                } else {
                    binding.shownGalleryLayout.visibility = View.GONE
                }
            }
        }

        viewModel.isEditState.observe(viewLifecycleOwner) { isEditState ->
            if (isEditState) {
                binding.addPinContainer.visibility = View.VISIBLE
            } else {
                binding.addPinContainer.visibility = View.GONE
            }
        }

        binding.uploadImagesButton.setOnClickListener {
            requestReadImagesPermissionLauncher.launch(readImagePermission)
        }

        val attributeUtils = AttributeUtils(binding.root, R.styleable.FabView)
        fabColorStateList =
            ColorStateList.valueOf(attributeUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        firstAppearFABAddPin()
        attributeUtils.onClear()

        return binding.root
    }

    private fun prepareGalleryRecyclerViews() {
        previewImageAdapter = ImageAdapter()
        shownImageAdapter = ImageAdapter()
        prepareGalleryRecyclerView(binding.previewGallery, previewImageAdapter)
        prepareGalleryRecyclerView(binding.shownGallery, shownImageAdapter)
    }

    private fun prepareGalleryRecyclerView(galleryRecyclerView: RecyclerView, imageAdapter: ImageAdapter) {
        galleryRecyclerView.addItemDecoration(HorizontalSpaceItemDecoration(30))
        galleryRecyclerView.adapter = imageAdapter
        galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun openGalleryForImages() {
        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        resultLauncher.launch(galleryIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
            previewImageAdapter.selectImages(selectedImages)
            selectedImageUris += selectedImages
            binding.previewGallery.visibility = View.VISIBLE
        }
    }


    private fun firstAppearFABAddPin() {
        binding.fabAddPin.appear()
        binding.fabAddPin.setOnClickListener {
            if (isEditState) {
                // Submit functionality
                val notesInputText = if (binding.notesInput.text.toString() == "") {
                    null
                } else {
                    binding.notesInput.text.toString()
                }
                val selectedImageUris = if (previewImageAdapter.selectedImageUris().isEmpty()) {
                    null
                } else {
                    val selectedImageUrlsWithAuthority = mutableListOf<String>()
                    previewImageAdapter.selectedImageUris().forEach { uri ->
                        val urlWithAuthority = getImageUrlWithAuthority(requireContext(), uri)
                        if (urlWithAuthority != null) {
                            selectedImageUrlsWithAuthority += urlWithAuthority
                        }
                    }
                    selectedImageUrlsWithAuthority.toList()
                }
                viewModel.savePin(notesInputText, selectedImageUris)
                binding.addPinContainer.visibility = View.GONE
                hideFAB()
            } else {
                // Show Pin Editor functionality
                extendFAB()
            }
            viewModel.changeEditState()
            isEditState = !isEditState
        }
    }


    private fun extendFAB() {
        binding.fabAddPin.text = "Submit"
        binding.fabAddPin.extend()
    }

    private fun hideFAB() {
        binding.fabAddPin.shrink()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hobbyName = arguments?.getString("hobbyName") ?: getString(R.string.app_name)
        requireActivity().findViewById<TextView>(R.id.toolbarTitle).text =
            CustomTypeface.capitalizeEachWord(hobbyName)
    }
}
