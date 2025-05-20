package com.example.todoapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.entity.ToDo
import com.example.todoapplication.data.repo.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    fun addToDo(name: String) {
        viewModelScope.launch {
            val newToDo = ToDo(name = name)
            repository.insertToDo(newToDo)
        }
    }
}
