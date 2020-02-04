package com.example.externalstorage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    private val util = PermissionUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSave.setOnClickListener {
            saveExternalDataStreamPermissions()
        }

        buttonRead.setOnClickListener {
            readExternalDataStreamPermissions()
            //getStorageLocations()
        }
    }

    /**
     * This method is called when a user Allow or Deny our requested permissions.
     * So it will help us to move forward if the permissions are granted.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            util.WRITE_EXTERNAL_PERMISSION_ID -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Granted. Start saving the file
                    val fileName = editTextFileName.text.toString()
                    val fileContent = editTextFileContent.text.toString()
                    saveExternalDataStream(fileName, fileContent)
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(
                            this,
                            "Write permission is needed in order to save the file",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        //permission from popup was denied
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            util.READ_EXTERNAL_PERMISSION_ID -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Granted. Start reading the file
                    val fileName = editTextFileName.text.toString()
                    readExternalDataStream(fileName)
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(
                            this,
                            "Read permission is needed in order to read the file",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        //permission from popup was denied
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /** Checks if a volume containing external storage is available
     * for read and write.
     */
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * Checks if a volume containing external storage is available to at least read.
     */
    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    private fun getStorageLocations() {
        val stringBuilder: StringBuilder = StringBuilder()

        val externalStorageVolumes: Array<out File> = ContextCompat.getExternalFilesDirs(applicationContext, null)
        var i=0
        for (file in externalStorageVolumes) {
            i++
            stringBuilder.appendln("$i: $file")
        }

        //Displaying data on EditText
        textViewContent.setText(stringBuilder.toString()).toString()
    }

    /**
     * Saves data to file in external storage
     */
    private fun saveExternalDataStreamPermissions() {
        val fileName = editTextFileName.text.toString()
        val fileContent = editTextFileContent.text.toString()

        if (fileName.trim() != "") {
            when (util.checkWriteExternalPermissions()) {
                true -> {
                    saveExternalDataStream(fileName, fileContent)
                }
                false -> {
                    util.requestWritePermissions()
                }
            }
        } else{
            Toast.makeText(applicationContext,"file name cannot be blank", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveExternalDataStream(fileName: String, fileContent: String) {
        val myExternalFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        if (isExternalStorageWritable()) {
            try {
                val fileOutputStream = FileOutputStream(myExternalFile)
                fileOutputStream.write(fileContent.toByteArray())
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Toast.makeText(applicationContext, "data save", Toast.LENGTH_SHORT).show()

            editTextFileName.text.clear()
            editTextFileContent.text.clear()
        } else{
            Toast.makeText(applicationContext,"media storage not writable", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Reads data from a file in external storage
     */
    private fun readExternalDataStreamPermissions() {
        val fileName = editTextFileName.text.toString()

        if (fileName.trim() != "") {
            when (util.checkReadExternalPermissions()) {
                true -> {
                    readExternalDataStream(fileName)
                }
                false -> {
                    util.requestReadPermissions()
                }
            }
        }else{
            Toast.makeText(applicationContext,"file name cannot be blank", Toast.LENGTH_LONG).show()
        }
    }

    private fun readExternalDataStream(fileName: String) {
        val myExternalFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        if (isExternalStorageReadable() && myExternalFile.exists()) {
            try {
                val fileInputStream = FileInputStream(myExternalFile)
                val inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder: StringBuilder = StringBuilder()
                var text: String? = null

                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text)
                }

                //Displaying data on EditText
                textViewContent.setText(stringBuilder.toString()).toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else{
            Toast.makeText(applicationContext,"media storage not readable", Toast.LENGTH_LONG).show()
        }
    }
}
