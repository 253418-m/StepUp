package com.example.stepup.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepup.R
import com.example.stepup.presentation.components.AchievementCard
import com.example.stepup.presentation.components.StepUpBottomBar
import com.example.stepup.presentation.components.StepUpHeader
import com.example.stepup.presentation.state.TaskUiState
import com.example.stepup.ui.theme.StepUpBackground
import com.example.stepup.ui.theme.StepUpChipGray
import com.example.stepup.ui.theme.StepUpDarkRed
import com.example.stepup.ui.theme.StepUpMutedText
import com.example.stepup.ui.theme.StepUpPaleRed
import com.example.stepup.ui.theme.StepUpText

@Composable
fun ProgressScreen(
    uiState: TaskUiState,
    onTasksClick: () -> Unit
) {
    val unlockedCount = uiState.achievements.count { it.isUnlocked }

    Scaffold(
        containerColor = StepUpBackground,
        topBar = { StepUpHeader() },
        bottomBar = {
            StepUpBottomBar(
                selectedProgress = true,
                onTasksClick = onTasksClick,
                onProgressClick = {}
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                ProgressHero(uiState)
            }
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.progress_task_summary),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = StepUpText
                    )
                    Surface(color = StepUpChipGray, shape = RoundedCornerShape(50)) {
                        Text(
                            text = stringResource(R.string.progress_total_period),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            color = StepUpMutedText
                        )
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProgressStatCard(
                        label = stringResource(R.string.progress_total_tasks),
                        value = uiState.totalTasks.toString(),
                        icon = Icons.Default.List,
                        modifier = Modifier.weight(1f)
                    )
                    ProgressStatCard(
                        label = stringResource(R.string.filter_completed),
                        value = uiState.completedTasks.toString(),
                        icon = Icons.Default.CheckCircle,
                        emphasized = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProgressStatCard(
                        label = stringResource(R.string.filter_pending),
                        value = uiState.pendingTasks.toString(),
                        icon = Icons.Default.Schedule,
                        modifier = Modifier.weight(1f)
                    )
                    ProgressStatCard(
                        label = stringResource(R.string.progress_completion),
                        value = stringResource(
                            R.string.progress_percentage_value,
                            uiState.completionPercent
                        ),
                        icon = Icons.Default.PieChart,
                        emphasized = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.progress_achievements_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = StepUpText
                    )
                    Text(
                        text = stringResource(
                            R.string.progress_achievements_count,
                            unlockedCount,
                            uiState.achievements.size
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StepUpDarkRed
                    )
                }
            }
            items(uiState.achievements.size) { index ->
                AchievementCard(achievement = uiState.achievements[index])
            }
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(StepUpChipGray, RoundedCornerShape(13.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = StepUpDarkRed,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.progress_motivation),
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = StepUpMutedText
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressHero(uiState: TaskUiState) {
    val levelBadge = when {
        uiState.level == 1 -> stringResource(R.string.progress_badge_beginner)
        uiState.level <= 3 -> stringResource(R.string.progress_badge_constant)
        else -> stringResource(R.string.progress_badge_expert)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(StepUpPaleRed, RoundedCornerShape(13.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = StepUpDarkRed,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text(
                        text = stringResource(R.string.level_number, uiState.level),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = StepUpText
                    )
                    Text(
                        text = stringResource(R.string.progress_hero_subtitle),
                        fontSize = 12.sp,
                        color = StepUpMutedText
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(color = StepUpPaleRed, shape = RoundedCornerShape(50)) {
                    Text(
                        text = levelBadge,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        color = StepUpDarkRed,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.progress_accumulated_xp, uiState.totalXp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = StepUpDarkRed
                )
                Text(
                    text = stringResource(R.string.progress_xp_goal),
                    fontSize = 12.sp,
                    color = StepUpMutedText
                )
            }
            LinearProgressIndicator(
                progress = { uiState.xpInLevel / 100f },
                modifier = Modifier.fillMaxWidth().height(9.dp),
                color = StepUpDarkRed,
                trackColor = StepUpPaleRed
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = StepUpDarkRed,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = stringResource(
                        R.string.xp_until_next_level,
                        100 - uiState.xpInLevel,
                        uiState.level + 1
                    ),
                    modifier = Modifier.padding(start = 3.dp),
                    fontSize = 12.sp,
                    color = StepUpMutedText
                )
            }
        }
    }
}

@Composable
private fun ProgressStatCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false
) {
    Card(
        modifier = modifier.height(94.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(13.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, fontSize = 12.sp, color = StepUpMutedText)
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (emphasized) StepUpDarkRed else StepUpMutedText,
                    modifier = Modifier.size(17.dp)
                )
            }
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (emphasized) StepUpDarkRed else StepUpText
            )
        }
    }
}

