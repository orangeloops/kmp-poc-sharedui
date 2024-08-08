import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(label: String) {
    val datePickerState = rememberDatePickerState()
    val formViewModel: FormViewModel = viewModel()
    val selectedDate by formViewModel.selectedDate.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    fun formatDate(date: LocalDate): String {
        val dayOfWeek = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }.substring(0, 3)
        return "${dayOfWeek}, $month ${date.dayOfMonth}, ${date.year}"
    }

    val selectedDateMillis = datePickerState.selectedDateMillis
    val newSelectedDate = selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
    }

    LaunchedEffect(newSelectedDate) {
        newSelectedDate?.let {
            if (it != selectedDate) {
                formViewModel.setSelectedDate(it)
            }
        }
    }

    val dateText = formatDate(selectedDate)

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            content = {
                DatePicker(
                    state = datePickerState,
                )
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
            .clickable { showDialog = true }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            if (dateText != null) {
                Text(
                    text = dateText,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}

