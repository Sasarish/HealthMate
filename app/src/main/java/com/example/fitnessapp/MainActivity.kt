package com.example.fitnessapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.fitnessapp.ui.screens.DashboardScreen
import com.example.fitnessapp.ui.screens.RecordListScreen
import com.example.fitnessapp.ui.theme.AppBlack
import com.example.fitnessapp.ui.theme.AppWhite
import com.example.fitnessapp.ui.theme.FitnessAppTheme
import com.example.fitnessapp.ui.theme.MidGrey
import com.example.fitnessapp.viewmodel.HealthViewModel

// The tabs shown in the bottom bar: a name and an icon for each one
enum class Tab(val label: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Home),
    RECORDS("Records", Icons.AutoMirrored.Filled.List)
}

// MainActivity is the first thing Android opens when the app starts.
class MainActivity : ComponentActivity() {

    // Create the ViewModel (Android keeps it alive while the app is open)
    private val viewModel: HealthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw the app under the status bar, and keep the status bar icons
        // dark so they stay visible on our white background.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            FitnessAppTheme {

                // Remembers which tab is open (it also survives screen rotation)
                var currentTab by rememberSaveable { mutableStateOf(Tab.DASHBOARD) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    // The bottom navigation bar
                    bottomBar = {
                        NavigationBar(containerColor = AppWhite) {
                            // Create one item for every tab in the enum
                            Tab.entries.forEach { tab ->
                                NavigationBarItem(
                                    selected = currentTab == tab,
                                    onClick = { currentTab = tab },
                                    icon = { Icon(tab.icon, contentDescription = tab.label) },
                                    label = { Text(tab.label) },
                                    // Selected = white icon on a black circle
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = AppWhite,
                                        selectedTextColor = AppBlack,
                                        indicatorColor = AppBlack,
                                        unselectedIconColor = MidGrey,
                                        unselectedTextColor = MidGrey
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // Show the screen that matches the selected tab
                    when (currentTab) {
                        Tab.DASHBOARD -> DashboardScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )

                        Tab.RECORDS -> RecordListScreen(
                            viewModel = viewModel,
                            onEditClick = { /* Step 6: open the edit form here */ },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}