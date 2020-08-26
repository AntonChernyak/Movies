package ru.educationalwork.movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.details_fragment.*
import ru.educationalwork.movies.activities.MainActivity

class DetailsFragment : Fragment(){

    companion object{
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_DESCRIPTION = "extra_description"
        private const val EXTRA_POSTER = "extra_poster"

        fun newInstance(title: String, description: String, poster: Int): DetailsFragment{
            val moviesListFragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_TITLE, title)
            bundle.putString(EXTRA_DESCRIPTION, description)
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
        movieTitleTextView.text = arguments?.getString(EXTRA_TITLE, "")
        movieDescriptionTextView.text = arguments?.getString(EXTRA_DESCRIPTION, "")
        arguments?.getInt(EXTRA_POSTER, 0)?.let { movieDescriptionImageView.setImageResource(it) }
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
        //fragmentManager?.popBackStack()
        super.onPause()
    }

}