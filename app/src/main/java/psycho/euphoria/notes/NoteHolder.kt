package psycho.euphoria.notes

import android.view.View
import kotlinx.android.synthetic.main.item_note.view.*

class NoteHolder(private val view: View) : BaseViewHolder(view) {


    fun setValue(note: Note) {
        view.note_title.text = note.title
    }

}
