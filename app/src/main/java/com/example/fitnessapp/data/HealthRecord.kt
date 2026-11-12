package com.example.fitnessapp.data

//This is daily health record model - single
data class HealthRecord(
    val id: Int = 0,
    val date: String,
    val steps: Int,
    val calories: Int,
    val water: Int
)