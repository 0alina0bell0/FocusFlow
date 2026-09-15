package com.example.focusflow.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey


@Entity(tableName = "entities")
data class Entity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val reminder: Long? = null,
    // Когда задачу нужно выполнить
    val dueAt: Long?,
    val priority: Int = 0
)
