package com.example.todoapplication.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.todoapplication.data.entity.ToDo
import com.example.todoapplication.data.repo.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    private val _toDoList = MutableStateFlow<List<ToDo>>(emptyList())
    val toDoList: StateFlow<List<ToDo>> = _toDoList.asStateFlow()

    fun loadToDos() {
        repository.getAllToDos()
            .onEach { todos ->
                _toDoList.value = todos
            }
            .launchIn(viewModelScope)
    }

    fun searchToDos(query: String) {
        repository.searchToDos(query)
            .onEach { todos ->
                _toDoList.value = todos
            }
            .launchIn(viewModelScope)
    }
    fun deleteToDo(todo: ToDo) {
        viewModelScope.launch {
            repository.deleteToDo(todo)
        }
    }

}
