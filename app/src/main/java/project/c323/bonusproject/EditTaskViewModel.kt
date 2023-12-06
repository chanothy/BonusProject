package project.c323.bonusproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EditTaskViewModel(taskId: Long, val dao: TaskDao) : ViewModel() {
    /**
     * Viewmodel for edit task
     *
     * Contains the methods for updating the tasks
     * @property taskId - id of current note
     * @property dao - current dao
     */
    val task = dao.get(taskId)
    var newDate = ""
    var newTime = ""
    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    // updates a task to the new value if found
    fun updateTask() {
        viewModelScope.launch {
            dao.update(task.value!!)
            _navigateToList.value = true
        }
    }


    fun onNavigatedToList() {
        _navigateToList.value = false
    }
}