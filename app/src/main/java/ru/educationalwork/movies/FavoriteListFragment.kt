package ru.educationalwork.movies

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.educationalwork.movies.activities.MainActivity
import ru.educationalwork.movies.all_movies_recycler.CustomItemAnimator
import ru.educationalwork.movies.all_movies_recycler.MovieItem
import ru.educationalwork.movies.all_movies_recycler.MoviesAdapter

class FavoriteListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler(view)
    }

    private fun initRecycler(view: View){
        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        } else GridLayoutManager(requireContext(), 2)

        val favoriteRecyclerView = view.findViewById<RecyclerView>(R.id.favoriteListRecycler)
        favoriteRecyclerView.layoutManager = layoutManager
        favoriteRecyclerView.adapter = MoviesAdapter(MainActivity.Storage.favoriteList, object : MoviesAdapter.MovieItemListener{
            override fun onMoreButtonClick(movieItem: MovieItem, position: Int) {
                movieItem.isClick = true
                favoriteRecyclerView.adapter?.notifyItemChanged(position)
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, DetailsFragment.newInstance(movieItem.title, movieItem.description, movieItem.poster))
                    .addToBackStack(null)
                    .commit()
            }

            override fun onFavoriteButtonClick(movieItem: MovieItem, position: Int) {
                if (movieItem.isFavorite) {
                    movieItem.isFavorite = false
                    MainActivity.Storage.favoriteList.removeAt(position)
                    favoriteRecyclerView.adapter?.notifyItemRemoved(position)
                    favoriteRecyclerView.adapter?.notifyItemRangeChanged(position, MainActivity.Storage.favoriteList.size - position)
                }
            }

        })

        // Разделитель
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        requireContext().getDrawable(R.drawable.item_decorator_image)?.let { itemDecoration.setDrawable(it) }
        favoriteRecyclerView.addItemDecoration(itemDecoration)

        // Аниматор
        favoriteRecyclerView.itemAnimator = CustomItemAnimator()

    }
}