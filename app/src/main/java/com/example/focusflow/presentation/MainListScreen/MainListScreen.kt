package com.example.focusflow.presentation.MainListScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.focusflow.navigation.Routes
import com.example.focusflow.ui.theme.BitterYell

@Composable
fun MainListScreen(
    viewModel: ViewModelListScreen = hiltViewModel(),
    navController: NavController
){

    val tasks by viewModel.allTasks.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.CREATE) },
                containerColor = BitterYell,
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {

            items(tasks) { item ->
                ListItem(
                    itemEntity = item,
                    onCheckedChange = { item, isChecked ->
                        viewModel.updateTask(item, isChecked)
                    },
                    onDelete = { viewModel.deleteTask(item) }
                )
            }
        }
    }
}