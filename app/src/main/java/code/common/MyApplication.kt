package code.common

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
    }

    companion object {
        val TAG = MyApplication::class.java.simpleName

        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }
}