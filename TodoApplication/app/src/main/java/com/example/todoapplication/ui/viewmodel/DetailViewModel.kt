package com.example.todoapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.entity.ToDo
import com.example.todoapplication.data.repo.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    fun updateToDo(todo: ToDo) {
        viewModelScope.launch {
            repository.updateToDo(todo)
        }
    }

    fun deleteToDo(todo: ToDo) {
        viewModelScope.launch {
            repository.deleteToDo(todo)
        }
    }
}
