

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object FormViewModel: ViewModel() {
    private val _selectedClient = MutableStateFlow("OrangeLoops")
    val selectedClient: StateFlow<String> = _selectedClient

    private val _selectedProject = MutableStateFlow("[OL-CS] Cloud studio")
    val selectedProject: StateFlow<String> = _selectedProject

    private val _selectedTask = MutableStateFlow("Design - Non billable")
    val selectedTask: StateFlow<String> = _selectedTask

    private val _selectedDate = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _selectedDuration = MutableStateFlow(0.0)
    val selectedDuration: StateFlow<Double> = _selectedDuration

    private val _note = MutableStateFlow("")
    val note: StateFlow<String> = _note

    private var refresh: Boolean = true

    fun setSelectedClient(client: String){
        _selectedClient.value = client
        refresh = false
    }

    fun setSelectedProject(project: String){
        _selectedProject.value = project
        refresh = false
    }

    fun setSelectedTask(task: String){
        _selectedTask.value = task
        refresh = false
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setSelectedDuration(duration: Double){
        _selectedDuration.value = duration
    }

    fun setNote(note: String){
        _note.value = note
    }

    fun String.extractTextBetweenParentheses(): String? {
        val start = this.indexOf('(')
        val end = this.indexOf(')')
        return if (start != -1 && end != -1 && start < end) {
            this.substring(start + 1, end)
        } else {
            null
        }
    }

    fun String.extractTextBeforeParentheses(): String {
        val start = this.indexOf('(')
        return if (start != -1) {
            this.substring(0, start)
        } else {
            this
        }
    }

    fun String.toSeconds(): Double {
        val parts = this.split(":")
        if (parts.size != 2) return 0.0
        val hours = parts[0].toIntOrNull() ?: return 0.0
        val minutes = parts[1].toIntOrNull() ?: return 0.0
        return (hours * 3600 + minutes * 60).toDouble()
    }
    fun setTask(id: String){
        val task = Task.getTask(id)
        if (task != null && refresh){
            setSelectedClient(task.title.extractTextBetweenParentheses()!!)
            setSelectedProject(task.title.extractTextBeforeParentheses()!!)
            setSelectedTask(task.category)
            setSelectedDate(LocalDate(task.date.year, task.date.month, task.date.dayOfMonth))
            setSelectedDuration(task.hours.toSeconds())
            setNote(task.details)
        }
    }

    fun setRefresh(refresh: Boolean){
        this.refresh = refresh
    }
}