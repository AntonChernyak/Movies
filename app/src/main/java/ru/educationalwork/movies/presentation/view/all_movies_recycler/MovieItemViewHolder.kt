package ru.educationalwork.movies.presentation.view.all_movies_recycler

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.educationalwork.movies.R
import ru.educationalwork.movies.presentation.view.fragments.SettingsFragment
import ru.educationalwork.movies.repository.model.MovieItem

class MovieItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val poster: ImageView = itemView.findViewById(R.id.itemPosterView)
    private val title: TextView = itemView.findViewById(R.id.itemTitleView)
    private val favoriteBtn : ImageView = itemView.findViewById(R.id.itemLikeImageView)

    fun bind(item: MovieItem) {
     //   title.text = itemView.resources.getString(item.title)
      //  poster.setImageResource(item.posterPath)
        title.text = item.title

        Glide.with(poster.context)
            .load(item.posterPath)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .centerCrop()
            //.override(itemView.resources.getDimensionPixelSize(R.dimen.movie_list_poster_height))
           // .thumbnail(0.5f)
           /* .diskCacheStrategy(DiskCacheStrategy.NONE)  // Чтобы одинаковые ссылки подгружались (для пагинации), но это убирает кеширование
            .skipMemoryCache(true) // Это тоже*/
            .into(poster)

        if (item.onClickStatus) title.setTextColor(Color.MAGENTA)
        else title.setTextColor(getTitleColor(title.context))

        if (item.favoriteStatus) favoriteBtn.setColorFilter(Color.RED)
        else favoriteBtn.setColorFilter(Color.GRAY)
    }

    private fun getTitleColor(context: Context): Int {
        val theme: Int =
            context.getSharedPreferences(SettingsFragment.MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(
                SettingsFragment.SAVE_THEME,
                0
            )
        return when (theme) {
            SettingsFragment.DARK_THEME -> Color.WHITE
            SettingsFragment.LIGHT_THEME -> Color.BLACK
            else -> Color.BLACK
        }
    }
}