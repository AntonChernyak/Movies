package ru.educationalwork.movies.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.*
import ru.educationalwork.movies.all_movies_recycler.MovieItem


class MainActivity : BaseActivity(), MoviesListFragment.OnDetailButtonClickListener {

    companion object {
        const val RESULT_TAG = "result_tag"
        const val OUR_REQUEST_CODE = 42
        const val CHECKBOX_STATUS = "checkbox_status"
        const val COMMENT_TEXT = "comment_text"
        const val MOVIES_LIST_FRAGMENT_TAG = "home_fragment_tag"
        const val FAVORITE_LIST_FRAGMENT_TAG = "favorite_fragment_tag"
        const val SETTINGS_FRAGMENT_TAG = "settings_fragment_tag"
        const val DETAILS_FRAGMENT_TAG = "details_fragment_tag"
        const val MOVIES_LIST_FRAGMENT_BN_POSITION = 0
        const val FAVORITE_LIST_FRAGMENT_BN_POSITION = 1
        const val SETTINGS_FRAGMENT_BN_POSITION = 2
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

        if (savedInstanceState == null) bottomNavigationView.selectedItemId = R.id.itemHomeFragment
    }

    private fun bottomNavigationViewSettings() {

        val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.itemHomeFragment -> setFragment(MOVIES_LIST_FRAGMENT_TAG)
                    R.id.itemFavoriteFragment -> setFragment(FAVORITE_LIST_FRAGMENT_TAG)
                    R.id.itemSettingsFragment -> setFragment(SETTINGS_FRAGMENT_TAG)
                }
                true
            }

        val bnView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bnView.setOnNavigationItemSelectedListener(navListener)

        // установим цвет заднего фона в зависимости от темы
        val theme: Int = getSharedPreferences(SettingsFragment.MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .getInt(SettingsFragment.SAVE_THEME, 0)

        when (theme) {
            SettingsFragment.DARK_THEME -> bottomNavigationView.setBackgroundColor(
                resources.getColor(R.color.darkBackground, null)
            )
            SettingsFragment.LIGHT_THEME -> bottomNavigationView.setBackgroundColor(
                resources.getColor(R.color.lightBackground, null)
            )
        }
    }

    // переопределим кнопку "Назад"
    override fun onBackPressed() {
        val homeFragment = supportFragmentManager.findFragmentByTag(MOVIES_LIST_FRAGMENT_TAG)
        val favoriteFragment = supportFragmentManager.findFragmentByTag(FAVORITE_LIST_FRAGMENT_TAG)
        val settingsFragment = supportFragmentManager.findFragmentByTag(SETTINGS_FRAGMENT_TAG)
        //for (f in supportFragmentManager.fragments) f.isDetached
        if (favoriteFragment != null && favoriteFragment.isVisible) favoriteFragment.onDetach()
        if (settingsFragment != null && settingsFragment.isVisible) settingsFragment.isRemoving

        Log.d("BLA", "${settingsFragment?.isVisible}")

        if (homeFragment != null && !homeFragment.isDetached) {
            Log.d("BLA", "if = ${supportFragmentManager.backStackEntryCount}")
            if (supportFragmentManager.backStackEntryCount == 0) CustomDialog(this).show()
          else super.onBackPressed()
            //  else supportFragmentManager.popBackStack()
        } else {
            Log.d("BLA", "else = ${supportFragmentManager.backStackEntryCount}")
           // if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
            if (supportFragmentManager.backStackEntryCount > 0) super.onBackPressed()
            else bottomNavigationView.selectedItemId = R.id.itemHomeFragment
        }
    }

    private fun setFragment(
        tag: String,
        title: Int = 0,
        description: Int = 0,
        poster: Int = 0,
        requestFragment: Fragment? = null
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            fragment = when (tag) {
                MOVIES_LIST_FRAGMENT_TAG -> MoviesListFragment()
                FAVORITE_LIST_FRAGMENT_TAG -> FavoriteListFragment()
                SETTINGS_FRAGMENT_TAG -> SettingsFragment()
                DETAILS_FRAGMENT_TAG -> DetailsFragment.newInstance(title, description, poster)
                else -> null
            }
            if (tag == DETAILS_FRAGMENT_TAG) fragment?.setTargetFragment(requestFragment, OUR_REQUEST_CODE)
            fragment?.let { transaction.replace(R.id.fragmentContainer, it, tag) }
            if (fragment?.tag == DETAILS_FRAGMENT_TAG) transaction.addToBackStack(null)

        } else {
            transaction.replace(R.id.fragmentContainer, fragment, tag)
        }
        transaction.commit()
    }


    override fun onDetailBtnClick(movie: MovieItem, requestFragment: Fragment?) {
        movie.isClick = true
        // requestFragment нужен, чтобы получить статус чекбокса и текст комментария из DetailFragment
        setFragment(DETAILS_FRAGMENT_TAG, movie.title, movie.description, movie.poster, requestFragment)
    }

}