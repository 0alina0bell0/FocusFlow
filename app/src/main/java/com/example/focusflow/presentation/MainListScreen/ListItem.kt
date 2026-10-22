package com.example.focusflow.presentation.MainListScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.focusflow.data.Entity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun ListItem(
    itemEntity: Entity,
    onCheckedChange: (Entity, Boolean) -> Unit,
    onDelete: () -> Unit
) {

    val dateFormatter = remember {
        DateTimeFormatter.ofPattern(
            "dd MMMM yyyy, HH:mm",
            Locale.getDefault()
        )
    }

    fun formatDateTime(timestamp: Long): String {
        return Instant
            .ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = itemEntity.isCompleted,
                onCheckedChange = { isChecked ->
                    onCheckedChange(
                        itemEntity,
                        isChecked
                    )
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = itemEntity.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (itemEntity.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemEntity.dueAt?.let { dueAt ->
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Выполнить: ${formatDateTime(dueAt)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemEntity.reminder?.let { reminder ->
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Напомнить: ${formatDateTime(reminder)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (itemEntity.priority > 0) {
                    Text(
                        text = "Приоритет: ${
                            getPriorityTitle(itemEntity.priority)
                        }",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }



            }
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить задачу",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun getPriorityTitle(priority: Int): String {
    return when (priority) {
        1 -> "низкий"
        2 -> "средний"
        3 -> "высокий"
        else -> priority.toString()
    }
}