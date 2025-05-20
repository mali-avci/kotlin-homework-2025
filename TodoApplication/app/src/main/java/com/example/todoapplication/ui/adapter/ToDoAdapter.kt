package com.example.todoapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.data.entity.ToDo
import com.example.todoapplication.databinding.ItemTodoBinding
class ToDoAdapter(
    private val onItemClick: (ToDo) -> Unit,
    private val onDeleteClick: (ToDo) -> Unit         // 👈 silme tıklayıcısı eklendi
) : ListAdapter<ToDo, ToDoAdapter.ToDoViewHolder>(DiffCallback) {

    inner class ToDoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: ToDo) {
            binding.textViewName.text = todo.name

            binding.root.setOnClickListener {
                onItemClick(todo)
            }

            // 👇 Silme butonuna tıklama
            binding.imageViewDelete.setOnClickListener {
                onDeleteClick(todo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ToDo>() {
            override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo): Boolean =
                oldItem == newItem
        }
    }
}
