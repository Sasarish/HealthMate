package com.example.fitnessapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.ui.theme.AppBlack
import com.example.fitnessapp.ui.theme.BorderGrey
import com.example.fitnessapp.ui.theme.MidGrey
import com.example.fitnessapp.viewmodel.FormErrors
import com.example.fitnessapp.viewmodel.HealthViewModel

//This form is for adding new records and update existing records
@Composable
fun AddEditScreen(
    viewModel: HealthViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // The record which is we are going to edit
    val record = viewModel.selectedRecord
    val isEditing = record != null

    // making sure when we editing the old value should appeared in text box
    var date by rememberSaveable { mutableStateOf(record?.date ?: viewModel.getTodayDate()) }
    var steps by rememberSaveable { mutableStateOf(record?.steps?.toString() ?: "") }
    var calories by rememberSaveable { mutableStateOf(record?.calories?.toString() ?: "") }
    var water by rememberSaveable { mutableStateOf(record?.water?.toString() ?: "") }

    // The error messages will show under the required fields
    var errors by remember { mutableStateOf(FormErrors()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // title
        Text(
            text = if (isEditing) "Edit Record" else "Add Record",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter your health data for one day",
            fontSize = 14.sp,
            color = MidGrey
        )

        Spacer(modifier = Modifier.height(20.dp))

        // input fields
        FormField(
            label = "Date (yyyy-MM-dd)",
            value = date,
            onValueChange = { date = it },
            error = errors.dateError,
            keyboardType = KeyboardType.Text
        )
        FormField(
            label = "👣 Steps",
            value = steps,
            onValueChange = { steps = it },
            error = errors.stepsError,
            keyboardType = KeyboardType.Number
        )
        FormField(
            label = "🔥 Calories burned (kcal)",
            value = calories,
            onValueChange = { calories = it },
            error = errors.caloriesError,
            keyboardType = KeyboardType.Number
        )
        FormField(
            label = "💧 Water intake (ml)",
            value = water,
            onValueChange = { water = it },
            error = errors.waterError,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(12.dp))

        // save button
        Button(
            onClick = {
                // requesting view model to validate the data and save
                errors = viewModel.saveRecord(
                    id = record?.id ?: 0,
                    date = date,
                    stepsText = steps,
                    caloriesText = calories,
                    waterText = water
                )

                // No errors = records are saved successfully
                if (!errors.hasError()) {
                    val message = if (isEditing) "Record updated" else "Record added"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    onClose()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (isEditing) "Update Record" else "Save Record",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cancel button
        OutlinedButton(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Cancel", fontSize = 16.sp)
        }
    }
}


@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        // showing error message under the field
        supportingText = if (error != null) {
            { Text(error) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppBlack,
            unfocusedBorderColor = BorderGrey,
            focusedLabelColor = AppBlack,
            cursorColor = AppBlack
        ),
        modifier = Modifier.fillMaxWidth()
    )
}