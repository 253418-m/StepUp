package com.example.stepup.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepup.R
import com.example.stepup.presentation.components.ProgressSummary
import com.example.stepup.presentation.components.TaskCard
import com.example.stepup.presentation.state.TaskStatusFilter
import com.example.stepup.presentation.state.TaskUiState
import com.example.stepup.ui.theme.StepUpBackground
import com.example.stepup.ui.theme.StepUpChipGray
import com.example.stepup.ui.theme.StepUpDarkRed
import com.example.stepup.ui.theme.StepUpMutedText
import com.example.stepup.ui.theme.StepUpPaleRed
import com.example.stepup.ui.theme.StepUpRed
import kotlinx.coroutines.launch

@Composable
fun TasksScreen(
    uiState: TaskUiState,
    onToggleTask: (String) -> Unit,
    onStatusFilterChange: (TaskStatusFilter) -> Unit,
    onCategoryFilterChange: (String?) -> Unit,
    onDeleteTask: (String) -> Unit,
    onAddTask: () -> Unit,
    onEditTask: (String) -> Unit,
    onClearError: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var taskIdToDelete by rememberSaveable { mutableStateOf<String?>(null) }

    val progressPendingMessage = stringResource(R.string.progress_screen_pending)
    val errorMessage = stringResource(R.string.generic_error)

    LaunchedEffect(uiState.hasError) {
        if (uiState.hasError) {
            snackbarHostState.showSnackbar(errorMessage)
            onClearError()
        }
    }

    Scaffold(
        containerColor = StepUpBackground,
        topBar = { StepUpHeader() },
        bottomBar = {
            StepUpBottomBar(
                onProgressClick = {
                    scope.launch { snackbarHostState.showSnackbar(progressPendingMessage) }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                containerColor = StepUpDarkRed,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task),
                    modifier = Modifier.size(29.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            item {
                ProgressSummary(uiState)
            }
            item {
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.my_tasks),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(color = StepUpChipGray, shape = RoundedCornerShape(50)) {
                        Text(
                            text = stringResource(R.string.total_tasks, uiState.totalTasks),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            color = StepUpMutedText
                        )
                    }
                }
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        StepUpFilterButton(
                            label = stringResource(R.string.filter_all),
                            icon = Icons.Default.List,
                            selected = uiState.statusFilter == TaskStatusFilter.ALL,
                            onClick = { onStatusFilterChange(TaskStatusFilter.ALL) }
                        )
                    }
                    item {
                        StepUpFilterButton(
                            label = stringResource(R.string.filter_pending),
                            icon = Icons.Default.Schedule,
                            selected = uiState.statusFilter == TaskStatusFilter.PENDING,
                            onClick = { onStatusFilterChange(TaskStatusFilter.PENDING) }
                        )
                    }
                    item {
                        StepUpFilterButton(
                            label = stringResource(R.string.filter_completed),
                            icon = Icons.Default.CheckCircle,
                            selected = uiState.statusFilter == TaskStatusFilter.COMPLETED,
                            onClick = { onStatusFilterChange(TaskStatusFilter.COMPLETED) }
                        )
                    }
                }
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    item {
                        StepUpCategoryButton(
                            label = stringResource(R.string.filter_all),
                            selected = uiState.categoryFilter == null,
                            onClick = { onCategoryFilterChange(null) }
                        )
                    }
                    item {
                        StepUpCategoryButton(
                            label = stringResource(R.string.category_study),
                            icon = Icons.Default.School,
                            selected = uiState.categoryFilter == "Estudio",
                            onClick = { onCategoryFilterChange("Estudio") }
                        )
                    }
                    item {
                        StepUpCategoryButton(
                            label = stringResource(R.string.category_personal),
                            icon = Icons.Default.Person,
                            selected = uiState.categoryFilter == "Personal",
                            onClick = { onCategoryFilterChange("Personal") }
                        )
                    }
                    item {
                        StepUpCategoryButton(
                            label = stringResource(R.string.category_home),
                            icon = Icons.Default.Home,
                            selected = uiState.categoryFilter == "Hogar",
                            onClick = { onCategoryFilterChange("Hogar") }
                        )
                    }
                }
            }

            if (uiState.visibleTasks.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_matching_tasks),
                        modifier = Modifier.padding(vertical = 32.dp),
                        color = StepUpMutedText
                    )
                }
            } else {
                items(items = uiState.visibleTasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggleCompleted = { onToggleTask(task.id) },
                        onEdit = { onEditTask(task.id) },
                        onDelete = { taskIdToDelete = task.id }
                    )
                }
            }
        }
    }

    val selectedTask = uiState.tasks.firstOrNull { it.id == taskIdToDelete }
    if (selectedTask != null) {
        AlertDialog(
            onDismissRequest = { taskIdToDelete = null },
            title = { Text(stringResource(R.string.delete_dialog_title)) },
            text = { Text(stringResource(R.string.delete_dialog_message, selectedTask.title)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteTask(selectedTask.id)
                    taskIdToDelete = null
                }) {
                    Text(stringResource(R.string.delete_confirm), color = StepUpDarkRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { taskIdToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun StepUpHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(StepUpRed)
            .statusBarsPadding()
            .height(62.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.White.copy(alpha = 0.14f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(23.dp)
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            color = Color.White
        )
        // Sin icono de perfil: asi lo especifica el diseno.
    }
}

@Composable
private fun StepUpFilterButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                if (selected) StepUpDarkRed else Color.White,
                RoundedCornerShape(9.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 11.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) Color.White else StepUpMutedText,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = label,
            color = if (selected) Color.White else StepUpMutedText,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun StepUpCategoryButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null
) {
    Row(
        modifier = Modifier
            .background(
                if (selected) StepUpPaleRed else StepUpChipGray,
                RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) StepUpDarkRed else StepUpMutedText,
                modifier = Modifier.size(13.dp)
            )
        }
        Text(
            text = label,
            color = if (selected) StepUpDarkRed else StepUpMutedText,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun StepUpBottomBar(onProgressClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepUpBottomItem(
                label = stringResource(R.string.my_tasks),
                icon = Icons.Default.List,
                selected = true,
                onClick = {}
            )
            StepUpBottomItem(
                label = stringResource(R.string.my_progress),
                icon = Icons.Default.EmojiEvents,
                selected = false,
                onClick = onProgressClick
            )
        }
    }
}

@Composable
private fun StepUpBottomItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (selected) StepUpPaleRed else Color.Transparent,
                    RoundedCornerShape(50)
                )
                .padding(horizontal = 18.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = if (selected) StepUpDarkRed else StepUpMutedText
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (selected) StepUpDarkRed else StepUpMutedText,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}


