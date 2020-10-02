package ru.educationalwork.movies.presentation.view.all_movies_recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_movie.view.*
import ru.educationalwork.movies.R
import ru.educationalwork.movies.repository.model.MovieModel
import ru.educationalwork.movies.repository.server.MovieItem
import java.util.ArrayList

/*
class MoviesAdapter(private val items: List<MovieItem>, private val listener: MovieItemListener) : RecyclerView.Adapter<MovieItemViewHolder>() {
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
}*/

class MoviesAdapter(private val listener: MovieItemListener) : RecyclerView.Adapter<MovieItemViewHolder>() {

    private val items = mutableListOf<MovieItem>()

    fun setItems(movies: List<MovieModel>) {
       // items.clear()
        movies.forEach {
            items.add(
                MovieItem(
                    it.posterPath,
                    it.description,
                    it.title,
                    it.isFavorite,
                    it.isClick
                )
            )
        }
        Log.d("TAGGGGG", "Adapter = ${items.size}")
     //   notifyDataSetChanged()
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