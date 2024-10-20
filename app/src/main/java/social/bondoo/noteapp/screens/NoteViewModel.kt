package social.bondoo.noteapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import social.bondoo.noteapp.di.IoDispatcher
import social.bondoo.noteapp.model.Note
import social.bondoo.noteapp.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    //这一步是在ViewModel中创建一个MutableStateFlow对象，用于存储Note对象的列表，使用 emptyList() 初始化
    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch(ioDispatcher) {
            repository.getAllNotes().distinctUntilChanged().collect { listOfNotes ->
                _noteList.value = listOfNotes
            }
        }
    }

    fun addNote(note: Note) = viewModelScope.launch {
        repository.addNote(note)
        getAllNotes()
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
        getAllNotes()
    }

    fun deleteAllNotes() = viewModelScope.launch {
        repository.deleteAllNotes()
        getAllNotes()
    }

    fun removeNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
        getAllNotes()
    }
}