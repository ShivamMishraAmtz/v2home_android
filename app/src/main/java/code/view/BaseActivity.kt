package code.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import code.utils.OSettings
import com.amtz.v2home.R

open class BaseActivity : AppCompatActivity() {
    protected var mActivity: BaseActivity? = null
    private var noAnimation = true
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        mActivity = this
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
               window.decorView.windowInsetsController!!.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
                val window = this.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = this.resources.getColor(R.color.white)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        OSettings(mActivity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransitionExit()
    }

    override fun finish() {
        super.finish()
        //overridePendingTransitionExit();
    }

    fun finishNew() {
        super.finish()
        overridePendingTransitionExit()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        if (noAnimation) overridePendingTransitionEnter() else noAnimation = true
    }

    fun startActivity(intent: Intent?, noAnimation: Boolean) {
        this.noAnimation = noAnimation
        super.startActivity(intent)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        overridePendingTransitionEnter()
    }

    /**
     * Overrides the pending Activity transition method performing the "Enter" animation.
     */
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        //overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        //overridePendingTransition(R.anim.zoom_exit, R.anim.zoom_enter);
    }

    /**
     * Overrides the pending Activity transition method performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        //overridePendingTransition(R.anim.zoom_exit, R.anim.zoom_enter);
        //overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }


}