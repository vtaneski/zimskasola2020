package com.example.externalstorage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil (private val activity: AppCompatActivity) {

    val READ_EXTERNAL_PERMISSION_ID = 1
    val WRITE_EXTERNAL_PERMISSION_ID = 2

    /**
     * Checks whether or not the user grant us to access READ_EXTERNAL_STORAGE.
     */
    fun checkReadExternalPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    /**
     * Checks whether or not the user grant us to access WRITE_EXTERNAL_STORAGE.
     */
    fun checkWriteExternalPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    /**
     * This method will request our necessary permissions to the user
     * to read from external storage,
     * if they are not already granted.
     */
    fun requestReadPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            READ_EXTERNAL_PERMISSION_ID
        )
    }

    /**
     * This method will request our necessary permissions to the user
     * to write to external storage,
     * if they are not already granted.
     */
    fun requestWritePermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            WRITE_EXTERNAL_PERMISSION_ID
        )
    }
}