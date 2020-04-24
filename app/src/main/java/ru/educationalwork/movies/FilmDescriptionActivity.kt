package ru.educationalwork.movies

import android.app.Activity
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import ru.educationalwork.movies.MainActivity.Companion.CHECKBOX_STATUS
import ru.educationalwork.movies.MainActivity.Companion.COMMENT_TEXT
import ru.educationalwork.movies.MainActivity.Companion.IMAGE_INTENT_KEY
import ru.educationalwork.movies.MainActivity.Companion.TEXT_INTENT_KEY
import java.util.*

class FilmDescriptionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreateSetTheme(this)
        loadLocate()
        setContentView(R.layout.activity_film_description)

        setPoster()
        setTextDescription()
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

    // установим изображение
    private fun setPoster() {
        val imageResources = intent.getIntExtra(IMAGE_INTENT_KEY, 0)
        val posterImage: ImageView = findViewById(R.id.filmDescriptionImageView)
        posterImage.setImageResource(imageResources)

    }

    // установим описание
    private fun setTextDescription() {
        val textResources: String = intent.getStringExtra(TEXT_INTENT_KEY) ?: ""
        val textDescription: TextView = findViewById(R.id.filmDescriptionTextView)
        textDescription.text = textResources
    }

}