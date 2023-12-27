package com.yelysei.hobbyharbor.screens.dialogs

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.AddCustomHobbyDialogBinding
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.screens.uiactions.UiActions

typealias OnAddCustomHobbySubmitClickListener = (hobby: Hobby) -> Unit
class AddCustomHobbyDialog(
    private val context: Context,
    private val uiActions: UiActions,
    private val categoryItems: List<String>,
    private val onSubmitClickListener: OnAddCustomHobbySubmitClickListener,
) : Dialog{

    private val binding: AddCustomHobbyDialogBinding by lazy {
        AddCustomHobbyDialogBinding.inflate(LayoutInflater.from(context))
    }

    private val categoryNameLayout: TextInputLayout = binding.categoryNameLayout
    private val editCategoryName: MaterialAutoCompleteTextView = binding.categoryNameInput
    private val hobbyNameLayout: TextInputLayout = binding.hobbyNameLayout
    private val editHobbyName: TextInputEditText = binding.hobbyNameInput
    private val editCost: MaterialAutoCompleteTextView = binding.costInput
    private val editPlace: MaterialAutoCompleteTextView = binding.placeInput
    private val editPeople: MaterialAutoCompleteTextView = binding.peopleInput

    lateinit var dialog: AlertDialog

    override fun show() {
        setUpMaterialAutoCompleteTextViews()
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.please_set_up_your_goal_for_the_hobby)
            .setView(binding.root)
            .setPositiveButton("Submit", null)
            .show()
        this.dialog = dialog
        setUpIMEActions()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
                submitAddCustomHobby()
            }
        }

    private fun submitAddCustomHobby() {
        try {
            validateFields()
            val categoryName = editCategoryName.text.toString()
            val hobbyName = editHobbyName.text.toString()
            val cost = if (editCost.text.toString() == "") { null } else { editCost.text.toString() }
            val place = if (editPlace.text.toString() == "") { null } else { editPlace.text.toString() }
            val people = if (editPeople.text.toString() == "") { null } else { editPeople.text.toString() }
            val hobby = Hobby(
                hobbyName = hobbyName.lowercase(),
                categoryName = categoryName.lowercase(),
                cost = cost?.lowercase(),
                place = place?.lowercase(),
                people = people?.lowercase()
            )
            onSubmitClickListener(hobby)
        } catch (e: ValidationException) {
            uiActions.toast(e.message!!)
        }
    }

    private fun setUpIMEActions() {
        editCategoryName.onIMAActionNextGoTo(editHobbyName)
        editHobbyName.onIMAActionNextGoTo(editCost)
        editCost.onIMAActionNextGoTo(editPlace)
        editPlace.onIMAActionNextGoTo(editPeople)
        editPeople.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                submitAddCustomHobby()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun EditText.onIMAActionNextGoTo(editText: EditText) {
        this.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                focusMaterialAutoCompleteTextView(editText)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
    private fun setUpMaterialAutoCompleteTextViews() {
        val costItems = listOf("Cheap", "Affordable", "Expensive")
        val placeItems = listOf("Home", "Outdoor", "Special")
        val peopleItems = listOf("Alone", "Small Group", "Big Group")
        setUpMaterialAutoCompleteTextView(editCategoryName, categoryItems)
        setUpMaterialAutoCompleteTextView(editCost, costItems)
        setUpMaterialAutoCompleteTextView(editPlace, placeItems)
        setUpMaterialAutoCompleteTextView(editPeople, peopleItems)
        setClickListenersOnItemSelected()
    }
    private fun setUpMaterialAutoCompleteTextView(materialAutoCompleteTextView: MaterialAutoCompleteTextView, items: List<String>) {
        val adapter = ArrayAdapter(context, R.layout.list_item, items)
        materialAutoCompleteTextView.setAdapter(adapter)
    }
    private fun setClickListenersOnItemSelected() {
        val autoCompleteTextViews = listOf(editCost, editPlace, editPeople)
        editCategoryName.setOnItemClickListener { _, _, _, _ ->
            editHobbyName.requestFocus()
        }
        for (i in autoCompleteTextViews.indices) {
            autoCompleteTextViews[i].setOnItemClickListener { _, _, _, _ ->
                // Focus on the next AutoCompleteTextView (if available)
                if (i < autoCompleteTextViews.size - 1) {
                    val autoCompleteTextView = autoCompleteTextViews[i + 1]
                    focusMaterialAutoCompleteTextView(autoCompleteTextView)
                }
            }
        }
    }
    private fun focusMaterialAutoCompleteTextView(editText: EditText) {
        editText.requestFocus()
        if (editText is MaterialAutoCompleteTextView) {
            editText.showDropDown()
        }
    }
    /**
     * @return true if there is error (editText is not Null or Blank)
     * @return false if there is no error (editText is filled)
     */
    private fun displayError (textInputLayout: TextInputLayout, editText: EditText, fieldName: String): Boolean {
        return if (editText.text.isNullOrBlank()) {
            textInputLayout.error = context.getString(R.string.field_is_required_error, fieldName)
            true
        } else {
            textInputLayout.error = null
            false
        }
    }

    private fun validateFields() {
        val categoryNameIsEmpty = displayError(categoryNameLayout, editCategoryName, "Category Name")
        val hobbyNameIsEmpty = displayError(hobbyNameLayout, editHobbyName, "Hobby Name")
        if (categoryNameIsEmpty || hobbyNameIsEmpty) {
            throw ValidationException("It is not possible to add new custom hobby when required fields are not filled")
        }
    }
    private class ValidationException(message: String) : Exception(message)
}
