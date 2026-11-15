package com.example.fitnessapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.data.HealthRecord
import com.example.fitnessapp.ui.components.RecordItem
import com.example.fitnessapp.ui.theme.AppBlack
import com.example.fitnessapp.ui.theme.BorderGrey
import com.example.fitnessapp.ui.theme.MidGrey
import com.example.fitnessapp.viewmodel.HealthViewModel

//search box and list of all saved records
@Composable
fun RecordListScreen(
    viewModel: HealthViewModel,
    onEditClick: (HealthRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    //context to show in toast message
    val context = LocalContext.current

   //confirming which record to delete
    var recordToDelete by remember { mutableStateOf<HealthRecord?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        // Title
        Text(
            text = "Records",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "${viewModel.records.size} record(s) found",
            fontSize = 14.sp,
            color = MidGrey
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search box
        OutlinedTextField(
            value = viewModel.searchText,
            onValueChange = { viewModel.onSearchTextChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by date (e.g. 2026-11-12)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            // displaying clear button
            trailingIcon = {
                if (viewModel.searchText.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchTextChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppBlack,
                unfocusedBorderColor = BorderGrey,
                cursorColor = AppBlack
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // List of record section
        if (viewModel.records.isEmpty()) {
            // displaying no records if no records
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 40.sp)
                    Text(
                        text = "No records found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Try a different date",
                        fontSize = 14.sp,
                        color = MidGrey,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(viewModel.records, key = { it.id }) { record ->
                    RecordItem(
                        record = record,
                        onEditClick = { onEditClick(record) },
                        // opens the dialog
                        onDeleteClick = { recordToDelete = record }
                    )
                }
            }
        }
    }

    //Delete confirmation
    recordToDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { recordToDelete = null },   // tapped outside the dialog
            title = { Text("Delete record?") },
            text = { Text("The record for ${record.date} will be removed permanently.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteRecord(record)
                    Toast.makeText(context, "Record deleted", Toast.LENGTH_SHORT).show()
                    recordToDelete = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}