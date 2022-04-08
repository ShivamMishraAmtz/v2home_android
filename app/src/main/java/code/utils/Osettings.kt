package code.utils

import android.app.Activity
import android.content.Context

//Class used for shared Preference
 public open class OSettings(mActivity: Activity?) {
    companion object {
        private var mActivity: Activity? = null

        /**
         * Clear save local data in App
         *
         * @param FileName : Which file you want to clear use it
         */
        fun clearSharedPreference(FileName: String?) {
            val prefs = mActivity!!.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.clear()
            editor.commit()
        }

        /**
         * Clear save local data in App
         */
        fun clearSharedPreference() {
            val prefs =
                mActivity!!.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.clear()
            editor.commit()
        }

        /**
         * store data in  string type sharedPreferences
         *
         * @param key
         * @param value
         * @param FileName
         */
        fun putString(key: String?, value: String?, FileName: String?) {
            val prefs = mActivity!!.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.commit()
        }

        /**
         * store data in  string type sharedPreferences
         *
         * @param key
         * @param value
         */
        fun putString(key: String?, value: String?) {
            val prefs =
                mActivity!!.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.commit()
        }

        /**
         * get data in string type sharedPreferences
         *
         * @param key
         * @param FileName
         * @return
         */
        fun getString(key: String?, FileName: String?): String? {
            val prefs = mActivity!!.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            return prefs.getString(key, "")
        }

        /**
         * get data in string type sharedPreferences
         *
         * @param key
         * @return
         */
        fun getString(key: String?): String? {
            val prefs =
                mActivity!!.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE)
            return prefs.getString(key, "")
        }

        /**
         * store data in  string type sharedPreferences
         *
         * @param key
         * @param value
         * @param FileName
         */
        fun putBoolean(key: String?, value: Boolean, FileName: String?) {
            val prefs = mActivity!!.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        /**
         * store data in  string type sharedPreferences
         *
         * @param key
         * @param value
         */
        fun putBoolean(key: String?, value: Boolean) {
            val prefs =
                mActivity!!.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        /**
         * get data in string type sharedPreferences
         *
         * @param key
         * @param FileName
         * @return
         */
        fun getBoolean(key: String?, FileName: String?): Boolean {
            val prefs = mActivity!!.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            return prefs.getBoolean(key, true)
        }

        /**
         * get data in string type sharedPreferences
         *
         * @param key
         * @return
         */
        fun getBoolean(key: String?): Boolean {
            val prefs =
                mActivity!!.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE)
            return prefs.getBoolean(key, true)
        }

        /**
         * store data in  string type sharedPreferences
         *
         * @param key
         * @param value
         * @param FileName
         */
        fun putInt(key: String?, value: Int, FileName: String?) {
            val prefs = mActivity!!.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        /**
         * store data in  string type sharedPreferences
         *
         * @param key
         * @param value
         */
        fun putInt(key: String?, value: Int) {
            val prefs =
                mActivity!!.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        /**
         * get data in string type sharedPreferences
         *
         * @param key
         * @param FileName
         * @return
         */
        fun getInt(key: String?, FileName: String?): Int {
            val prefs = mActivity!!.getSharedPreferences(FileName, Context.MODE_PRIVATE)
            return prefs.getInt(key, 0)
        }

        /**
         * get data in string type sharedPreferences
         *
         * @param key
         * @return
         */
        fun getInt(key: String?): Int {
            val prefs =
                mActivity!!.getSharedPreferences(AppSettings.PREFS_MAIN_FILE, Context.MODE_PRIVATE)
            return prefs.getInt(key, 0)
        }
    }

    init {
        Companion.mActivity = mActivity
    }
}