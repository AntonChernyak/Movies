package ru.educationalwork.movies.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.CustomDialog
import ru.educationalwork.movies.R
import ru.educationalwork.movies.activities.MainActivity.Storage.favoriteList
import ru.educationalwork.movies.activities.MainActivity.Storage.updateList
import ru.educationalwork.movies.all_movies_recycler.MovieItem
import ru.educationalwork.movies.all_movies_recycler.MoviesAdapter

class MainActivity : BaseActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
        const val IMAGE_INTENT_KEY = "image_intent_key"
        const val TITLE_INTENT_KEY = "title_intent_key"
        const val DESCRIPTION_INTENT_KEY = "description_intent_key"
        const val OUR_REQUEST_CODE = 42
        const val CHECKBOX_STATUS = "checkbox_status"
        const val COMMENT_TEXT = "comment_text"
    }

    object Storage {
        val favoriteList: MutableList<MovieItem> = mutableListOf()
        val updateList: MutableList<Int> = mutableListOf()
    }

    private var items = arrayListOf<MovieItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            items = arrayListOf(
                MovieItem(
                    resources.getString(R.string.lotr_title),
                    resources.getString(R.string.lotr_description),
                    R.drawable.lotr
                ),
                MovieItem(
                    resources.getString(R.string.pcs_title),
                    resources.getString(R.string.pcs_description),
                    R.drawable.pcs
                ),
                MovieItem(
                    resources.getString(R.string.eurotrip_title),
                    resources.getString(R.string.eurotrip_description),
                    R.drawable.euro_trip
                )
            )
        } else savedInstanceState.getParcelableArrayList<MovieItem>("List")?.let { items.addAll(it) }
            initRecycler()
    }

    private fun initRecycler() {

        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            } else GridLayoutManager(this, 2)

        val recyclerView = findViewById<RecyclerView>(R.id.moviesRecycler)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MoviesAdapter(items, object : MoviesAdapter.MovieItemListener {
            override fun onMoreButtonClick(movieItem: MovieItem, position: Int) {
                movieItem.isClick = true
                recyclerView.adapter?.notifyItemChanged(position)
                val intent = Intent(this@MainActivity, FilmDescriptionActivity::class.java)
                intent.putExtra(TITLE_INTENT_KEY, movieItem.title)
                intent.putExtra(DESCRIPTION_INTENT_KEY, movieItem.description)
                intent.putExtra(IMAGE_INTENT_KEY, movieItem.poster)
                startActivityForResult(
                    intent,
                    OUR_REQUEST_CODE
                )
            }

            override fun onFavoriteButtonClick(movieItem: MovieItem, position: Int) {
                if (!movieItem.isFavorite) {
                    movieItem.originalPosition = position
                    movieItem.isFavorite = true
                    favoriteList.add(movieItem)
                    recyclerView.adapter?.notifyItemChanged(position)
                } else {
                    favoriteList.remove(movieItem)
                    movieItem.isFavorite = false
                    recyclerView.adapter?.notifyItemChanged(position)
                }
            }

        })

        // Пагинация
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastVisibleItemPosition() == items.size - 1) {
                    repeat(3) {
                        items.add(
                            MovieItem(
                                resources.getString(R.string.lotr_title),
                                resources.getString(R.string.lotr_description),
                                R.drawable.lotr
                            )
                        )
                        items.add(
                            MovieItem(
                                resources.getString(R.string.pcs_title),
                                resources.getString(R.string.pcs_description),
                                R.drawable.pcs
                            )
                        )
                        items.add(
                            MovieItem(
                                resources.getString(R.string.eurotrip_title),
                                resources.getString(R.string.eurotrip_description),
                                R.drawable.euro_trip
                            )
                        )
                    }
                    recyclerView.adapter?.notifyItemRangeInserted(items.size - 3, 3)
                }
            }
        })

        // Чтобы отобразились касания на сердечко до скролла списка
        recyclerView.smoothScrollToPosition(0)

        // Разделитель
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        getDrawable(R.drawable.item_decorator_image)?.let { itemDecoration.setDrawable(it) }
        recyclerView.addItemDecoration(itemDecoration)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("List", items)
    }

    // переопределим кнопку "Назад"
    override fun onBackPressed() {
        val dialog: Dialog = CustomDialog(this)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (updateList.isNotEmpty()) {
            for (i in updateList) {
                moviesRecycler?.adapter?.notifyItemChanged(i)
            }
            updateList.clear()
        }
    }

    // Получим результат из активити с описанием фильма
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OUR_REQUEST_CODE) {
            var checkBoxStatus: Boolean? = false
            var commentText: String? = null
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    commentText = it.getStringExtra(COMMENT_TEXT)
                    checkBoxStatus = it.getBooleanExtra(CHECKBOX_STATUS, false)
                }
            }
            Log.i(TAG, "Статус чекбокса: $checkBoxStatus, текст комментария: $commentText")
        }
    }

    // обработка кнопки отправки сообщения (неявный интент)
    fun inviteFriendOnClick() {
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
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}