package com.example.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //init db
    private val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //on Click Save button
        buttonSave.setOnClickListener {
            // checking input text should not be null
            if (validation()){
                val person = Person()
                var success = false
                person.firstName = editText_name.text.toString()
                person.lastName = editText_lastname.text.toString()

                success = dbHandler.addPerson(person)

                if (success){
                    Toast.makeText(this,"Saved Successfully", Toast.LENGTH_LONG).show()
                }
            }
        }

        buttonShow.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val persons = CoroutineScope(Dispatchers.IO).async {
                    //asynchronous call to database
                    dbHandler.getAllPersons()
                }.await()

                //after the asynchronous call, we can use the result and show it in the Main thread
                textView.text = persons
            }
        }
    }

    override fun onDestroy() {
        dbHandler.close()
        super.onDestroy()
    }

    private fun validation(): Boolean{
        var validate = false

        if (editText_name.text.toString() != "" &&
            editText_lastname.text.toString() != ""
        ){
            validate = true
        }else{
            validate = false
            Toast.makeText(this,"Fill all details", Toast.LENGTH_LONG).show()
        }

        return validate
    }
}
