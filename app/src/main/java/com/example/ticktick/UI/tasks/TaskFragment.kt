package com.example.ticktick.UI.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktick.R
import com.example.ticktick.data.SortOrder
import com.example.ticktick.data.Task
import com.example.ticktick.databinding.FragmentTasksBinding
import com.example.ticktick.util.exhaustive
import com.example.ticktick.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
Author : Swaraj Deshmukh
 **/


@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_tasks), TasksAdapter.OnItemClickListener {

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)

        val tasksAdapter = TasksAdapter(this)

        binding.apply {
            recyclerViewTasks.apply {
                adapter =  tasksAdapter
                layoutManager = LinearLayoutManager(requireContext())   // layout manager is responsible for how the recycle view should actually layout the items of the screen
                setHasFixedSize(true)
            }
            
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false;
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = tasksAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTasks)

            fabAddTask.setOnClickListener{
                viewModel.onAddNewTaskClick()
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            tasksAdapter.submitList(it)

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect { event ->
                when (event) {
                    is TaskViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                    is TaskViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                       val action = TaskFragmentDirections.actionTaskFragmentToAddEditTaskFragment(null,"New Task")
                        findNavController().navigate(action)
                    }
                    is TaskViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val action = TaskFragmentDirections.actionTaskFragmentToAddEditTaskFragment(event.task,"Edit Task")
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClicked(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            //update search query
            viewModel.searchQuery.value = it

            // We type something into the search field here which use our extension function , it will change the value of
            // our SearchQuery flow to the new query string this will trigger the flatMapLatest operator execute and search in dao taskflow which we observe as LiveData
            //And our Dao simply uses the searchQuery String to filter our SQlite accordingly

            /**
             It bascically runs a new sqlite query that returns a new flow whenever we type a new letter */
         }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
           R.id.action_sort_by_date_created ->{
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
               true
           }
           R.id.action_hide_completed_tasks -> {
               item.isChecked = !item.isChecked
               viewModel.onHideCompletedClick(item.isChecked)
               true
           }
           R.id.action_delete_all_completed_tasks -> {

               true
           }
           else -> super.onOptionsItemSelected(item)
        }
    }



}