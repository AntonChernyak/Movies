package ru.educationalwork.movies.presentation.view.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.*
import ru.educationalwork.movies.repository.model.MovieModel
import ru.educationalwork.movies.presentation.view.CustomDialog
import ru.educationalwork.movies.presentation.view.fragments.DetailsFragment
import ru.educationalwork.movies.presentation.view.fragments.FavoriteListFragment
import ru.educationalwork.movies.presentation.view.fragments.MoviesListFragment
import ru.educationalwork.movies.presentation.view.fragments.SettingsFragment
import ru.educationalwork.movies.repository.server.MovieItem

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

        if (homeFragment != null && homeFragment.isVisible) CustomDialog(
            this
        ).show()
        else {
            if (supportFragmentManager.backStackEntryCount > 0) {
                when(bottomNavigationView.selectedItemId){
                    R.id.itemHomeFragment -> bottomNavigationView.selectedItemId = R.id.itemHomeFragment
                    R.id.itemFavoriteFragment -> bottomNavigationView.selectedItemId = R.id.itemFavoriteFragment
                }
            }
            else bottomNavigationView.selectedItemId = R.id.itemHomeFragment
        }
    }

    private fun setFragment(
        tag: String,
        title: String = "",
        description: String = "",
        poster: String = "",
        requestFragment: Fragment? = null
    ) {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()

        val transaction = supportFragmentManager.beginTransaction()
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)

        val detachTransaction = supportFragmentManager.beginTransaction()
        for (frag in supportFragmentManager.fragments) {
            frag?.let { detachTransaction.detach(it) }
        }
        detachTransaction.commit()

        if (fragment == null) {
            fragment = when (tag) {
                MOVIES_LIST_FRAGMENT_TAG -> MoviesListFragment()
                FAVORITE_LIST_FRAGMENT_TAG -> FavoriteListFragment()
                SETTINGS_FRAGMENT_TAG -> SettingsFragment()
                DETAILS_FRAGMENT_TAG -> DetailsFragment.newInstance(title, description, poster)
                else -> null
            }
            if (tag == DETAILS_FRAGMENT_TAG) fragment?.setTargetFragment(requestFragment, OUR_REQUEST_CODE)
            fragment?.let { transaction.add(R.id.fragmentContainer, it, tag) }
            if (fragment?.tag == DETAILS_FRAGMENT_TAG) transaction.addToBackStack(null)
        } else {
            transaction.attach(fragment)
        }
        transaction.commitAllowingStateLoss()
    }

    override fun onDetailBtnClick(movie: MovieItem, requestFragment: Fragment?) {
        movie.isClick = true
        // requestFragment нужен, чтобы получить статус чекбокса и текст комментария из DetailFragment
        setFragment(DETAILS_FRAGMENT_TAG, movie.title, movie.description, movie.posterPath, requestFragment)
    }

}