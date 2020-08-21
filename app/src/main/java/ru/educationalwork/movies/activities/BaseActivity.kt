package ru.educationalwork.movies.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import ru.educationalwork.movies.R
import java.util.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    // установим языковые настройки
    fun loadLocate() {
        // получаем значение из памяти
        val sharedPreferences =
            getSharedPreferences(SettingsActivity.MY_SHARED_PREF_NAME, Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString(SettingsActivity.LANG_KEY, "")
        if (language != null) {
            // устанавливаем локализацию
            val localeLang = Locale(language)
            val config = Configuration()
            Locale.setDefault(localeLang)
            config.setLocale(localeLang)
            config.setLayoutDirection(localeLang)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }

    // установим тему
    fun onActivityCreateSetTheme(mContext: Context) {
        val activity: Activity = mContext as Activity
        // получаем значение сохраненной темы из памяти
        val theme: Int =
            getSharedPreferences(SettingsActivity.MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(
                SettingsActivity.SAVE_THEME,
                0
            )
        // устанавливаем тему
        when (theme) {
            SettingsActivity.DARK_THEME -> activity.setTheme(
                R.style.DarkAppTheme
            )
            SettingsActivity.LIGHT_THEME -> activity.setTheme(
                R.style.AppTheme
            )
        }
    }

}