package ru.educationalwork.movies

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.activities.MainActivity
import ru.educationalwork.movies.activities.MainActivity.Companion.MOVIES_LIST_FRAGMENT_BN_POSITION
import ru.educationalwork.movies.activities.MainActivity.Companion.OUR_REQUEST_CODE
import ru.educationalwork.movies.activities.MainActivity.Companion.RESULT_TAG
import ru.educationalwork.movies.activities.MainActivity.Storage.favoriteList
import ru.educationalwork.movies.activities.MainActivity.Storage.items
import ru.educationalwork.movies.all_movies_recycler.MovieItem
import ru.educationalwork.movies.all_movies_recycler.MoviesAdapter

class MoviesListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (items.isEmpty()) {
            items = arrayListOf(
                MovieItem(
                    R.string.lotr_title,
                    R.string.lotr_description,
                    R.drawable.lotr
                ),
                MovieItem(
                    R.string.pcs_title,
                    R.string.pcs_description,
                    R.drawable.pcs
                ),
                MovieItem(
                    R.string.eurotrip_title,
                    R.string.eurotrip_description,
                    R.drawable.euro_trip
                )
            )
        }
        initRecycler(view)
        requireActivity().bottomNavigationView.menu.getItem(MOVIES_LIST_FRAGMENT_BN_POSITION).isChecked =
            true

    }

    private fun initRecycler(view: View) {

        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            } else GridLayoutManager(requireContext(), 2)

        val recyclerView = view.findViewById<RecyclerView>(R.id.moviesListRecycler)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MoviesAdapter(items, object : MoviesAdapter.MovieItemListener {
            override fun onMoreButtonClick(movieItem: MovieItem, position: Int) {
                (requireActivity() as? OnDetailButtonClickListener)?.onDetailBtnClick(movieItem, this@MoviesListFragment)
            }

            override fun onFavoriteButtonClick(movieItem: MovieItem, position: Int) {
                if (!movieItem.isFavorite) {
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
                                R.string.lotr_title,
                                R.string.lotr_description,
                                R.drawable.lotr
                            )
                        )
                        items.add(
                            MovieItem(
                                R.string.pcs_title,
                                R.string.pcs_description,
                                R.drawable.pcs
                            )
                        )
                        items.add(
                            MovieItem(
                                R.string.eurotrip_title,
                                R.string.eurotrip_description,
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
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        requireContext().getDrawable(R.drawable.item_decorator_image)
            ?.let { itemDecoration.setDrawable(it) }
        recyclerView.addItemDecoration(itemDecoration)
    }

    // Получим результат из фрагмента с описанием фильма
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OUR_REQUEST_CODE) {
            var checkBoxStatus: Boolean? = false
            var commentText: String? = null
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    commentText = it.getStringExtra(MainActivity.COMMENT_TEXT)
                    checkBoxStatus = it.getBooleanExtra(MainActivity.CHECKBOX_STATUS, false)
                }
            }
            Log.i(RESULT_TAG, "Статус чекбокса: $checkBoxStatus, текст комментария: $commentText")
        }
    }

    interface OnDetailButtonClickListener {
        fun onDetailBtnClick(movie: MovieItem, requestFragment: Fragment?)
    }

}