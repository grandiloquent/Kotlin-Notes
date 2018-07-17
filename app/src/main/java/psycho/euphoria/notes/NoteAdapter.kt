package psycho.euphoria.notes

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


class NoteAdapter : RecyclerView.Adapter<NoteHolder>() {
    private var mItems = emptyList<Note>()

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemId(position: Int): Long {
        return mItems[position].id!!.toLong()
    }

    fun setItems(notes: List<Note>) {
        mItems = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = parent.inflate(R.layout.item_note)
        return NoteHolder(view)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = mItems[position]
        holder.setValue(note)
    }

}

