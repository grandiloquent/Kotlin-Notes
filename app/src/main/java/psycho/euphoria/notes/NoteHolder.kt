package psycho.euphoria.notes

import android.graphics.Color
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import kotlinx.android.synthetic.main.item_note.*
import kotlinx.android.synthetic.main.item_note.view.*

class NoteHolder(private val view: View,
                 private val adapter: NoteAdapter) : BaseViewHolder(view) {

    init {
        note_menu.setOnClickListener { it.post { showPopupMenu(it) } }
        itemView.setOnClickListener {
            adapter.onItemViewClickListener?.invoke(adapterPosition)
        }
    }

    fun setValue(note: Note) {
        view.note_title.text = note.title
        view.note_menu.setVectorCompat(R.drawable.ic_more_vert_black_24dp, Color.WHITE)
    }

    private fun showPopupMenu(view: View) {
        val item = adapter.getItem(adapterPosition) ?: return

        val popup = PopupMenu(view.context, view)
        popup.menuInflater.inflate(R.menu.menu_note, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            adapter.onMenuItemClickListener?.invoke(adapterPosition, menuItem)
            true
        }
        popup.show()
    }

}
