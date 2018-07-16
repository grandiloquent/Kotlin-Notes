package psycho.euphoria.notes

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator


class NoteListAdapter(val context: Context, val notes: ArrayList<Note>) : RecyclerView.Adapter<NoteViewHolder>() {
    override fun getItemCount() = notes.size

    private val animatorInterpolator = LinearInterpolator()
    private var mLastPosition = -1
    var isFirstOnly = true
    private var animatorDuration = 300L
    private var canClickItem: HashMap<Int, Pair<(View, View, Int) -> Unit, (View, View, Int) -> Boolean>>? = null


    fun addInternalClickListener(view: View, position: Int, note: Note) {
        canClickItem?.let {
            for (key in it.keys) {
                val v = view.findViewById<View>(key)
                val f = it.get(key)
                if (v != null && f != null) {
                    v.setOnClickListener { vc -> f.first(view, vc, position) }
                    v.setOnLongClickListener { vc -> f.second(view, vc, position) }
                }
            }
        }
    }

    fun animate(holder: NoteViewHolder, position: Int) {
        if (!isFirstOnly || position > mLastPosition) {
            for (anim in getAnimators(holder.itemView)) {
                anim.apply {
                    duration = animatorDuration
                    interpolator = animatorInterpolator
                    start()
                }
            }
            mLastPosition = position
        } else clearAnimationView(holder.itemView)
    }

    fun getAnimators(view: View): Array<Animator> {
        return arrayOf(
                ObjectAnimator.ofFloat(view, "scaleX", 1.05f, 1.0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.05f, 1.0f)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        addInternalClickListener(holder.itemView, position, notes[position])
        val note = notes[position]
        holder.setContentText(note.content)
        animate(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note_layout, parent, false)
        return NoteViewHolder(view)
    }

    fun setStartPosition(start: Int) {
        mLastPosition = start
    }

    fun setViewClickListener(key: Int, listener: Pair<(View, View, Int) -> Unit, (View, View, Int) -> Boolean>) {
        if (canClickItem == null)
            canClickItem = HashMap()
        canClickItem?.put(key, listener)
    }
}

