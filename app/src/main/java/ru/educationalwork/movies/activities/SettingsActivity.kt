package ru.educationalwork.movies.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Switch
import ru.educationalwork.movies.CustomLanguageAdapter
import ru.educationalwork.movies.R

class SettingsActivity : BaseActivity() {

    private lateinit var localeValue: String
    private lateinit var languageSpinner: Spinner
    private lateinit var themeSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_settings)

        initSpinner()
        onSwitchClicked()
        loadPositions()
    }

    private fun initSpinner() {
        // инициализируем спиннер
        languageSpinner = findViewById(R.id.spinner)

        // зададим данные
        val spinnerData = arrayOf(
            resources.getString(R.string.lang_russian),
            resources.getString(R.string.lang_english)
        )

        val images = intArrayOf(
            R.drawable.flag_rus,
            R.drawable.flag_en
        )

        // установим адаптер
        val languageAdapter = CustomLanguageAdapter(
            applicationContext,
            images,
            spinnerData
        )
        languageSpinner.adapter = languageAdapter

        // обработаем нажатие на элемент - выбор локализации
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    RUS -> localeValue = "ru"
                    ENG -> localeValue = "en"
                }

                // получим старую позицию спиннера
                val oldSpinnerPosition =
                    getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(
                        LANG_SPINNER_POSITION, 0
                    )
                // сохраним выбранную локаль и позицию
                saveLocaleSettings(localeValue)
                // если позиция поменялась, то персоздадим активити
                if (position != oldSpinnerPosition) {
                    recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    // сохраним значение и положение спиннера
    private fun saveLocaleSettings(locale: String) {
        val editor = getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(LANG_KEY, locale)
        editor.putInt(LANG_SPINNER_POSITION, languageSpinner.selectedItemPosition)
        editor.apply()
    }

    override fun onBackPressed() {
        val intent = Intent(this@SettingsActivity, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    // поведение switch
    private fun onSwitchClicked() {
        themeSwitch = findViewById(R.id.switch_theme)
        // отследить положение
        themeSwitch.setOnCheckedChangeListener { _, onSwitch ->
            val mySpinnersEditor: Editor =
                getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).edit()
            if (onSwitch) {
                mySpinnersEditor.putInt(
                    SAVE_THEME,
                    DARK_THEME
                )
            } else {
                mySpinnersEditor.putInt(
                    SAVE_THEME,
                    LIGHT_THEME
                )
            }
            mySpinnersEditor.putBoolean(SWITCH_THEME_POSITION, onSwitch)
            mySpinnersEditor.apply()
        }

        // отследить переключение
        themeSwitch.setOnClickListener {
            recreate()
        }
    }

    // установка начальных позиций спиннера и свитча
    private fun loadPositions() {
        val switchPosition: Boolean =
            getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getBoolean(
                SWITCH_THEME_POSITION, false
            )
        themeSwitch.isChecked = switchPosition

        val langSpinnerPosition =
            getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(
                LANG_SPINNER_POSITION, 0
            )
        languageSpinner.setSelection(langSpinnerPosition)
    }

    companion object {
        const val MY_SHARED_PREF_NAME = "settings"
        const val LANG_KEY = "my_lang"
        const val LANG_SPINNER_POSITION = "spinner_position"
        const val SAVE_THEME = "save_theme"
        const val SWITCH_THEME_POSITION = "save_position"
        const val RUS = 0
        const val ENG = 1
        const val LIGHT_THEME = 100
        const val DARK_THEME = 101
    }
}