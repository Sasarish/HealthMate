package com.example.fitnessapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
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
import com.example.fitnessapp.ui.screens.AddEditScreen
import com.example.fitnessapp.ui.screens.DashboardScreen
import com.example.fitnessapp.ui.screens.RecordListScreen
import com.example.fitnessapp.ui.theme.AppBlack
import com.example.fitnessapp.ui.theme.AppWhite
import com.example.fitnessapp.ui.theme.FitnessAppTheme
import com.example.fitnessapp.ui.theme.MidGrey
import com.example.fitnessapp.viewmodel.HealthViewModel

//Tabs under the bottom bar
enum class Tab(val label: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Home),
    RECORDS("Records", Icons.AutoMirrored.Filled.List)
}

// Project initial point
class MainActivity : ComponentActivity() {

    // Create the ViewModel
    private val viewModel: HealthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Displaying app under the status bar
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            FitnessAppTheme {

                // Remember which tab is currenty open
                var currentTab by rememberSaveable { mutableStateOf(Tab.DASHBOARD) }

                // showing add/ edit forms id this condition is true
                var showForm by rememberSaveable { mutableStateOf(false) }

                // When the form is open, back button close the form
                BackHandler(enabled = showForm) {
                    showForm = false
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    // The bottom navigation bar
                    bottomBar = {
                        if (!showForm) {
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
                    },

                    // open form button
                    floatingActionButton = {
                        if (!showForm) {
                            FloatingActionButton(
                                onClick = {
                                    viewModel.selectRecord(null)   // null = add a NEW record
                                    showForm = true
                                },
                                containerColor = AppBlack,
                                contentColor = AppWhite
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add record")
                            }
                        }
                    }
                ) { innerPadding ->

                    if (showForm) {
                        // The Add/Edit form
                        AddEditScreen(
                            viewModel = viewModel,
                            onClose = { showForm = false },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        // show the screen of the selected tab
                        when (currentTab) {
                            Tab.DASHBOARD -> DashboardScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(innerPadding)
                            )

                            Tab.RECORDS -> RecordListScreen(
                                viewModel = viewModel,
                                onEditClick = { record ->
                                    viewModel.selectRecord(record)  // the record to edit
                                    showForm = true
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}