package com.example.fitnessapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//This class interact with SQlite
class HealthDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "healthmate.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "health_records"
        const val COL_ID = "id"
        const val COL_DATE = "date"
        const val COL_STEPS = "steps"
        const val COL_CALORIES = "calories"
        const val COL_WATER = "water"
    }


    override fun onCreate(db: SQLiteDatabase) {
        // sql command to create the health_records table
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_DATE TEXT NOT NULL, " +
                "$COL_STEPS INTEGER NOT NULL, " +
                "$COL_CALORIES INTEGER NOT NULL, " +
                "$COL_WATER INTEGER NOT NULL)"
        db.execSQL(createTable)

        // initial dummy records for testing purpose
        addDummyData(db)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 7 days sample data for testing
    private fun addDummyData(db: SQLiteDatabase) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()   // starts as today

        val stepsList = listOf(7850, 9200, 6400, 10500, 5300, 8700, 7100)
        val caloriesList = listOf(420, 510, 350, 600, 290, 480, 390)
        val waterList = listOf(2000, 2500, 1800, 3000, 1500, 2200, 1900)

        for (i in stepsList.indices) {
            val values = ContentValues().apply {
                put(COL_DATE, formatter.format(calendar.time))
                put(COL_STEPS, stepsList[i])
                put(COL_CALORIES, caloriesList[i])
                put(COL_WATER, waterList[i])
            }
            db.insert(TABLE_NAME, null, values)

            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
    }

//creating new record function
    fun addRecord(record: HealthRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_DATE, record.date)
            put(COL_STEPS, record.steps)
            put(COL_CALORIES, record.calories)
            put(COL_WATER, record.water)
        }
        return db.insert(TABLE_NAME, null, values)
    }

//reading all records function
    fun getAllRecords(): List<HealthRecord> {
        val list = mutableListOf<HealthRecord>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, null, null, null, null,
            "$COL_DATE DESC, $COL_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToRecord(it))
            }
        }
        return list
    }

//search record function
    fun searchByDate(dateText: String): List<HealthRecord> {
        val list = mutableListOf<HealthRecord>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null,
            "$COL_DATE LIKE ?",             // the ? is replaced by the value below
            arrayOf("%$dateText%"),         // % means "anything before/after"
            null, null,
            "$COL_DATE DESC, $COL_ID DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToRecord(it))
            }
        }
        return list
    }

//update record function
    fun updateRecord(record: HealthRecord): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_DATE, record.date)
            put(COL_STEPS, record.steps)
            put(COL_CALORIES, record.calories)
            put(COL_WATER, record.water)
        }
        return db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(record.id.toString()))
    }

//delete record function
    fun deleteRecord(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }


    //turning current row into object
    private fun cursorToRecord(cursor: Cursor): HealthRecord {
        return HealthRecord(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
            date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
            steps = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STEPS)),
            calories = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CALORIES)),
            water = cursor.getInt(cursor.getColumnIndexOrThrow(COL_WATER))
        )
    }
}