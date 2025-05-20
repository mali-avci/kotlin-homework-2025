package com.example.todoapplication.data.datasource


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapplication.data.entity.ToDo

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun getToDoDao(): ToDoDao
}
