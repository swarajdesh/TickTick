package com.example.ticktick.UI.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktick.data.Task
import com.example.ticktick.databinding.ItemTaskBinding

// Extending List Adapter because the data we are getting is in list
class TasksAdapter(private val listener: OnItemClickListener) : ListAdapter<Task, TasksAdapter.TasksViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)  //binding object
        //layout inflation means that layout xml file is turned into a actual object
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    // View Binding is a way to look up views in your layout to get a reference of layout
    inner class TasksViewHolder(private val binding : ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) { // binding.root it takes root layout

        init {
             binding.apply {
                 root.setOnClickListener {
                     val position = adapterPosition
                     if (position != RecyclerView.NO_POSITION) {
                         val task = getItem(position)
                         listener.onItemClick(task)
                     }
                 }
                 checkbox.setOnClickListener {
                     val position = adapterPosition
                     if (position != RecyclerView.NO_POSITION) {
                         val task = getItem(position)
                         listener.onCheckBoxClicked(task, checkbox.isChecked)
                     }
                 }
             }
        }

        fun bind(task: Task){
            binding.apply {
                checkbox.isChecked = task.completed
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.completed
                labelPriority.isVisible = task.important
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClicked(task:Task, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }

}