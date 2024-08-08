
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

fun formatDuration(duration: Double): String {
    val hours = duration.toInt() / 3600
    val minutes = duration.toInt() % 3600 / 60
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
}
@Composable
fun EntryForm(navController: NavHostController) {
    val viewModel: FormViewModel = viewModel { FormViewModel }
    val selectedDuration by viewModel.selectedDuration.collectAsState()
    val note by viewModel.note.collectAsState()
    val taskViewModel: TaskViewModel = viewModel { TaskViewModel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.Black else Color(0xFFFFFAF0))
            .padding(horizontal = 16.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "New time entry", fontSize = 16.sp, modifier = Modifier.weight(6f), textAlign = TextAlign.Center)
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            FormField(navController, label = "Client", route = "client_list_view")
            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            FormField(navController, label = "Project", route = "project_list_view")
            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            FormField(navController, label = "Task", route = "task_list_view")
        }

        Spacer(modifier = Modifier.height(8.dp))

        DatePickerField(label = "Date")

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            DurationField()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Leave blank to start a timer",
            color = if (isSystemInDarkTheme()) Color.White else Color.LightGray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { viewModel.setNote(it) },
            placeholder = { Text("Write a note (optional)") },
            modifier = Modifier.fillMaxWidth().background(if (isSystemInDarkTheme()) Color.Black else Color.White),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (isSystemInDarkTheme()) Color.Gray else Color.White,
                unfocusedBorderColor = if (isSystemInDarkTheme()) Color.Gray else Color.White,
                backgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.White,
                cursorColor = Color(0xFF007D00),
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                taskViewModel.addTask("${viewModel.selectedProject.value} (${viewModel.selectedClient.value})", viewModel.selectedTask.value, viewModel.note.value, formatDuration(selectedDuration), viewModel.selectedDate.value.year, viewModel.selectedDate.value.monthNumber, viewModel.selectedDate.value.dayOfMonth)
                navController.navigate(Screen.Time.route)
            },
            modifier = Modifier.fillMaxWidth().heightIn(min = 40.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF008000)),
            elevation = ButtonDefaults.elevation(0.dp)
        ) {
            if (selectedDuration == 0.0) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = if (isSystemInDarkTheme()) Color.Black else Color.White
                )
                Text("Start timer", color = if (isSystemInDarkTheme()) Color.Black else Color.White)
            } else {
                Text("Save entry", color = if (isSystemInDarkTheme()) Color.Black else Color.White)
            }
        }
    }
}
