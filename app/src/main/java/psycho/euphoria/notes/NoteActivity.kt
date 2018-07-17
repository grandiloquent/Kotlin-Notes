package psycho.euphoria.notes

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    private var mNote: Note = Note.newInstance()


    private fun formatAsterisk(s: String): String {
        return if (s.isBlank())
            "*"
        else
            "* ${s.trim()}\n\n"
    }

    private fun formatCode(s: String): String {
        return if (s.isBlank())
            "``"
        else if (s.contains('\n'))
            "\n```\n${s.trim()}\n```\n"
        else
            "`${s.trim()}`"
    }

    private fun formatDash(s: String): String {
        return if (s.isBlank())
            "-"
        else
            "- ${s.trim()}\n\n"
    }

    private fun formatparentheses(s: String): String {
        return if (s.isBlank())
            "()"
        else
            "(${s.trim()})"
    }

    private fun formatTitle(s: String): String {
        return if (s.isBlank())
            "## "
        else
            "## ${s.trim()}\n\n"
    }

    fun initialize() {
        setContentView(R.layout.activity_note)
        val id = intent.getLongExtra(KEY_NOTE_ID, -1L)
        if (id != -1L) {
            mNote.id = id
            NoteDatabase.getInstance().queryNote(mNote)
            edit_text.setText(mNote.content)
        }

        button_asterisk.setOnClickListener {
            insertText { v -> formatAsterisk(v) }
        }
        button_code.setOnClickListener {
            insertText { v -> formatCode(v) }
        }
        button_dash.setOnClickListener {
            insertText { v -> formatDash(v) }
        }
        button_parentheses.setOnClickListener {
            insertText { v -> formatparentheses(v) }
        }
        button_title.setOnClickListener {
            insertText { v -> formatTitle(v) }
        }
    }

    private fun insertText(transform: (String) -> String) {
        val start = edit_text.selectionStart
        val end = edit_text.selectionEnd
        val text = edit_text.text
        val selected = text.substring(start, end)
        val startString = if (start !== 0) text.substring(0, start) else "";
        val endString = if (text.length - 1 !== end) text.substring(end, text.length) else ""
        edit_text.setText(startString + transform(selected) + endString)
        edit_text.setSelection(start)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onPause() {
        updateNote()
        super.onPause()
    }

    fun updateNote() {
        if (mNote.id == null && edit_text.text.isNotBlank()) {
            mNote.title = edit_text.text.trim().toString().substringBefore('\n').trim()
            mNote.content = edit_text.text.trim().toString()
            mNote.createTime = getTimeStamp()
            mNote.lastOprTime = getTimeStamp()
            mNote.trash = 0
            NoteDatabase.getInstance().insertNote(mNote)
            setResult(Activity.RESULT_OK)
        } else if (edit_text.text.isNotBlank()) {
            mNote.title = edit_text.text.trim().toString().substringBefore('\n').trim()
            mNote.content = edit_text.text.trim().toString()
            mNote.lastOprTime = getTimeStamp()
            NoteDatabase.getInstance().updateNote(mNote)
            setResult(Activity.RESULT_OK)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
    }

    companion object {
        const val KEY_NOTE_ID = "note_id"
    }
}