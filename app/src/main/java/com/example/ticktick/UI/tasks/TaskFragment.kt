package com.example.ticktick.UI.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktick.R
import com.example.ticktick.databinding.FragmentTasksBinding
import com.example.ticktick.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
/**
Author : Swaraj Deshmukh
 **/


@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)

        val tasksAdapter = TasksAdapter()

        binding.apply {
            recyclerViewTasks.apply {
                adapter =  tasksAdapter
                layoutManager = LinearLayoutManager(requireContext())   // layout manager is responsible for how the recycle view should actually layout the items of the screen
                setHasFixedSize(true)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            tasksAdapter.submitList(it)

        }

        setHasOptionsMenu(true)
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when(item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
           R.id.action_sort_by_date_created ->{
                viewModel.sortOrder.value = SortOrder.BY_DATE
               true
           }
           R.id.action_hide_completed_tasks -> {
               item.isChecked = !item.isChecked
               viewModel.hideCompleted.value = item.isChecked
               true
           }
           R.id.action_delete_all_completed_tasks -> {

               true
           }
           else -> super.onOptionsItemSelected(item)
        }
    }

}