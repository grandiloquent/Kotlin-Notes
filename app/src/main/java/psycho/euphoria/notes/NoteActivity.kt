package psycho.euphoria.notes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    private var mNote: Note = Note.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    fun initialize() {
        setContentView(R.layout.activity_note)

        val id = intent.getLongExtra(KEY_NOTE_ID, -1L)
        if (id != -1L) {
            mNote.id = id
            NoteDatabase.getInstance().queryNote(mNote)
        }
    }

    fun updateNote() {
        if (mNote.id == null && edit_text.text.isNotBlank()) {
            mNote.title = edit_text.text.trim().toString().substringBefore('\n').trim()
            mNote.content = edit_text.text.trim().toString()
            mNote.createTime = getTimeStamp()
            mNote.lastOprTime = getTimeStamp()
            mNote.trash = 0
            NoteDatabase.getInstance().insertNote(mNote)
        } else {
            mNote.title = edit_text.text.trim().toString().substringBefore('\n').trim()
            mNote.content = edit_text.text.trim().toString()
            mNote.lastOprTime = getTimeStamp()
            NoteDatabase.getInstance().updateNote(mNote)
        }
    }

    override fun onPause() {
        updateNote()
        super.onPause()
    }

    companion object {
        const val KEY_NOTE_ID = "note_id"
    }
}