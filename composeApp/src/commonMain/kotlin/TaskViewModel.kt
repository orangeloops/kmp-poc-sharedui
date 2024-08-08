
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Models.Task>>(emptyList())
    val tasks: StateFlow<List<Models.Task>> = _tasks

    fun getTasks(number: String, month: String): List<Models.Task> {
        var filtered = mutableListOf<Models.Task>()
        for (task : Models.Task in Task.getTasks()){
            if (task.date.dayOfMonth.toString() == number && task.date.month.name.lowercase() == month.lowercase()){
                filtered.add(task)
            }
        }
        return filtered
    }

    init {
        loadTasks()
    }
    private fun loadTasks() {
        _tasks.value = Task.getTasks()
    }

    fun addTask(title: String, category: String, details: String, hours: String, year: Int, month: Int, day: Int) {
        Task.addTask(title, category, details, hours, year, month, day)
        loadTasks()
    }

    fun addPendingTask(title: String, category: String, details: String) {
        Task.addPendingTask(title, category, details)
        loadTasks()
    }

    fun finishPendingTask(taskId: String) {
        Task.finishPendingTask(taskId)
        loadTasks()
    }

    fun deleteTask(taskId: String) {
        Task.deleteTask(taskId)
        loadTasks()
        FormViewModel.setRefresh(true);
    }

    fun editTask(taskId: String, title: String, category: String, details: String, hours: String, year: Int, month: Int, day: Int) {
        Task.editTask(taskId, title, category, details, hours, year, month, day)
        loadTasks()
        FormViewModel.setRefresh(true);
    }
}
