package com.example.fitnessapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapp.ui.components.SummaryCard
import com.example.fitnessapp.ui.theme.AppBlack
import com.example.fitnessapp.ui.theme.AppWhite
import com.example.fitnessapp.ui.theme.BorderGrey
import com.example.fitnessapp.ui.theme.CaloriesRed
import com.example.fitnessapp.ui.theme.WaterBlue
import com.example.fitnessapp.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Daily goals for achieve
private const val STEPS_GOAL = 10000
private const val CALORIES_GOAL = 500
private const val WATER_GOAL = 2500

// Dash board screen is to show summary of today's activities
@Composable
fun DashboardScreen(viewModel: HealthViewModel, modifier: Modifier = Modifier) {

    // today's date in correct format
    val todayText = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())

    // using Column in order to place thing one below other
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Title and today's date
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AppBlack)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Today",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppWhite
                )
                Text(text = todayText, fontSize = 14.sp, color = BorderGrey)
            }
        }

        //Daily steps card
        SummaryCard(
            emoji = "👣",
            title = "Steps",
            value = viewModel.todaySteps,
            unit = "steps",
            goal = STEPS_GOAL,
            accentColor = AppBlack
        )

        // Calories card
        SummaryCard(
            emoji = "🔥",
            title = "Calories Burned",
            value = viewModel.todayCalories,
            unit = "kcal",
            goal = CALORIES_GOAL,
            accentColor = CaloriesRed
        )

        // Water card
        SummaryCard(
            emoji = "💧",
            title = "Water Intake",
            value = viewModel.todayWater,
            unit = "ml",
            goal = WATER_GOAL,
            accentColor = WaterBlue
        )
    }
}