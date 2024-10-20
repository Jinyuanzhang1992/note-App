package social.bondoo.noteapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import social.bondoo.noteapp.data.NoteDataBaseDao
import social.bondoo.noteapp.model.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDataBaseDao: NoteDataBaseDao) {
    suspend fun addNote(note: Note) = noteDataBaseDao.insert(note)
    suspend fun updateNote(note: Note) = noteDataBaseDao.update(note)
    suspend fun deleteAllNotes() = noteDataBaseDao.deleteAll()
    suspend fun deleteNote(note: Note) = noteDataBaseDao.deleteNote(note)
    fun getAllNotes(): Flow<List<Note>> = noteDataBaseDao.getNotes().flowOn(Dispatchers.IO).conflate()
    fun getNoteById(id: String): Flow<Note> = noteDataBaseDao.getNoteById(id).flowOn(Dispatchers.IO).conflate()
}