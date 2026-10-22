package com.example.focusflow.presentation.MainListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focusflow.data.Entity
import com.example.focusflow.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelListScreen @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val allTasks: StateFlow<List<Entity>> =
        repository.getAllTasks()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    fun addTask(item: Entity) {
        viewModelScope.launch {
            repository.addTasks(item)
        }
    }

    fun deleteTask(itemId: Entity) {
        viewModelScope.launch {
            repository.deleteTasks(itemId)
        }
    }

    fun updateTask(item: Entity, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedItem = item.copy(
                isCompleted = isChecked,
                completedAt = if (isChecked) {
                    System.currentTimeMillis()
                } else {
                    null
                }
            )
            repository.updateTasks(updatedItem)
        }
    }
}