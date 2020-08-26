package ru.educationalwork.movies.activities

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.R
import ru.educationalwork.movies.SettingsFragment
import java.util.*

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    // установим языковые настройки
/*    fun loadLocate() {
        // получаем значение из памяти
        val sharedPreferences =
            getSharedPreferences(SettingsFragment.MY_SHARED_PREF_NAME, Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString(SettingsFragment.LANG_KEY, "ru")
        if (language != null) {
            // устанавливаем локализацию
            val localeLang = Locale(language)
            val config = Configuration()
            Locale.setDefault(localeLang)
            config.setLocale(localeLang)
            config.setLayoutDirection(localeLang)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }*/

    // установим тему
    fun onActivityCreateSetTheme(mContext: Context) {
        val activity: Activity = mContext as Activity
        // получаем значение сохраненной темы из памяти
        val theme: Int =
            getSharedPreferences(SettingsFragment.MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(
                SettingsFragment.SAVE_THEME,
                0
            )
        // устанавливаем тему
        when (theme) {
            SettingsFragment.DARK_THEME -> activity.setTheme(
                R.style.DarkAppTheme
            )
            SettingsFragment.LIGHT_THEME -> activity.setTheme(
                R.style.AppTheme
            )
        }
    }

    @Suppress("DEPRECATION")
    fun ContextWrapper.wrap(language: String): ContextWrapper {
        val config = baseContext.resources.configuration
        val sysLocale: Locale =
            this.getSystemLocale()

        if (language.isNotEmpty() && sysLocale.language != language) {
            val locale = Locale(language)
            Locale.setDefault(locale)

            this.setSystemLocale(locale)
        }
        val context = baseContext.createConfigurationContext(config)
        return ContextWrapper(context)

    }

    @TargetApi(Build.VERSION_CODES.N)
    fun ContextWrapper.getSystemLocale(): Locale {
        val config = baseContext.resources.configuration
        return config.locales[0]
    }


    @TargetApi(Build.VERSION_CODES.N)
    fun ContextWrapper.setSystemLocale(locale: Locale) {
        val config = baseContext.resources.configuration
        config.setLocale(locale)
    }

    override fun attachBaseContext(newBase: Context?) {
        // получаем значение из памяти
        val sharedPreferences: SharedPreferences? =
            newBase?.getSharedPreferences(SettingsFragment.MY_SHARED_PREF_NAME, Activity.MODE_PRIVATE)
        val language = sharedPreferences?.getString(SettingsFragment.LANG_KEY, "ru") ?: "ru"
        super.attachBaseContext(language.let { ContextWrapper(newBase).wrap(it) })
    }
}