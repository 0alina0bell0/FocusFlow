package com.example.focusflow.presentation.MainListScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.focusflow.ui.theme.Pink80

@Composable
fun MainListScreen(){

//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    navController.navigate(Routes.HabitCreate) },
//                containerColor = Pink80,
//            ) {
//                Icon(Icons.Default.Add, contentDescription = null)
//            }
//        }
//    ) { innerPadding ->
//        LazyColumn(
//            contentPadding = innerPadding,
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//            items(items) { item ->
//                ListItem(
//                    habit = item,
//                    onCheckedChange = { item, isChecked ->
//                        viewModel.updateHabit(item, isChecked)
//                    },
//                    onDelete = { viewModel.deleteHabit(item) }
//                )
//            }
//        }
//    }
}