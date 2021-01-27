package ru.educationalwork.movies.presentation.view.all_movies_recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_movie.view.*
import ru.educationalwork.movies.R
import ru.educationalwork.movies.repository.model.MovieItem

class MoviesAdapter(private val listener: MovieItemListener) : RecyclerView.Adapter<MovieItemViewHolder>() {

    private val items = mutableListOf<MovieItem>()

    fun setItems(movieRespons: List<MovieItem>) {
        items.addAll(movieRespons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieItemViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.itemMoreButton.setOnClickListener {
            listener.onMoreButtonClick(item, position)
        }

        holder.itemView.itemLikeImageView.setOnClickListener {
            listener.onFavoriteButtonClick(item, position)
        }
    }

    interface MovieItemListener {
        fun onMoreButtonClick(movieItem: MovieItem, position: Int)
        fun onFavoriteButtonClick(movieItem: MovieItem, position: Int)
    }
}