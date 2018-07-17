package psycho.euphoria.notes

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_preview.*
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class PreviewNoteActivity() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setSupportActionBar(toolbar)

        val id = intent.getLongExtra(NoteActivity.KEY_NOTE_ID, -1L)
        if (id != -1L) {
            val note = Note.newInstance().also { it.id = id }
            NoteDatabase.getInstance().queryNote(note)
            text_view.setText(note.content)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, MENU_SAVE_AS_IMAGE, 0, resources.getString(R.string.menu_save_as_image))
        menu.add(0, MENU_SAVE_AS_HTML, 0, resources.getString(R.string.menu_save_as_html))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_SAVE_AS_IMAGE -> saveAsImage()
            MENU_SAVE_AS_HTML -> markdown2html(text_view.text.toString())
        }

        return true
    }


    private fun markdown2html(text: String) {
        val parser = Parser.builder().build()
        val document = parser.parse(text)
        val renderer = HtmlRenderer.builder().build()
        val content = renderer.render(document)
        val patternString = File(Environment.getExternalStorageDirectory(), "/Notes/dom.html").readText()
        var output = patternString.replace("###", content)
        output = encodeImage(output)
        File(Environment.getExternalStorageDirectory(), "/Notes/a.html").writeText(output)
    }

    private fun encodeImage(content: String): String {
        var content = content
        val pattern = Pattern.compile("src=\"([^\"]+jpg)\"")
        val matcher = pattern.matcher(content)
        while (matcher.find()) {
            val file = File(Environment.getExternalStorageDirectory(), "/Notes/" + matcher.group(1))
            if (file.exists()) {
                val base64 = Base64.getEncoder().encode(file.readBytes())
                content = content.replace(matcher.group(), "src=\"$base64\"")
            }
        }
        return content
    }

    fun saveAsImage() {
        val formatter = SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.ENGLISH)//
        val targetFile = File(Environment.getExternalStorageDirectory(), formatter.format(Date()) + ".jpg")
        drawText(text_view.text.toString(), 720, targetFile)
    }

    companion object {
        private const val MENU_SAVE_AS_IMAGE = 1
        private const val MENU_SAVE_AS_HTML = 2

    }

}