package ru.educationalwork.movies.all_movies_recycler

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.educationalwork.movies.R
import ru.educationalwork.movies.activities.SettingsActivity

class MovieItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val poster: ImageView = itemView.findViewById(R.id.itemPosterView)
    private val title: TextView = itemView.findViewById(R.id.itemTitleView)
    private val favoriteBtn : ImageView = itemView.findViewById(R.id.itemLikeImageView)

    fun bind(item: MovieItem) {
        title.text = item.title
        poster.setImageResource(item.poster)

        if (item.isClick) title.setTextColor(Color.MAGENTA)
        else title.setTextColor(getTitleColor(title.context))

        if (item.isFavorite) favoriteBtn.setColorFilter(Color.RED)
        else favoriteBtn.setColorFilter(Color.GRAY)
    }

    private fun getTitleColor(context: Context): Int {
        val theme: Int =
            context.getSharedPreferences(SettingsActivity.MY_SHARED_PREF_NAME, Context.MODE_PRIVATE).getInt(
                SettingsActivity.SAVE_THEME,
                0
            )
        return when (theme) {
            SettingsActivity.DARK_THEME -> Color.WHITE
            SettingsActivity.LIGHT_THEME -> Color.BLACK
            else -> Color.BLACK
        }
    }
}