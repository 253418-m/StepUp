package com.example.stepup.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stepup.R
import com.example.stepup.presentation.state.FormError
import com.example.stepup.presentation.state.TaskFormUiState
import com.example.stepup.ui.theme.StepUpBackground
import com.example.stepup.ui.theme.StepUpChipGray
import com.example.stepup.ui.theme.StepUpDarkRed
import com.example.stepup.ui.theme.StepUpMutedText
import com.example.stepup.ui.theme.StepUpPaleRed
import com.example.stepup.ui.theme.StepUpRed
import com.example.stepup.ui.theme.StepUpText

@Composable
fun TaskFormScreen(
    formState: TaskFormUiState,
    hasError: Boolean,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
    onEffortChange: (Int) -> Unit,
    onMinutesChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onClearError: () -> Unit
) {
    val editing = formState.editingTaskId != null
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(R.string.generic_error)
    val titleValid = formState.title.trim().length in 3..60

    LaunchedEffect(hasError) {
        if (hasError) {
            onClearError()
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        containerColor = StepUpBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StepUpRed)
                    .statusBarsPadding()
                    .height(64.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
                Text(
                    text = stringResource(
                        if (editing) R.string.edit_task else R.string.add_task
                    ),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StepUpBackground)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Button(
                    onClick = onSave,
                    enabled = !formState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StepUpRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = stringResource(
                            if (editing) R.string.save_changes else R.string.save_task
                        ),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Surface(
                    color = StepUpChipGray,
                    shape = RoundedCornerShape(11.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onCancel,
                        modifier = Modifier.height(44.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = StepUpText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.form_details),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = StepUpMutedText,
                            letterSpacing = 0.5.sp
                        )
                        Surface(
                            color = Color(0xFFFFEBEB),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = stringResource(R.string.form_m3),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                color = StepUpDarkRed
                            )
                        }
                    }

                    Spacer(Modifier.height(3.dp))
                    FormFieldLabel(stringResource(R.string.task_name))
                    OutlinedTextField(
                        value = formState.title,
                        onValueChange = onTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.task_name_hint)) },
                        isError = formState.titleError != null,
                        trailingIcon = {
                            if (titleValid) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StepUpDarkRed
                                )
                            }
                        },
                        shape = RoundedCornerShape(11.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StepUpRed,
                            unfocusedBorderColor = Color(0xFFECE8E8)
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (formState.titleError == null) {
                                Icons.Default.CheckCircle
                            } else {
                                Icons.Default.Warning
                            },
                            contentDescription = null,
                            tint = StepUpDarkRed,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = when (formState.titleError) {
                                FormError.REQUIRED_TITLE -> stringResource(R.string.error_required_title)
                                FormError.TITLE_TOO_SHORT -> stringResource(R.string.error_short_title)
                                FormError.TITLE_TOO_LONG -> stringResource(R.string.error_long_title)
                                else -> if (titleValid) {
                                    stringResource(R.string.valid_task_name)
                                } else {
                                    stringResource(R.string.task_name_rule)
                                }
                            },
                            modifier = Modifier.padding(start = 5.dp),
                            fontSize = 11.sp,
                            color = StepUpMutedText
                        )
                    }

                    Spacer(Modifier.height(4.dp))
                    FormFieldLabel(stringResource(R.string.category_label))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ChoiceTile(
                            text = stringResource(R.string.category_study),
                            selected = formState.category == "Estudio",
                            icon = Icons.Default.School,
                            modifier = Modifier.weight(1f),
                            onClick = { onCategoryChange("Estudio") }
                        )
                        ChoiceTile(
                            text = stringResource(R.string.category_personal),
                            selected = formState.category == "Personal",
                            icon = Icons.Default.Person,
                            modifier = Modifier.weight(1f),
                            onClick = { onCategoryChange("Personal") }
                        )
                        ChoiceTile(
                            text = stringResource(R.string.category_home),
                            selected = formState.category == "Hogar",
                            icon = Icons.Default.Home,
                            modifier = Modifier.weight(1f),
                            onClick = { onCategoryChange("Hogar") }
                        )
                    }

                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FormFieldLabel(stringResource(R.string.priority_label))
                        if (formState.priority == 3) {
                            Text(
                                text = stringResource(R.string.high_urgency),
                                fontSize = 11.sp,
                                color = StepUpDarkRed
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        ChoiceTile(
                            text = stringResource(R.string.low),
                            selected = formState.priority == 1,
                            selectedColor = StepUpPaleRed,
                            selectedTextColor = StepUpDarkRed,
                            modifier = Modifier.weight(1f),
                            onClick = { onPriorityChange(1) }
                        )
                        ChoiceTile(
                            text = stringResource(R.string.medium),
                            selected = formState.priority == 2,
                            selectedColor = StepUpPaleRed,
                            selectedTextColor = StepUpDarkRed,
                            modifier = Modifier.weight(1f),
                            onClick = { onPriorityChange(2) }
                        )
                        ChoiceTile(
                            text = stringResource(R.string.high),
                            selected = formState.priority == 3,
                            selectedColor = StepUpPaleRed,
                            selectedTextColor = StepUpDarkRed,
                            modifier = Modifier.weight(1f),
                            onClick = { onPriorityChange(3) }
                        )
                    }

                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FormFieldLabel(stringResource(R.string.effort_label))
                        Text(
                            text = stringResource(R.string.effort_xp_bonus, formState.effort * 3),
                            fontSize = 11.sp,
                            color = StepUpDarkRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        ChoiceTile(
                            text = stringResource(R.string.low),
                            subtitle = stringResource(R.string.effort_xp_bonus, 3),
                            selected = formState.effort == 1,
                            modifier = Modifier.weight(1f),
                            onClick = { onEffortChange(1) }
                        )
                        ChoiceTile(
                            text = stringResource(R.string.medium),
                            subtitle = stringResource(R.string.effort_xp_bonus, 6),
                            selected = formState.effort == 2,
                            modifier = Modifier.weight(1f),
                            onClick = { onEffortChange(2) }
                        )
                        ChoiceTile(
                            text = stringResource(R.string.high_plain),
                            subtitle = stringResource(R.string.effort_xp_bonus, 9),
                            selected = formState.effort == 3,
                            modifier = Modifier.weight(1f),
                            onClick = { onEffortChange(3) }
                        )
                    }

                    Spacer(Modifier.height(6.dp))
                    FormFieldLabel(stringResource(R.string.estimated_duration))
                    OutlinedTextField(
                        value = formState.estimatedMinutesInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                onMinutesChange(input)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.duration_hint)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = StepUpMutedText
                            )
                        },
                        suffix = { Text(stringResource(R.string.minutes_abbreviation)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = formState.durationError != null,
                        shape = RoundedCornerShape(11.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StepUpRed,
                            unfocusedBorderColor = Color(0xFFECE8E8)
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = StepUpMutedText,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (formState.durationError != null) {
                                stringResource(R.string.error_invalid_duration)
                            } else {
                                stringResource(R.string.duration_rule)
                            },
                            modifier = Modifier.padding(start = 5.dp),
                            fontSize = 11.sp,
                            color = if (formState.durationError != null) {
                                StepUpDarkRed
                            } else {
                                StepUpMutedText
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormFieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = StepUpText
    )
}

@Composable
private fun ChoiceTile(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: String? = null,
    selectedColor: Color = StepUpRed,
    selectedTextColor: Color = Color.White
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(11.dp))
            .background(if (selected) selectedColor else StepUpChipGray)
            .clickable(onClick = onClick)
            .padding(horizontal = 3.dp, vertical = if (subtitle == null) 10.dp else 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) selectedTextColor else StepUpText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.size(3.dp))
            }
            Text(
                text = text,
                fontSize = 12.sp,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) selectedTextColor else StepUpText
            )
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = if (selected) selectedTextColor else StepUpMutedText
            )
        }
    }
}


