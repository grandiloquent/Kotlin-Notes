package psycho.euphoria.notes

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.javia.arity.Symbols
import org.javia.arity.SyntaxException
import org.javia.arity.Util
import java.util.regex.Pattern


class NoteActivity : AppCompatActivity() {
    private var mNote: Note = Note.newInstance()
    private lateinit var mSymbols: Symbols

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

    private fun calculateExpression() {
        if (mSymbols == null) {
            mSymbols = Symbols()
        }
        val input = edit_text.getText().toString()
        val pattern = Pattern.compile("[0-9\\+\\-\\*\\.\\(\\)\\=/]+")
        val matcher = pattern.matcher(input.split("\\={5}".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0])
        val stringBuilder = StringBuilder()
        stringBuilder.append(input).append("\n\n\n=====\n\n\n")
        val results = ArrayList<Double>()
        while (matcher.find()) {
            stringBuilder.append(matcher.group()).append(" => ")
            try {
                val result = Util.doubleToString(mSymbols.eval(matcher.group()), -1)
                results.add(java.lang.Double.parseDouble(result))
                stringBuilder.append(result).append("\n\n")
            } catch (e: SyntaxException) {
                stringBuilder.append(e)
            }

        }
        var addAll = 0.0
        for (i in results) {
            addAll += i
        }
        stringBuilder.append("相加总结果：").append(addAll).append("\n\n\n")
        edit_text.setText(stringBuilder.toString())
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
        setSupportActionBar(toolbar)
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
        button_add.setOnClickListener {
            insertText { v -> "+${v.trim()}" }
        }
        button_divider.setOnClickListener {
            insertText { v -> "/${v.trim()}" }
        }
        button_square_brackets.setOnClickListener {
            insertText { v -> "[${v.trim()}]" }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, MENU_CALCULATE, 0, "")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            MENU_CALCULATE -> calculateExpression()
        }

        return true
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
        private const val MENU_CALCULATE = 1
        const val KEY_NOTE_ID = "note_id"
    }
}