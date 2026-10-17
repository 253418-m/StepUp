package com.example.stepup.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepup.R
import com.example.stepup.presentation.state.TaskUiState
import com.example.stepup.ui.theme.StepUpDarkRed
import com.example.stepup.ui.theme.StepUpPaleRed
import com.example.stepup.ui.theme.StepUpMutedText

@Composable
fun ProgressSummary(
    uiState: TaskUiState,
    modifier: Modifier = Modifier
) {
    val levelTitle = when {
        uiState.level == 1 ->
            stringResource(R.string.level_beginner)
        uiState.level <= 3 ->
            stringResource(R.string.level_constant)
        else ->
            stringResource(R.string.level_expert)
    }

    val xpRemaining = 100 - uiState.xpInLevel

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = StepUpDarkRed,
                    modifier = Modifier
                        .size(25.dp)
                        .background(
                            StepUpPaleRed,
                            CircleShape
                        )
                        .padding(3.dp)
                )

                Text(
                    text = stringResource(
                        R.string.level_number,
                        uiState.level
                    ),
                    modifier = Modifier.padding(start = 5.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    modifier = Modifier.padding(start = 5.dp),
                    color = StepUpPaleRed,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = levelTitle,
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 3.dp
                        ),
                        color = StepUpDarkRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(
                        R.string.xp_in_level,
                        uiState.xpInLevel
                    ),
                    fontSize = 12.sp,
                    color = StepUpMutedText
                )
            }

            LinearProgressIndicator(
                progress = { uiState.xpInLevel / 100f },
                modifier = Modifier.fillMaxWidth(),
                color = StepUpDarkRed,
                trackColor = StepUpPaleRed
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StepUpDarkRed,
                        modifier = Modifier.size(13.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.completed_tasks_count,
                            uiState.completedTasks
                        ),
                        modifier = Modifier.padding(start = 4.dp),
                        color = StepUpMutedText,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = stringResource(
                        R.string.xp_until_next_level,
                        xpRemaining,
                        uiState.level + 1
                    ),
                    color = StepUpDarkRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

