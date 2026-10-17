package com.example.stepup.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepup.R
import com.example.stepup.data.model.Task
import com.example.stepup.ui.theme.StepUpChipGray
import com.example.stepup.ui.theme.StepUpCompletedCard
import com.example.stepup.ui.theme.StepUpDarkRed
import com.example.stepup.ui.theme.StepUpMutedText
import com.example.stepup.ui.theme.StepUpPaleRed
import com.example.stepup.ui.theme.StepUpText

@Composable
fun TaskCard(
    task: Task,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryIcon = when (task.category) {
        "Estudio" -> Icons.Default.School
        "Personal" -> Icons.Default.Person
        else -> Icons.Default.Home
    }

    val priorityText = when (task.priority) {
        1 -> stringResource(R.string.priority_low)
        3 -> stringResource(R.string.priority_high)
        else -> stringResource(R.string.priority_medium)
    }

    val effortText = when (task.effort) {
        1 -> stringResource(R.string.effort_low)
        3 -> stringResource(R.string.effort_high)
        else -> stringResource(R.string.effort_medium)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                StepUpCompletedCard
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 9.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = {
                        onToggleCompleted()
                    },
                    modifier = Modifier.size(38.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = StepUpDarkRed,
                        uncheckedColor = Color(0xFFD5D2D2),
                        checkmarkColor = Color.White
                    )
                )

                Text(
                    text = task.title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    color = if (task.isCompleted) {
                        StepUpMutedText
                    } else {
                        StepUpText
                    },
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(
                            R.string.edit_task
                        ),
                        tint = StepUpMutedText,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(
                            R.string.delete_task
                        ),
                        tint = StepUpMutedText,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(start = 42.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskInfoPill(
                        text = task.category,
                        icon = categoryIcon
                    )

                    TaskInfoPill(
                        text = priorityText,
                        isHighlighted = task.priority == 3
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskInfoPill(text = effortText)

                    TaskInfoPill(
                        text = stringResource(
                            R.string.duration_minutes,
                            task.estimatedMinutes
                        ),
                        icon = Icons.Default.Schedule
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskInfoPill(
    text: String,
    icon: ImageVector? = null,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .background(
                color = if (isHighlighted) {
                    StepUpPaleRed
                } else {
                    StepUpChipGray
                },
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isHighlighted) {
                    StepUpDarkRed
                } else {
                    StepUpMutedText
                },
                modifier = Modifier.size(12.dp)
            )
        }

        Text(
            text = text,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            color = if (isHighlighted) {
                StepUpDarkRed
            } else {
                StepUpMutedText
            },
            fontWeight = if (isHighlighted) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            },
            maxLines = 1
        )
    }
}
