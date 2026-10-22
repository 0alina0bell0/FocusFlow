package com.example.focusflow.presentation.MainListScreen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.focusflow.data.Entity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateItemScreen(
    onBackClick: () -> Unit,
    onSaveClick: (Entity) -> Unit
//    onBackClick: () -> Unit = {},
//    onSaveClick: (Entity) -> Unit = {}
){

    var title by rememberSaveable { mutableStateOf("") }
    val isSaveEnabled = title.isNotBlank()

    val context = LocalContext.current
    val zoneId = ZoneId.systemDefault()
    var priority by rememberSaveable { mutableIntStateOf(2) }

    var selectedEpochDay by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedHour by rememberSaveable {mutableIntStateOf(-1) }
    var selectedMinute by rememberSaveable {mutableIntStateOf(-1) }

    val selectedDate = selectedEpochDay?.let { LocalDate.ofEpochDay(it) }
    val dateFormatter = DateTimeFormatter.ofPattern(
        "dd MMMM yyyy",
        Locale.getDefault()
    )


    fun openDatePicker() {
        val initialDate = selectedDate ?: LocalDate.now()

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = LocalDate.of(
                    year,
                    month + 1,
                    dayOfMonth
                )

                selectedEpochDay = date.toEpochDay()
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        ).show()
    }

    fun openTimePicker() {
        val currentTime = LocalTime.now()

        val initialHour = if (selectedHour >= 0) {
            selectedHour
        } else {
            currentTime.hour
        }

        val initialMinute = if (selectedMinute >= 0) {
            selectedMinute
        } else {
            currentTime.minute
        }

        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Новая задача")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { newTitle ->
                    title = newTitle
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Задача")
                },
                placeholder = {
                    Text("Например: подготовить отчёт")
                },
                minLines = 3,
                maxLines = 5
            )

            Text(
                text = "Когда напомнить",
                style = MaterialTheme.typography.titleMedium
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        openDatePicker()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = selectedDate?.format(dateFormatter)
                            ?: "Выбрать день"
                    )
                }

                OutlinedButton(
                    onClick = {
                        openTimePicker()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (selectedHour >= 0) {

                            String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                selectedHour,
                                selectedMinute
                            )
                        } else {
                            "Выбрать время"
                        }
                    )
                }
            }

            Text(
                text = "Приоритет",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriorityChip(
                    title = "Низкий",
                    selected = priority == 1,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        priority = 1
                    }
                )

                PriorityChip(
                    title = "Средний",
                    selected = priority == 2,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        priority = 2
                    }
                )

                PriorityChip(
                    title = "Высокий",
                    selected = priority == 3,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        priority = 3
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val date = requireNotNull(selectedDate)

                    val reminderDateTime = LocalDateTime.of(
                        date,
                        LocalTime.of(
                            selectedHour,
                            selectedMinute
                        )
                    )

                    val reminderMillis = reminderDateTime
                        .atZone(zoneId)
                        .toInstant()
                        .toEpochMilli()

                    val newTask = Entity(
                        title = title.trim(),
                        isCompleted = false,
                        reminder = reminderMillis,
                        dueAt = null,
                        priority = priority
                    )

                    onSaveClick(newTask)
                },
                enabled = isSaveEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}


@Composable
private fun PriorityChip(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(title)
        },
        modifier = modifier
    )
}




