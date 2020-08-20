package ru.educationalwork.movies.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.educationalwork.movies.R
import ru.educationalwork.movies.activities.MainActivity.Storage.favoriteList
import ru.educationalwork.movies.all_movies_recycler.CustomItemAnimator
import ru.educationalwork.movies.all_movies_recycler.MovieItem
import ru.educationalwork.movies.all_movies_recycler.MoviesAdapter


class FavoriteActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreateSetTheme(this)
        loadLocate()
        setContentView(R.layout.activity_favorite)
        initRecycler()
    }

    private fun initRecycler(){
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        } else GridLayoutManager(this, 2)

        val favoriteRecyclerView = findViewById<RecyclerView>(R.id.favoriteRecycler)
        favoriteRecyclerView.layoutManager = layoutManager
        favoriteRecyclerView.adapter = MoviesAdapter(favoriteList, object : MoviesAdapter.MovieItemListener{
            override fun onMoreButtonClick(movieItem: MovieItem, position: Int) {
                movieItem.isClick = true
                favoriteRecyclerView.adapter?.notifyItemChanged(position)
                val intent = Intent(this@FavoriteActivity, FilmDescriptionActivity::class.java)
                intent.putExtra(MainActivity.TITLE_INTENT_KEY, movieItem.title)
                intent.putExtra(MainActivity.DESCRIPTION_INTENT_KEY, movieItem.description)
                intent.putExtra(MainActivity.IMAGE_INTENT_KEY, movieItem.poster)
                startActivityForResult(intent,
                    MainActivity.OUR_REQUEST_CODE
                )
            }

            override fun onFavoriteButtonClick(movieItem: MovieItem, position: Int) {
                if (movieItem.isFavorite) {
                    movieItem.isFavorite = false
                    MainActivity.Storage.updateList.add(movieItem.originalPosition)
                    favoriteList.removeAt(position)
                    favoriteRecyclerView.adapter?.notifyItemRemoved(position)
                    favoriteRecyclerView.adapter?.notifyItemRangeChanged(position, favoriteList.size - position)
                }
            }

        })

        // Разделитель
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        getDrawable(R.drawable.item_decorator_image)?.let { itemDecoration.setDrawable(it) }
        favoriteRecyclerView.addItemDecoration(itemDecoration)

        // Аниматор
        favoriteRecyclerView.itemAnimator = CustomItemAnimator()

    }
}