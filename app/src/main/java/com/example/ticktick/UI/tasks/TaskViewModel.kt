package com.example.ticktick.UI.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ticktick.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat

class TaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow( false)

    private val tasksFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    ) { query , sortOrder , hideCompleted ->
        Triple(query, sortOrder, hideCompleted)
    }.flatMapConcat { (query, sortOrder, hideCompleted) ->
        taskDao.getTasks(query , sortOrder , hideCompleted)
    }

    val tasks = tasksFlow.asLiveData() // liveData is part of android architecture component it is conceptually very similar to flow
}

 enum class SortOrder { BY_NAME, BY_DATE }