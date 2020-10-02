package ru.educationalwork.movies.presentation.view.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.R
import ru.educationalwork.movies.presentation.view.activities.MainActivity
import ru.educationalwork.movies.presentation.view.activities.MainActivity.Companion.MOVIES_LIST_FRAGMENT_BN_POSITION
import ru.educationalwork.movies.presentation.view.activities.MainActivity.Companion.OUR_REQUEST_CODE
import ru.educationalwork.movies.presentation.view.activities.MainActivity.Companion.RESULT_TAG
import ru.educationalwork.movies.repository.model.MovieModel
import ru.educationalwork.movies.presentation.view.all_movies_recycler.MoviesAdapter
import ru.educationalwork.movies.presentation.viewmodel.MovieListViewModel
import ru.educationalwork.movies.repository.server.MovieItem

class MoviesListFragment : BaseFragment() {

    private var adapter: MoviesAdapter? = null
    private val moviesListViewModel: MovieListViewModel by lazy {
        ViewModelProvider(this@MoviesListFragment).get(MovieListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler(view)
         moviesListViewModel.getData()

            moviesListViewModel.moviesList.observe(this.viewLifecycleOwner, Observer {
                Log.d("TAGGGGG", "FragmentObserver = ${adapter?.itemCount}")
                if (adapter?.itemCount == 0) {
                    adapter?.setItems(it)
                    adapter?.notifyDataSetChanged()
                }
            })

        requireActivity().bottomNavigationView.menu.getItem(MOVIES_LIST_FRAGMENT_BN_POSITION).isChecked = true
    }

    private fun initRecycler(view: View) {

        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            } else GridLayoutManager(requireContext(), 2)

        val recyclerView = view.findViewById<RecyclerView>(R.id.moviesListRecycler)
        recyclerView.layoutManager = layoutManager
        adapter = MoviesAdapter(object : MoviesAdapter.MovieItemListener {
            override fun onMoreButtonClick(movieItem: MovieItem, position: Int) {
                (requireActivity() as? OnDetailButtonClickListener)
                    ?.onDetailBtnClick(movieItem, this@MoviesListFragment)
            }

            override fun onFavoriteButtonClick(movieItem: MovieItem, position: Int) {
/*                if (!movieItem.isFavorite) {
                    movieItem.isFavorite = true
                    favoriteList.add(movieItem)
                    recyclerView.adapter?.notifyItemChanged(position)

                    Snackbar
                        .make(view, resources.getString(R.string.added_to_favorites), Snackbar.LENGTH_SHORT)
                        .setAction(resources.getString(R.string.undo)) {
                            favoriteList.remove(movieItem)
                            movieItem.isFavorite = false
                            recyclerView.adapter?.notifyItemChanged(position)}
                        .show()
                } else {
                    favoriteList.remove(movieItem)
                    movieItem.isFavorite = false
                    recyclerView.adapter?.notifyItemChanged(position)

                    Snackbar
                        .make(view, resources.getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT)
                        .setAction(resources.getString(R.string.undo)) {
                            movieItem.isFavorite = true
                            favoriteList.add(movieItem)
                            recyclerView.adapter?.notifyItemChanged(position)
                        }
                        .show()
                }*/
            }

        })

        recyclerView.adapter = adapter

        // Пагинация
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (layoutManager.findLastVisibleItemPosition() == adapter!!.itemCount - 1) {

                    moviesListViewModel.moviesList.observe(
                        this@MoviesListFragment.viewLifecycleOwner,
                        Observer {
                            Log.d("TAGGGGG", "Пагинация = $it")
                            adapter?.setItems(it)
                        })
                    // recyclerView.adapter?.notifyItemRangeInserted(items.size - 3, 3)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        })

        // Чтобы отобразились касания на сердечко до скролла списка
        //if (adapter?.items?.size < 4) recyclerView.smoothScrollToPosition(0)

        // Разделитель
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(
            requireActivity(),
            R.drawable.item_decorator_image
        )
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