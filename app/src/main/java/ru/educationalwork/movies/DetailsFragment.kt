package ru.educationalwork.movies

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.details_fragment.*
import ru.educationalwork.movies.activities.MainActivity

class DetailsFragment : BaseFragment(){

    companion object{
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_DESCRIPTION = "extra_description"
        private const val EXTRA_POSTER = "extra_poster"

        fun newInstance(title: Int, description: Int, poster: Int): DetailsFragment{
            val moviesListFragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_TITLE, title)
            bundle.putInt(EXTRA_DESCRIPTION, description)
            bundle.putInt(EXTRA_POSTER, poster)
            moviesListFragment.arguments = bundle

            return moviesListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title: Int? = arguments?.getInt(EXTRA_TITLE, 0)
        movieTitleTextView.text = title?.let { view.resources.getString(it) }

        val description: Int? = arguments?.getInt(EXTRA_DESCRIPTION, 0)
        movieDescriptionTextView.text = description?.let { view.resources.getString(it) }

        arguments?.getInt(EXTRA_POSTER, 0)?.let { movieDescriptionImageView.setImageResource(it) }

        toolbarDescriptionFragment.title = resources.getString(R.string.movie_description)
    }

    override fun onPause() {
        val resultIntent = Intent()
        // передаём текст комментария
        val commentText = requireActivity().findViewById<EditText>(R.id.commentEditText).text.toString()
        resultIntent.putExtra(MainActivity.COMMENT_TEXT, commentText)

        // передаём статус чекбокса
        val checkBoxStatus = requireActivity().findViewById<CheckBox>(R.id.likeCheckBox).isChecked
        resultIntent.putExtra(MainActivity.CHECKBOX_STATUS, checkBoxStatus)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, resultIntent)
        super.onPause()
    }

}