package com.example.stepup.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.stepup.presentation.state.AchievementType
import com.example.stepup.presentation.state.AchievementUiModel
import com.example.stepup.ui.theme.StepUpChipGray
import com.example.stepup.ui.theme.StepUpCompletedCard
import com.example.stepup.ui.theme.StepUpDarkRed
import com.example.stepup.ui.theme.StepUpMutedText
import com.example.stepup.ui.theme.StepUpPaleRed
import com.example.stepup.ui.theme.StepUpText

@Composable
fun AchievementCard(
    achievement: AchievementUiModel,
    modifier: Modifier = Modifier
) {
    val title = when (achievement.type) {
        AchievementType.FIRST_STEP -> stringResource(R.string.achievement_first)
        AchievementType.CONSISTENCY -> stringResource(R.string.achievement_consistency)
        AchievementType.SPECIALIST -> stringResource(R.string.achievement_specialist)
    }
    val description = when (achievement.type) {
        AchievementType.FIRST_STEP -> stringResource(R.string.achievement_first_description)
        AchievementType.CONSISTENCY -> stringResource(
            R.string.achievement_consistency_description,
            achievement.progress,
            achievement.target
        )
        AchievementType.SPECIALIST -> stringResource(
            R.string.achievement_specialist_description,
            achievement.progress,
            achievement.target
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) Color.White
            else StepUpCompletedCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier.size(44.dp).background(
                    if (achievement.isUnlocked) StepUpPaleRed else StepUpChipGray,
                    CircleShape
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (achievement.isUnlocked) Icons.Default.EmojiEvents
                    else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (achievement.isUnlocked) StepUpDarkRed else StepUpMutedText,
                    modifier = Modifier.size(23.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (achievement.isUnlocked) StepUpText else StepUpMutedText
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = StepUpMutedText,
                    maxLines = 2,
                    lineHeight = 16.sp
                )
            }

            Surface(
                color = if (achievement.isUnlocked) StepUpPaleRed else StepUpChipGray,
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (achievement.isUnlocked) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = StepUpDarkRed,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = if (achievement.isUnlocked) stringResource(R.string.achievement_unlocked)
                        else stringResource(R.string.achievement_locked),
                        color = if (achievement.isUnlocked) StepUpDarkRed else StepUpMutedText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


