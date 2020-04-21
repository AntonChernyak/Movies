package ru.educationalwork.movies

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : BaseActivity() {

    companion object {
        val TAG = MainActivity::class.java.simpleName
        const val COLORS_KEY = "saved_colors_key"
        const val IMAGE_INTENT_KEY = "image_intent_key"
        const val TEXT_INTENT_KEY = "text_intent_key"
        const val OUR_REQUEST_CODE = 42
        const val CHECKBOX_STATUS = "checkbox_status"
        const val COMMENT_TEXT = "comment_text"
    }

    private lateinit var firstFilmTitle: TextView
    private lateinit var secondFilmTitle: TextView
    private lateinit var thirdFilmTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
        onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_main)

        initViews()
    }

    // переопределим кнопку "Назад"
    override fun onBackPressed() {
        val dialog: Dialog = CustomDialog(this)
        dialog.show()
    }

    // инициализация View элементов
    private fun initViews() {
        val firstFilmButton = findViewById<Button>(R.id.firstFilmButton)
        val secondFilmButton = findViewById<Button>(R.id.secondFilmButton)
        val thirdFilmButton = findViewById<Button>(R.id.thirdFilmButton)

        firstFilmTitle = findViewById(R.id.firstFilmTitle)
        secondFilmTitle = findViewById(R.id.secondFilmTitle)
        thirdFilmTitle = findViewById(R.id.thirdFilmTitle)

        detailsButtonOnClick(firstFilmButton, firstFilmTitle, R.drawable.lotr)
        detailsButtonOnClick(secondFilmButton, secondFilmTitle, R.drawable.pcs)
        detailsButtonOnClick(thirdFilmButton, thirdFilmTitle, R.drawable.euro_trip)
    }

    // Обработка нажатия на кнопку "Детали"
    private fun detailsButtonOnClick(btn: Button, text: TextView, image: Int) {
        btn.setOnClickListener {
            // меняем цвет заголовка на случайный при каждом нажатии
            val rnd = Random()
            val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            text.setTextColor(color)
            // Переход на активити с описанием
            val intent = Intent(this@MainActivity, FilmDescriptionActivity::class.java)
            intent.putExtra(TEXT_INTENT_KEY, text.text.toString())
            intent.putExtra(IMAGE_INTENT_KEY, image)
            startActivityForResult(intent, OUR_REQUEST_CODE)
        }
    }

    // Получим результат из активити с описанием фильма
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OUR_REQUEST_CODE) {
            var checkBoxStatus: Boolean? = false
            var commentText: String? = null
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    commentText = it.getStringExtra(COMMENT_TEXT)
                    checkBoxStatus = it.getBooleanExtra(CHECKBOX_STATUS, false)
                }
            }
            Log.i(TAG, "Статус чекбокса: $checkBoxStatus, текст комментария: $commentText")
        }
    }

    // обработка кнопки отправки сообщения (неявный интент)
    fun inviteFriendOnClick(view: View?) {
        val textMessage = resources.getString(R.string.look)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage)
        sendIntent.type = "text/plain"
        val title = resources.getString(R.string.chooser)
        // Создаем Intent для отображения диалога выбора.
        val chooser = Intent.createChooser(sendIntent, title)
        // Проверяем, что intent может быть успешно обработан
        sendIntent.resolveActivity(packageManager)?.let {
            startActivity(chooser)
        }
    }

    // восстанавливаем информацию
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val colors = savedInstanceState.getIntegerArrayList(COLORS_KEY)
        colors?.get(0)?.let { firstFilmTitle.setTextColor(it) }
        colors?.get(1)?.let { secondFilmTitle.setTextColor(it) }
        colors?.get(2)?.let { thirdFilmTitle.setTextColor(it) }
    }

    // сохраняем информацию
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val colors = arrayListOf(
            firstFilmTitle.currentTextColor,
            secondFilmTitle.currentTextColor,
            thirdFilmTitle.currentTextColor
        )
        outState.putIntegerArrayList(COLORS_KEY, colors)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                this.finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}