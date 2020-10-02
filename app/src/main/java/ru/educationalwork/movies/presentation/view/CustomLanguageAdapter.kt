package ru.educationalwork.movies.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ru.educationalwork.movies.R

class CustomLanguageAdapter(
    applicationContext: Context?,
    private val images: IntArray,
    private val languages: Array<String>
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(applicationContext)

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }


    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.spinner_custom_layout, null)
        val icon = view.findViewById<ImageView>(R.id.imageView)
        val names = view.findViewById<TextView>(R.id.textView)
        icon.setImageResource(images[position])
        names.text = languages[position]
        return view
    }

}