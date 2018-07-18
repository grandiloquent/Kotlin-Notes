package psycho.euphoria.notes

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var prefsQuery: String by DelegatesExt.preference(this, "prefs_query", "")

    private val mNoteAdapter = NoteAdapter()


    private fun initialize() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        mNoteAdapter.run {
            setItems(NoteDatabase.getInstance().listNotes())
            onMenuItemClickListener = fun(positoin: Int, menuItem: MenuItem) = when (menuItem.itemId) {
                R.id.action_delete -> deleteNote(positoin)
                R.id.action_update -> updateNote(positoin)
                else -> {
                }
            }
            onItemViewClickListener = fun(position: Int) {
                previewNote(position)
            }
        }
        recycler.run {
            adapter = mNoteAdapter
            this.layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }
    }

    private fun previewNote(position: Int) {
        val note = mNoteAdapter.getItem(position)
        val i = Intent(this, PreviewNoteActivity::class.java)
        i.putExtra(NoteActivity.KEY_NOTE_ID, note.id)
        startActivity(i)
    }

    private fun updateNote(position: Int) {
        var intent = Intent(this, NoteActivity::class.java)
        intent.putExtra(NoteActivity.KEY_NOTE_ID, mNoteAdapter.getItem(position).id)
        startActivityForResult(intent, REQUEST_NOTE_ACTIVITY)
    }

    private fun deleteNote(position: Int) {
        NoteDatabase.getInstance().deleteNote(mNoteAdapter.getItem(position))
        mNoteAdapter.setItems(NoteDatabase.getInstance().listNotes())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isMarshmallowPlus()) {
            requestPermissions(arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_PERMISSIONS_CODE);
        } else initialize()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_search).apply {
            val searchView = actionView as SearchView
            if (prefsQuery.isNotBlank()) {
                expandActionView()
                searchView.setQuery(prefsQuery, true)
                searchView.clearFocus()
            }
            searchView.setOnQueryTextFocusChangeListener { view, b ->
                toast(searchView.query.toString())
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar?.setNavigationOnClickListener(null)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_note -> {
            val intent = Intent(this, NoteActivity::class.java)
            startActivityForResult(intent, REQUEST_NOTE_ACTIVITY)
            true
        }
        else -> true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_NOTE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            mNoteAdapter.setItems(NoteDatabase.getInstance().listNotes())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        initialize()
    }

    companion object {
        private const val REQUEST_PERMISSIONS_CODE = 1;
        private const val REQUEST_NOTE_ACTIVITY = 1
    }
}