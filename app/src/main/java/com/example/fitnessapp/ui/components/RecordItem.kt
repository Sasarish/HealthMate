package com.example.fitnessapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.data.HealthRecord
import com.example.fitnessapp.ui.theme.AppBlack
import com.example.fitnessapp.ui.theme.AppWhite
import com.example.fitnessapp.ui.theme.BorderGrey
import com.example.fitnessapp.ui.theme.CaloriesRed
import com.example.fitnessapp.ui.theme.MidGrey
import com.example.fitnessapp.ui.theme.WaterBlue
import java.text.SimpleDateFormat
import java.util.Locale

//this card displays single health record
@Composable
fun RecordItem(
    record: HealthRecord,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppWhite),
        border = BorderStroke(1.dp, BorderGrey)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Date (example: 2026-11-12) and the day name (example: Thursday)
            Text(text = record.date, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = getDayName(record.date), fontSize = 13.sp, color = MidGrey)

            Spacer(modifier = Modifier.height(12.dp))

            //displaying 3 values - steps, calorie and water intake
            Row(modifier = Modifier.fillMaxWidth()) {
                StatColumn(
                    emoji = "👣",
                    value = formatNumber(record.steps),
                    unit = "steps",
                    valueColor = AppBlack,
                    modifier = Modifier.weight(1f)
                )
                StatColumn(
                    emoji = "🔥",
                    value = formatNumber(record.calories),
                    unit = "kcal",
                    valueColor = CaloriesRed,
                    modifier = Modifier.weight(1f)
                )
                StatColumn(
                    emoji = "💧",
                    value = formatNumber(record.water),
                    unit = "ml",
                    valueColor = WaterBlue,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Edit and delete Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onEditClick) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDeleteClick) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
private fun StatColumn(
    emoji: String,
    value: String,
    unit: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = emoji, fontSize = 20.sp)
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
        Text(text = unit, fontSize = 12.sp, color = MidGrey)
    }
}

//Turns date into day name
private fun getDayName(date: String): String {
    return try {
        val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        SimpleDateFormat("EEEE", Locale.getDefault()).format(parsed!!)
    } catch (e: Exception) {
        ""
    }
}

// Adds commas to big numbers
private fun formatNumber(number: Int): String {
    return String.format(Locale.US, "%,d", number)
}