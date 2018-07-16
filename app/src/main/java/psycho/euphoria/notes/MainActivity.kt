package psycho.euphoria.notes

import android.Manifest
import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.ActionMode
import android.view.WindowManager
import com.readystatesoftware.systembartint.SystemBarTintManager
import kotlinx.android.synthetic.main.toolbar_layout.*

class MainActivity : AppCompatActivity() {

    var prefsTheme by DelegatesExt.preference(this, "PREFS_THEME", 0)


    override fun finish() {
        super.finish()
        showActivityExitAnim()
    }
    private fun initialize() {
        initializeWindow()
        setContentView(R.layout.activity_main)
        initializeToolbar()
    }
    private fun initializeTheme() {
        changeTheme(this, Theme.mapValueToTheme(prefsTheme))
    }
    private fun initializeToolbar() {
        initToolbar(toolbar, this)
    }
    private fun initializeWindow() {
        if (isKitkatPlus()) {
            window.run {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                val tintManager = SystemBarTintManager(this@MainActivity)
                tintManager.run {
                    setStatusBarTintColor(colorPrimary)
                    isStatusBarTintEnabled = true
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        parseIntent(intent)
        showActivityInAnim()
        initializeTheme()
        super.onCreate(savedInstanceState)
        if (isMarshmallowPlus()) {
            requestPermissions(arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_PERMISSIONS_CODE);
        } else initialize()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        initialize()
    }
    private fun parseIntent(i: Intent) {
    }
    private fun showActivityExitAnim() {
        overridePendingTransition(R.anim.activity_exit_anim, R.anim.activity_up_down_anim)
    }
    private fun showActivityInAnim() {
        overridePendingTransition(R.anim.activity_down_up_anim, R.anim.activity_exit_anim)
    }

    companion object {
        private const val REQUEST_PERMISSIONS_CODE = 1;
    }
}