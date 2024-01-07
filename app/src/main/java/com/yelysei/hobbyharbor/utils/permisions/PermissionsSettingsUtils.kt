package com.yelysei.hobbyharbor.utils.permisions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionsSettingsUtils(
    private val activity: Activity,
    private val context: Context
) {
    fun showSettingsDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle("Permission Required")
            .setMessage("You have denied the storage permission. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        startActivity(context, intent, null)
    }
}