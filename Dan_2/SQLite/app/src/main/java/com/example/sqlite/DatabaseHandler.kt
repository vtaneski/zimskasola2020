package com.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class DatabaseHandler (context: Context) :
    SQLiteOpenHelper(context, PersonEntry.DB_NAME, null, PersonEntry.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    //Inserting (Creating) data
    fun addPerson(person: Person): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(PersonEntry.FIRST_NAME, person.firstName)
            put(PersonEntry.LAST_NAME, person.lastName)
        }

        val success = db.insert(PersonEntry.TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedID", "$success")
        return (Integer.parseInt("$success") != -1)
    }

    //get all persons
    fun getAllPersons(): String {
        var allPerson = ""
        val db = readableDatabase

        Log.d("Tag", "Getting persons")

        val selectALLQuery = "SELECT * FROM ${PersonEntry.TABLE_NAME}"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex(PersonEntry.ID))
                    val firstName = cursor.getString(cursor.getColumnIndex(PersonEntry.FIRST_NAME))
                    val lastName = cursor.getString(cursor.getColumnIndex(PersonEntry.LAST_NAME))

                    allPerson = "$allPerson\n$id $firstName $lastName"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allPerson
    }

    /**
     * getAllPersons with the use of db.query()
     */
    /*fun getAllPersons(): String {
        var allPerson = ""
        val db = readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(PersonEntry.ID, PersonEntry.FIRST_NAME, PersonEntry.LAST_NAME)

        // Filter results WHERE "FirstName" = 'Viktor'
        val selection = "${PersonEntry.FIRST_NAME} = ?"
        val selectionArgs = arrayOf("Viktor")

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${PersonEntry.LAST_NAME} DESC"

        val cursor = db.query(
            PersonEntry.TABLE_NAME,     // The table to query
            projection,                 // The array of columns to return (pass null to get all)
            selection,                  // The columns for the WHERE clause
            selectionArgs,              // The values for the WHERE clause
            null,               // don't group the rows
            null,                // don't filter by row groups
            sortOrder                   // The sort order
        )

        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getString(cursor.getColumnIndex(PersonEntry.ID))
                        val firstName = cursor.getString(cursor.getColumnIndex(PersonEntry.FIRST_NAME))
                        val lastName = cursor.getString(cursor.getColumnIndex(PersonEntry.LAST_NAME))

                        allPerson = "$allPerson\n$id $firstName $lastName"
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        cursor.close()
        db.close()
        return allPerson
    }*/

    companion object PersonContract {
        // Table contents are grouped together in an anonymous object.
        object PersonEntry : BaseColumns {
            const val DB_NAME = "PersonsDB"
            const val DB_VERSION = 1
            const val TABLE_NAME = "persons"
            const val ID = "id"
            const val FIRST_NAME = "FirstName"
            const val LAST_NAME = "LastName"
        }
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${PersonEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${PersonEntry.FIRST_NAME} TEXT," +
                "${PersonEntry.LAST_NAME} TEXT)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PersonEntry.TABLE_NAME}"
}