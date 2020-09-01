package ru.educationalwork.movies.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.educationalwork.movies.R
import ru.educationalwork.movies.activities.MainActivity
import ru.educationalwork.movies.all_movies_recycler.CustomItemAnimator
import ru.educationalwork.movies.all_movies_recycler.MovieItem
import ru.educationalwork.movies.all_movies_recycler.MoviesAdapter

class FavoriteListFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(view)
        requireActivity().bottomNavigationView.menu.getItem(MainActivity.FAVORITE_LIST_FRAGMENT_BN_POSITION).isChecked = true
    }

    private fun initRecycler(view: View){
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        } else GridLayoutManager(requireContext(), 2)

        val favoriteRecyclerView = view.findViewById<RecyclerView>(R.id.favoriteListRecycler)
        favoriteRecyclerView.layoutManager = layoutManager
        favoriteRecyclerView.adapter = MoviesAdapter(MainActivity.Storage.favoriteList, object : MoviesAdapter.MovieItemListener{
            override fun onMoreButtonClick(movieItem: MovieItem, position: Int) {
                (requireActivity() as? MoviesListFragment.OnDetailButtonClickListener)?.onDetailBtnClick(movieItem, this@FavoriteListFragment)
            }

            override fun onFavoriteButtonClick(movieItem: MovieItem, position: Int) {
                if (movieItem.isFavorite) {
                    movieItem.isFavorite = false
                    MainActivity.Storage.favoriteList.removeAt(position)
                    favoriteRecyclerView.adapter?.notifyItemRemoved(position)
                    favoriteRecyclerView.adapter?.notifyItemRangeChanged(position, MainActivity.Storage.favoriteList.size - position)

                    Snackbar
                        .make(view, resources.getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT)
                        .setAction(resources.getString(R.string.undo)) {
                            movieItem.isFavorite = true
                            MainActivity.Storage.favoriteList.add(position, movieItem)
                            favoriteRecyclerView.adapter?.notifyItemChanged(position)
                            favoriteRecyclerView.adapter?.notifyItemRangeChanged(position, MainActivity.Storage.favoriteList.size - position)
                        }
                        .show()
                }
            }

        })

        // Разделитель
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireActivity(),
            R.drawable.item_decorator_image
        )?.let { itemDecoration.setDrawable(it) }
        favoriteRecyclerView.addItemDecoration(itemDecoration)

        // Аниматор
        favoriteRecyclerView.itemAnimator = CustomItemAnimator()

    }

    // Получим результат из фрагмента с описанием фильма
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.OUR_REQUEST_CODE) {
            var checkBoxStatus: Boolean? = false
            var commentText: String? = null
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    commentText = it.getStringExtra(MainActivity.COMMENT_TEXT)
                    checkBoxStatus = it.getBooleanExtra(MainActivity.CHECKBOX_STATUS, false)
                }
            }
            Log.i(MainActivity.RESULT_TAG, "Статус чекбокса: $checkBoxStatus, текст комментария: $commentText")
        }
    }


}