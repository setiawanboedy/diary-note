package com.tafakkur.diaryapp.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.tafakkur.diaryapp.model.Diary
import com.tafakkur.diaryapp.presentation.components.DisplayAlertDialog
import com.tafakkur.diaryapp.util.toInstant
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    onBackPressed: () -> Unit,
    selectedDiary: Diary?,
    onDeleteClicked: () -> Unit,
    onUpdatedDateTime: (ZonedDateTime) -> Unit,
    moodName: () -> String,
) {
    val dateDialog = rememberSheetState()
    val timeDialog = rememberSheetState()
    var dateTimeUpdate by remember {
        mutableStateOf(false)
    }
    val selectedDiaryDateTime = remember(selectedDiary) {
        if (selectedDiary != null) {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date.from(selectedDiary.date.toInstant())).uppercase()
        } else {
            "Unknown"
        }
    }
    var currentDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var currentTime by remember {
        mutableStateOf(LocalTime.now())
    }
    val formattedDate = remember(currentDate) {
        DateTimeFormatter.ofPattern("dd MMM yyyy")
            .format(currentDate).uppercase()
    }
    val formattedTime = remember(currentTime) {
        DateTimeFormatter.ofPattern("hh:mm a")
            .format(currentTime).uppercase()
    }
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Arrow Icon"
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = moodName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (selectedDiary != null && dateTimeUpdate) "$formattedDate, $formattedTime" else if (selectedDiary != null) selectedDiaryDateTime
                    else "$formattedDate, $formattedTime",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    ),
                    textAlign = TextAlign.Center
                )

            }
        },
        actions = {
            if (dateTimeUpdate) {
                IconButton(onClick = {
                    currentDate = LocalDate.now()
                    currentTime = LocalTime.now()
                    dateTimeUpdate = false
                    onUpdatedDateTime(
                        ZonedDateTime.of(
                            currentDate,
                            currentTime,
                            ZoneId.systemDefault(),
                        )
                    )
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                }

            } else {
                IconButton(onClick = {
                    dateDialog.show()
                }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date Icon")
                }
            }
            if (selectedDiary != null) {
                DeleteDiaryAction(
                    onDeleteClicked = onDeleteClicked,
                    selectedDiary = selectedDiary
                )
            }
        }
    )

    CalendarDialog(state = dateDialog, selection = CalendarSelection.Date{ localDate: LocalDate ->
        currentDate = localDate
        timeDialog.show()
    },
        config = CalendarConfig(monthSelection = true, yearSelection = true)
    )

    ClockDialog(state = timeDialog, selection = ClockSelection.HoursMinutes {hours, minutes ->
        currentTime = LocalTime.of(hours, minutes)
        dateTimeUpdate = true
        onUpdatedDateTime(
            ZonedDateTime.of(currentDate, currentTime, ZoneId.systemDefault())
        )
    })
}

@Composable
fun DeleteDiaryAction(
    selectedDiary: Diary?,
    onDeleteClicked: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var openedDialog by remember {
        mutableStateOf(false)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(text = {
            Text(text = "Delete")
        }, onClick = {
            openedDialog = true
            expanded = false
        })
    }
    DisplayAlertDialog(
        dialogOpened = openedDialog,
        title = "Delete",
        message = "Are you sure want to delete '${selectedDiary?.title}'?",
        onYesClicked = onDeleteClicked,
        onCloseClicked = { openedDialog = false }
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Overflow Menu Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}