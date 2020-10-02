package ru.educationalwork.movies.presentation.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import ru.educationalwork.movies.R


class CustomDialog(context: Context) : Dialog(context) {

    private val activity: Activity = context as Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog)

        // кнопка подтверждения
        val okButton = findViewById<Button>(R.id.dialog_ok_button)
        okButton.setOnClickListener {
            activity.finish()
        }

        // кнопка отмены
        val cancelButton = findViewById<Button>(R.id.dialog_cancel_button)
        cancelButton.setOnClickListener {
            cancel()
        }
    }

}