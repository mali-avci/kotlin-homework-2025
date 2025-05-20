package com.example.todoapplication.data.repo

import com.example.todoapplication.data.datasource.ToDoDao
import com.example.todoapplication.data.entity.ToDo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {

    suspend fun insertToDo(todo: ToDo) {
        toDoDao.insertToDo(todo)
    }

    suspend fun updateToDo(todo: ToDo) {
        toDoDao.updateToDo(todo)
    }

    suspend fun deleteToDo(todo: ToDo) {
        toDoDao.deleteToDo(todo)
    }

    fun getAllToDos(): Flow<List<ToDo>> {
        return toDoDao.getAllToDos()
    }

    fun searchToDos(query: String): Flow<List<ToDo>> {
        return toDoDao.searchToDos(query)
    }
}
