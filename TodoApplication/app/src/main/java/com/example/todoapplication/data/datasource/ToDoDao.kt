package com.example.todoapplication.data.datasource

import androidx.room.*
import com.example.todoapplication.data.entity.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDo(todo: ToDo)

    @Update
    suspend fun updateToDo(todo: ToDo)

    @Delete
    suspend fun deleteToDo(todo: ToDo)

    @Query("SELECT * FROM todos ORDER BY id DESC")
    fun getAllToDos(): Flow<List<ToDo>>

    @Query("SELECT * FROM todos WHERE name LIKE '%' || :query || '%' ORDER BY id DESC")
    fun searchToDos(query: String): Flow<List<ToDo>>
}
