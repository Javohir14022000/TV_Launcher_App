package uz.javohir.tvlaunchertestapp.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {

    private const val PREF_NAME = "Launcher_Test_App"
    private const val KEY_LAST_OPENED_APP = "last_opened_app"
    private const val KEY_SELECTED_CATEGORY = "selected_category"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLastOpenedApp(context: Context, packageName: String) {
        getPreferences(context).edit().putString(KEY_LAST_OPENED_APP, packageName).apply()
    }

    fun getLastOpenedApp(context: Context): String? {
        return getPreferences(context).getString(KEY_LAST_OPENED_APP, null)
    }

    fun saveSelectedCategory(context: Context, category: String) {
        getPreferences(context).edit().putString(KEY_SELECTED_CATEGORY, category).apply()
    }

    fun getSelectedCategory(context: Context): String {
        return getPreferences(context).getString(KEY_SELECTED_CATEGORY, "Barchasi") ?: "Barchasi"
    }
}
