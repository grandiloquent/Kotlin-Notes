
package psycho.euphoria.notes

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView


class NoteViewHolder(val parent: View) : RecyclerView.ViewHolder(parent) {

    private val mTextView: TextView

    init {
        mTextView = parent.findViewById(R.id.note_content_text)
    }

    fun setContentText(str: String) {
        mTextView.text = str
    }
}
