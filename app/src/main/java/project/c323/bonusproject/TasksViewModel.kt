package project.c323.bonusproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.concurrent.timer
import kotlin.coroutines.resume

/**
 * Viewmodel for tasks view
 *
 * await function waits for data to exist before continuing on with a function call.
 */
suspend fun <T> LiveData<T>.await(): T {
    return withContext(Dispatchers.Main.immediate) {
        suspendCancellableCoroutine { continuation ->
            val observer = object : Observer<T> {
                override fun onChanged(value: T) {
                    removeObserver(this)
                    continuation.resume(value)
                }
            }
            observeForever(observer)
            continuation.invokeOnCancellation {
                removeObserver(observer)
            }
        }
    }
}
class TasksViewModel(val dao: TaskDao) : ViewModel() {
    /**
     * Contains methods of adding and removing tasks.
     */
    var newTaskName = ""
    var newDescription = ""
    var newTime = ""
    var newDate = ""
    val tasks = dao.getAll()
    private val _navigateToTask = MutableLiveData<Long?>()
    val navigateToTask: LiveData<Long?>
        get() = _navigateToTask
    fun addTask() {
        viewModelScope.launch {
            val task = Task()
            task.taskName = newTaskName
            task.description = newDescription
            task.time = newTime
            task.date = newDate
            dao.insert(task)
        }
    }
    fun onTaskClicked(taskId: Long) {
        _navigateToTask.value = taskId
    }
    fun onTaskNavigated() {
        _navigateToTask.value = null
    }

    fun deleteNote(noteId: Long)
    {
        viewModelScope.launch {
            val note = dao.get(noteId).await()
            dao.delete(note)
            _navigateToTask.value = null
        }
    }
}