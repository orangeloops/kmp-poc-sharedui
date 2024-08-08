
import Models.Day
import Models.WeekInfo
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class WeekViewModel: ViewModel() {
    private val _selectedWeek = MutableStateFlow(Week().getCurrentWeek())
    val selectedWeek: StateFlow<WeekInfo> = _selectedWeek
    private val _selectedDay = MutableStateFlow(Week().getTodayDate())
    val selectedDay: StateFlow<Day> = _selectedDay

    fun setSelectedDay(day: Day){
        _selectedDay.value = day
    }

    fun setCurrentWeek() {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val startOfWeek = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)
        _selectedWeek.value = Week().getWeek(startOfWeek)
    }

    fun setCurrentWeek(day: String, monthName: String) {
        val numberDay = day.toInt()
        val month = Week().getMonthFromName(monthName)
        val specificDate = LocalDate(2024, month, numberDay)
        val startOfWeek = specificDate.minus(specificDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
        _selectedWeek.value = Week().getWeek(startOfWeek)
    }

    fun setPreviousWeek(day: String, monthName: String) {
        val numberDay = day.toInt()
        val month = Week().getMonthFromName(monthName)
        val specificDate = LocalDate(2024, month, numberDay)
        val startOfPreviousWeek = specificDate.minus(specificDate.dayOfWeek.ordinal + 7, DateTimeUnit.DAY)
        _selectedWeek.value = Week().getWeek(startOfPreviousWeek)
    }

    fun setFollowingWeek(day: String, monthName: String) {
        val numberDay = day.toInt()
        val month = Week().getMonthFromName(monthName)
        val specificDate = LocalDate(2024, month, numberDay)
        val startOfFollowingWeek = specificDate.plus(7 - specificDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
        _selectedWeek.value = Week().getWeek(startOfFollowingWeek)
    }
}