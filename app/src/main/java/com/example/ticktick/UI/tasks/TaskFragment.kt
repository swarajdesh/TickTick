package com.example.ticktick.UI.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ticktick.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TaskViewModel by viewModels()



}