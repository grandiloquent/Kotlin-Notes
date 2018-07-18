package psycho.euphoria.notes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class NoteDatabase(context: Context = App.instance) : @JvmOverloads SQLiteOpenHelper(
        context,
        File(Environment.getExternalStorageDirectory(), "notes_list.db").absolutePath,
        null,
        DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {

        db.setLocale(Locale.CHINA)
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME ( $COL_ID INTEGER, $COL_TITLE TEXT, $COL_CONTENT TEXT, `createTime` INTEGER, `lastOprTime` INTEGER, `trash` INTEGER, PRIMARY KEY(`id`) )")
    }

    fun listNotes(): ArrayList<Note> {
        val notes = ArrayList<Note>()
        val cursor = readableDatabase.rawQuery("SELECT $COL_ID,$COL_TITLE FROM $TABLE_NAME WHERE $COL_TRASH = 0 ORDER BY $COL_LASTOPRTIME DESC", null)
        try {

            while (cursor.moveToNext()) {
                val note = Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        "",
                        0L,
                        0L,
                        0
                )
                notes.add(note)

            }
            return notes

        } finally {
            cursor.close()
        }

    }

    fun queryNote(note: Note) {
        val cursor = readableDatabase.rawQuery("SELECT $COL_CONTENT FROM $TABLE_NAME WHERE $COL_ID = ?", arrayOf("${note.id}"))
        try {
            if (cursor.moveToNext())
                note.content = cursor.getString(0)
        } finally {
            cursor.close()
        }
    }

    fun insertNote(note: Note) {
        val values = ContentValues()
        values.put("title", note.title)
        values.put("content", note.content)
        values.put("createTime", note.createTime)
        values.put("lastOprTime", note.lastOprTime)
        values.put("trash", note.trash)
        note.id = writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun deleteNote(note: Note) {
        val values = ContentValues()
        values.put("$COL_TRASH", 1)
        writableDatabase.updateWithOnConflict(TABLE_NAME, values, "$COL_ID = ?", arrayOf("${note.id}"), SQLiteDatabase.CONFLICT_IGNORE)

        //  writableDatabase.delete(TABLE_NAME, "$COL_ID = ?", arrayOf("${note.id}"))
    }

    fun updateNote(note: Note) {
        val values = ContentValues()
        values.put("title", note.title)
        values.put("content", note.content)
        values.put("lastOprTime", note.lastOprTime)
        writableDatabase.updateWithOnConflict(TABLE_NAME, values, "$COL_ID = ?", arrayOf("${note.id}"), SQLiteDatabase.CONFLICT_IGNORE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {
        private const val TABLE_NAME = "notes"
        private const val DATABASE_VERSION = 1
        private const val COL_CONTENT = "content"
        private const val COL_ID = "id"
        private const val COL_TRASH = "trash"
        private const val COL_LASTOPRTIME = "lastOprTime"
        private const val COL_TITLE = "title"

        private var instance: NoteDatabase? = null
        fun getInstance() = instance ?: synchronized(this) {
            NoteDatabase().also { instance = it }
        }
    }
}

