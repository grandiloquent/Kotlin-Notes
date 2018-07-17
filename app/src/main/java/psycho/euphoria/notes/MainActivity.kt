package psycho.euphoria.notes

import android.Manifest
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

    private var mDrawerArrow: DrawerArrowDrawable? = null
    private val mNoteAdapter = NoteAdapter()

    private fun initialize() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mDrawerArrow = DrawerArrowDrawable(this).apply {
            color = Color.WHITE
        }
        toolbar.navigationIcon = mDrawerArrow

        nav_view.setNavigationItemSelectedListener { ite ->

            true
        }

        toolbar.setNavigationOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
        initializeRecyclerView()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
    }
    private fun initializeRecyclerView() {
        mNoteAdapter.setItems(NoteDatabase.getInstance().listNotes())
        recycler.run {
            adapter = mNoteAdapter
            this.layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_add_note -> {

            val intent = Intent(this,NoteActivity::class.java)
            startActivity(intent)
            true
        }
        else -> true

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

    override fun onDestroy() {
        super.onDestroy()
        nav_view?.setNavigationItemSelectedListener(null)
        toolbar?.setNavigationOnClickListener(null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        initialize()
    }

    companion object {
        private const val REQUEST_PERMISSIONS_CODE = 1;
    }
}