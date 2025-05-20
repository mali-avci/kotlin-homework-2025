package com.example.todoapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todos")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
) : Serializable
