package com.example.todoapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapplication.databinding.FragmentHomeBinding
import com.example.todoapplication.ui.adapter.ToDoAdapter
import com.example.todoapplication.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: ToDoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeToDos()
        setupSearch()
        setupFab()
    }

    private fun setupRecyclerView() {
        adapter = ToDoAdapter(
            onItemClick = { todo ->
                val action = HomeFragmentDirections.actionHomeToDetail(todo)
                findNavController().navigate(action)
            },
            onDeleteClick = { todo ->
                viewModel.deleteToDo(todo)
                Toast.makeText(requireContext(), "Silindi", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeToDos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.toDoList.collectLatest { todos ->
                adapter.submitList(todos)
            }
        }

        viewModel.loadToDos()
    }

    private fun setupSearch() {
        binding.editTextSearch.addTextChangedListener {
            viewModel.searchToDos(it.toString())
        }
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToAdd())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
