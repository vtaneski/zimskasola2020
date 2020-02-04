package com.example.internalstorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSave.setOnClickListener {
            saveDataStream()
        }

        buttonRead.setOnClickListener {
            readDataStream()
            //getFilesList()
        }
    }

    private fun saveDataStream() {
        val fileName = editTextFileName.text.toString()
        val fileContent = editTextFileContent.text.toString()

        if (fileName.trim().isNotEmpty()) {
            openFileOutput(fileName, Context.MODE_PRIVATE)
                .use {
                    it.write(fileContent.toByteArray())
                }

            Toast.makeText(applicationContext, "Data saved!", Toast.LENGTH_LONG).show()
            editTextFileName.text.clear()
            editTextFileContent.text.clear()
        }
        else{
            Toast.makeText(applicationContext,"file name cannot be blank",Toast.LENGTH_LONG).show()
        }
    }

    private fun readDataStream() {
        val fileName = editTextFileName.text.toString()

        if (fileName.trim().isNotEmpty()) {
            val stringBuilder = StringBuilder()
            val bufferedReader = openFileInput(fileName).bufferedReader()

            var text: String? = null

            while({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            //Displaying data on EditText
            textViewContent.setText(stringBuilder.toString()).toString()
        }else{
            Toast.makeText(applicationContext,"file name cannot be blank",Toast.LENGTH_LONG).show()
        }
    }

    private fun getFilesList() {
        val files: Array<String> = fileList()
        val stringBuilder = StringBuilder()

        for (file in files) {
            stringBuilder.appendln(file)
        }

        //Displaying data on EditText
        textViewContent.setText(stringBuilder.toString()).toString()
    }
}
