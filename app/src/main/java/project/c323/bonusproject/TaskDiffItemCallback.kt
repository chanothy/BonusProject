package project.c323.bonusproject

import androidx.recyclerview.widget.DiffUtil

/**
 * Checks if items are new and updates them
 * Checks if items are old and assigns the the same id and info
 */
class TaskDiffItemCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task)
            = (oldItem.taskId == newItem.taskId)
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = (oldItem == newItem)
}