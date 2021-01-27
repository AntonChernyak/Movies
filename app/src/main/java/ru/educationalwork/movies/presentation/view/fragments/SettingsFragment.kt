package ru.educationalwork.movies.presentation.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import ru.educationalwork.movies.presentation.view.CustomLanguageAdapter
import ru.educationalwork.movies.R
import ru.educationalwork.movies.presentation.view.activities.MainActivity

class SettingsFragment : BaseFragment() {

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

        var LOCALE = "ru"
    }

    private var localeValue: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner(view)
        onSwitchClicked(view)
        loadPositions(view)
        requireActivity().bottomNavigationView.menu.getItem(MainActivity.SETTINGS_FRAGMENT_BN_POSITION).isChecked = true
    }

    private fun initSpinner(fragmentView: View) {
        // инициализируем спиннер
        val languageSpinner: Spinner = fragmentView.findViewById(R.id.langSpinner)

        // зададим данные
        val spinnerData = arrayOf(resources.getString(R.string.lang_russian), resources.getString(
            R.string.lang_english
        ))

        val images = intArrayOf(
            R.drawable.flag_rus,
            R.drawable.flag_en
        )

        // установим адаптер
        val languageAdapter =
            CustomLanguageAdapter(
                requireContext(),
                images,
                spinnerData
            )
        languageSpinner.adapter = languageAdapter

        // обработаем нажатие на элемент - выбор локализации
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    RUS -> localeValue = "ru"
                    ENG -> localeValue = "en"
                }

                // получим старую позицию спиннера
                val oldSpinnerPosition = requireContext()
                    .getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
                    .getInt(LANG_SPINNER_POSITION, 0)

                // сохраним выбранную локаль и позицию
                localeValue?.let { saveLocaleSettings(it, fragmentView) }
                LOCALE = localeValue.toString()

                // если позиция поменялась, то персоздадим активити
                if (position != oldSpinnerPosition) requireActivity().recreate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    // сохраним значение и положение спиннера
    private fun saveLocaleSettings(locale: String, view: View) {
        val editor = requireContext()
            .getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            .edit()

        editor.putString(LANG_KEY, locale)
        editor.putInt(LANG_SPINNER_POSITION, view.langSpinner.selectedItemPosition)
        editor.apply()
    }

    // поведение switch
    private fun onSwitchClicked(view: View) {
        // отследить положение
        view.switchTheme?.setOnCheckedChangeListener { _, onSwitch ->
            val mySpinnersEditor: SharedPreferences.Editor = requireActivity()
                .getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit()

            if (onSwitch) mySpinnersEditor.putInt(
                SAVE_THEME,
                DARK_THEME
            )
            else mySpinnersEditor.putInt(
                SAVE_THEME,
                LIGHT_THEME
            )

            mySpinnersEditor.putBoolean(SWITCH_THEME_POSITION, onSwitch)
            mySpinnersEditor.apply()
        }

        // отследить переключение
        view.switchTheme?.setOnClickListener {
            requireActivity().recreate()
        }
    }

    // установка начальных позиций спиннера и свитча
    private fun loadPositions(view: View) {
        val switchPosition: Boolean = requireActivity()
            .getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(SWITCH_THEME_POSITION, false)

        view.switchTheme.isChecked = switchPosition

        val langSpinnerPosition = requireActivity()
            .getSharedPreferences(MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            .getInt(LANG_SPINNER_POSITION, 0)

        view.langSpinner.setSelection(langSpinnerPosition)
    }

}
