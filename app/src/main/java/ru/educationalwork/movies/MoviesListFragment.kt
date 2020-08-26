package ru.educationalwork.movies

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
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
import kotlinx.android.synthetic.main.fragment_movies_list.view.*
import ru.educationalwork.movies.activities.MainActivity
import ru.educationalwork.movies.activities.MainActivity.Companion.OUR_REQUEST_CODE
import ru.educationalwork.movies.activities.MainActivity.Companion.RESULT_TAG
import ru.educationalwork.movies.activities.MainActivity.Storage.favoriteList
import ru.educationalwork.movies.activities.MainActivity.Storage.items
import ru.educationalwork.movies.all_movies_recycler.MovieItem
import ru.educationalwork.movies.all_movies_recycler.MoviesAdapter

class MoviesListFragment : Fragment() {

    companion object {
        var flag = true

    }

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
        }
        Log.d("BLA", "onViewCreated: ${items[0]}")
        initRecycler(view)
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
                movieItem.isClick = true
                recyclerView.adapter?.notifyItemChanged(position)
                val fragment = DetailsFragment.newInstance(
                    movieItem.title,
                    movieItem.description,
                    movieItem.poster
                )
                fragment.setTargetFragment(this@MoviesListFragment, OUR_REQUEST_CODE)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
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

}