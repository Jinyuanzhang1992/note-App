package social.bondoo.noteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import social.bondoo.noteapp.model.Note
import social.bondoo.noteapp.utils.Converter

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDataBaseDao
}