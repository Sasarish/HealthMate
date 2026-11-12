package com.example.fitnessapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.fitnessapp.data.HealthDatabaseHelper
import com.example.fitnessapp.data.HealthRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Error handling
data class FormErrors(
    val dateError: String? = null,
    val stepsError: String? = null,
    val caloriesError: String? = null,
    val waterError: String? = null
) {
    // this will return true if one field have an error
    fun hasError(): Boolean {
        return dateError != null || stepsError != null ||
                caloriesError != null || waterError != null
    }
}

class HealthViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = HealthDatabaseHelper(application)

    var records by mutableStateOf<List<HealthRecord>>(emptyList())
        private set

    var searchText by mutableStateOf("")
        private set

    var todaySteps by mutableIntStateOf(0)
        private set
    var todayCalories by mutableIntStateOf(0)
        private set
    var todayWater by mutableIntStateOf(0)
        private set

    var selectedRecord by mutableStateOf<HealthRecord?>(null)
        private set

    // init runs once when the ViewModel is created in order tot load data
    init {
        loadRecords()
    }

 //Configuring date format
    fun getTodayDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    //Read records
    private fun loadRecords() {
        //Showing all the records, if the search box is empty
        records = if (searchText.isBlank()) {
            dbHelper.getAllRecords()
        } else {
            dbHelper.searchByDate(searchText.trim())
        }

        val todayRecords = dbHelper.searchByDate(getTodayDate())
        todaySteps = todayRecords.sumOf { it.steps }
        todayCalories = todayRecords.sumOf { it.calories }
        todayWater = todayRecords.sumOf { it.water }
    }

    // handling searchbox text
    fun onSearchTextChange(newText: String) {
        searchText = newText
        loadRecords()
    }

    //confirming which record is being edited
    fun selectRecord(record: HealthRecord?) {
        selectedRecord = record
    }

// validate the record and make sure whether this is add new record or update existing record
    fun saveRecord(
        id: Int,
        date: String,
        stepsText: String,
        caloriesText: String,
        waterText: String
    ): FormErrors {
        // 1. Check every field
        val errors = FormErrors(
            dateError = checkDate(date.trim()),
            stepsError = checkNumber(stepsText.trim(), "Steps", 100000),
            caloriesError = checkNumber(caloriesText.trim(), "Calories", 10000),
            waterError = checkNumber(waterText.trim(), "Water", 10000)
        )

        // throwing errors
        if (errors.hasError()) {
            return errors
        }

        // formating the record
        val record = HealthRecord(
            id = id,
            date = date.trim(),
            steps = stepsText.trim().toInt(),
            calories = caloriesText.trim().toInt(),
            water = waterText.trim().toInt()
        )

        // create or update record
        if (id == 0) {
            dbHelper.addRecord(record)
        } else {
            dbHelper.updateRecord(record)
        }

        // reload the screen in order to show new/ updated records
        loadRecords()
        return errors
    }

// delete the record
    fun deleteRecord(record: HealthRecord) {
        dbHelper.deleteRecord(record.id)
        loadRecords()
    }


    // validating the correct date
    private fun checkDate(date: String): String? {
        if (date.isBlank()) return "Date is required"
        if (!Regex("\\d{4}-\\d{2}-\\d{2}").matches(date)) {
            return "Use the format yyyy-MM-dd (example: 2026-11-12)"
        }
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        formatter.isLenient = false
        try {
            formatter.parse(date)
        } catch (e: Exception) {
            return "This date does not exist"
        }
        // prevent users from enter future date
        if (date > getTodayDate()) return "Date cannot be in the future"
        return null
    }

    // validating number vakue
    private fun checkNumber(text: String, name: String, max: Int): String? {
        if (text.isBlank()) return "$name is required"
        val number = text.toIntOrNull() ?: return "$name must be a whole number"
        if (number < 0) return "$name cannot be negative"
        if (number > max) return "$name cannot be more than $max"
        return null
    }
}