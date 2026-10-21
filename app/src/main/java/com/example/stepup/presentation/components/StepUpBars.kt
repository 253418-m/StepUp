package com.example.stepup.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
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
import com.example.stepup.ui.theme.StepUpDarkRed
import com.example.stepup.ui.theme.StepUpMutedText
import com.example.stepup.ui.theme.StepUpPaleRed
import com.example.stepup.ui.theme.StepUpRed

@Composable
fun StepUpHeader() {
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
                .background(
                    Color.White.copy(alpha = 0.14f),
                    RoundedCornerShape(10.dp)
                ),
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
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StepUpBottomBar(
    selectedProgress: Boolean,
    onTasksClick: () -> Unit,
    onProgressClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(72.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepUpBottomItem(
                label = stringResource(R.string.my_tasks),
                icon = Icons.Default.List,
                selected = !selectedProgress,
                onClick = onTasksClick
            )
            StepUpBottomItem(
                label = stringResource(R.string.my_progress),
                icon = Icons.Default.EmojiEvents,
                selected = selectedProgress,
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
                tint = if (selected) StepUpDarkRed else StepUpMutedText,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) StepUpDarkRed else StepUpMutedText
        )
    }
}
