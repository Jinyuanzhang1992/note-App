package social.bondoo.noteapp.screens

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import social.bondoo.noteapp.R
import social.bondoo.noteapp.components.CustomButton
import social.bondoo.noteapp.components.CustomInputField
import social.bondoo.noteapp.model.Note
import social.bondoo.noteapp.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun NoteScreen(noteViewModel: NoteViewModel = viewModel()) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = stringResource(id = R.string.app_icon),
                        modifier = Modifier
                            .padding(start = 10.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        }
    ) { innerPadding ->
        val focusManager = LocalFocusManager.current
        Surface(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp
                ).fillMaxHeight()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            MainContent(noteViewModel)
        }
    }
}

@Composable
fun MainContent(
    noteViewModel: NoteViewModel
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    val context = LocalContext.current
    val notes = noteViewModel.noteList.collectAsState().value
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomInputField(
            modifier = Modifier
                .padding(top = 9.dp, bottom = 9.dp)
                .fillMaxWidth(),
            text = title,
            label = stringResource(id = R.string.note_label_title),
            onTextChange = {
                if (it.all { char ->
                        char.isLetter() || char.isWhitespace() || char.isDigit()
                    }) {
                    title = it
                }
            }
        )
        CustomInputField(
            modifier = Modifier
                .padding(bottom = 9.dp)
                .fillMaxWidth(),
            text = description,
            label = stringResource(id = R.string.note_label_description),
            onTextChange = {
                if (it.all { char ->
                        char.isLetter() || char.isWhitespace() || char.isDigit()
                    }) {
                    description = it
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(
            text = stringResource(id = if (isEditing) R.string.note_button_update else R.string.note_button_save),
            onClick = {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    // save or update
                    if (isEditing) {
                        // 更新笔记
                        noteToEdit?.let {
                            noteViewModel.updateNote(
                                it.copy(
                                    title = title,
                                    description = description
                                )
                            )
                        }
                        isEditing = false
                        noteToEdit = null
                        Toast.makeText(context, "Note updated successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // 保存新笔记
                        noteViewModel.addNote(
                            Note(
                                title = title,
                                description = description
                            )
                        )
                        Toast.makeText(context, "Note saved successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                    title = ""
                    description = ""
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.notes_list),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(10.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(items = notes) { note ->
                NoteRow(
                    note = note,
                    onNoteClicked = {
                        noteViewModel.removeNote(note)
                        Toast.makeText(
                            context,
                            "Note removed successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onNoteLongPressed = {
                        title = it.title
                        description = it.description
                        isEditing = true
                        noteToEdit = it
                        Toast.makeText(
                            context,
                            "Long press for editing",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

@Composable
fun NoteRow(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClicked: (Note) -> Unit = {},
    onNoteLongPressed: (Note) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onNoteClicked(note) },
                    onLongPress = { onNoteLongPressed(note) }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = formatDate(note.entryDate.time),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}