package ru.educationalwork.movies.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.*
import ru.educationalwork.movies.all_movies_recycler.MovieItem

class MainActivity : BaseActivity() {

    companion object {
        const val RESULT_TAG = "result_tag"
        const val OUR_REQUEST_CODE = 42
        const val CHECKBOX_STATUS = "checkbox_status"
        const val COMMENT_TEXT = "comment_text"
        const val MOVIES_LIST_FRAGMENT_TAG = "home_fragment_tag"
        const val FAVORITE_LIST_FRAGMENT_TAG = "favorite_fragment_tag"
        const val SETTINGS_FRAGMENT_TAG = "settings_fragment_tag"
    }

    object Storage {
        val favoriteList: MutableList<MovieItem> = mutableListOf()
        var items = arrayListOf<MovieItem>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_main)

        bottomNavigationViewSettings()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, MoviesListFragment())
                .commit()
        }

    }

    private fun bottomNavigationViewSettings() {
        val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.itemHomeFragment -> moviesListFragmentShow()
                    R.id.itemFavoriteFragment -> favoriteListFragmentShow()
                    R.id.itemSettingsFragment -> settingsFragmentShow()
                }
                true
            }

        val bnView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bnView.setOnNavigationItemSelectedListener(navListener)

        // установим цвет заднего фона в зависимости от темы
        val theme: Int =
            getSharedPreferences(SettingsFragment.MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(
                SettingsFragment.SAVE_THEME,
                0
            )

        when (theme) {
            SettingsFragment.DARK_THEME -> bottomNavigationView.setBackgroundColor(
                resources.getColor(
                    R.color.bottomNavigationDarkBackground,
                    null
                )
            )
            SettingsFragment.LIGHT_THEME -> bottomNavigationView.setBackgroundColor(
                resources.getColor(
                    R.color.bottomNavigationLightBackground,
                    null
                )
            )
        }
    }


    private fun moviesListFragmentShow() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MoviesListFragment(), MOVIES_LIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    private fun favoriteListFragmentShow() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FavoriteListFragment(), FAVORITE_LIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    private fun settingsFragmentShow() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, SettingsFragment(), SETTINGS_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    // переопределим кнопку "Назад"
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) CustomDialog(this).show()
        else super.onBackPressed()
    }


    // обработка кнопки отправки сообщения (неявный интент)
    private fun inviteFriendOnClick() {
        val textMessage = resources.getString(R.string.look)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage)
        sendIntent.type = "text/plain"
        val title = resources.getString(R.string.chooser)
        // Создаем Intent для отображения диалога выбора.
        val chooser = Intent.createChooser(sendIntent, title)
        // Проверяем, что intent может быть успешно обработан
        sendIntent.resolveActivity(packageManager)?.let {
            startActivity(chooser)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_invite -> {
                inviteFriendOnClick()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}