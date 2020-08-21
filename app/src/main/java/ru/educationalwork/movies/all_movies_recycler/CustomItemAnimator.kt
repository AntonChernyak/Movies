package ru.educationalwork.movies.all_movies_recycler

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import ru.educationalwork.movies.R

class CustomItemAnimator: DefaultItemAnimator() {

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.itemView?.animation = AnimationUtils.loadAnimation(
            holder?.itemView?.context,
            R.anim.viewholder_remove_animation
        )
        return super.animateRemove(holder)
    }

    override fun getRemoveDuration(): Long {
        return 500
    }

}