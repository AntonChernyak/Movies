package ru.educationalwork.movies.activities

import android.app.Activity
import android.os.Bundle
import android.widget.*
import ru.educationalwork.movies.activities.MainActivity.Companion.CHECKBOX_STATUS
import ru.educationalwork.movies.activities.MainActivity.Companion.COMMENT_TEXT
import ru.educationalwork.movies.activities.MainActivity.Companion.DESCRIPTION_INTENT_KEY
import ru.educationalwork.movies.activities.MainActivity.Companion.IMAGE_INTENT_KEY
import ru.educationalwork.movies.activities.MainActivity.Companion.TITLE_INTENT_KEY
import ru.educationalwork.movies.R

class FilmDescriptionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreateSetTheme(this)
        loadLocate()
        setContentView(R.layout.activity_film_description)

        setContent()
    }

    // переопределим кнопку "Назад"
    override fun onBackPressed() {
        // передаём текст комментария
        val commentText = findViewById<EditText>(R.id.commentEditText).text.toString()
        intent.putExtra(COMMENT_TEXT, commentText)

        // передаём статус чекбокса
        val checkBoxStatus = findViewById<CheckBox>(R.id.likeCheckBox).isChecked
        intent.putExtra(CHECKBOX_STATUS, checkBoxStatus)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }


    private fun setContent() {
        // Постер
        val poster = intent.getIntExtra(IMAGE_INTENT_KEY, 0)
        val posterImage: ImageView = findViewById(R.id.movieDescriptionImageView)
        posterImage.setImageResource(poster)

        // Название
        val title: String = intent.getStringExtra(TITLE_INTENT_KEY) ?: ""
        val textTitle: TextView = findViewById(R.id.movieTitleTextView)
        textTitle.text = title

        // Описание
        val description: String = intent.getStringExtra(DESCRIPTION_INTENT_KEY) ?: ""
        val textDescription: TextView = findViewById(R.id.movieDescriptionTextView)
        textDescription.text = description
    }



}