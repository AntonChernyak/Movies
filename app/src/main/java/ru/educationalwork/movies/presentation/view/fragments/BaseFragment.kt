package ru.educationalwork.movies.presentation.view.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.fragment_movies_list.toolbar
import ru.educationalwork.movies.R

open class BaseFragment: Fragment() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_invite -> {
                inviteFriendOnClick()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // обработка кнопки отправки сообщения (неявный интент)
    private fun inviteFriendOnClick() {
        val textMessage = resources.getString(R.string.look)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage)
        sendIntent.type = "text/plain"
        val title = resources.getString(R.string.chooser)
        // Создаем Intent для отображения диалога выбора.
        val chooser = Intent.createChooser(sendIntent, title)
        // Проверяем, что intent может быть успешно обработан
        sendIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(chooser)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        // установим цвета в зависимости от темы
        val theme: Int = requireActivity().getSharedPreferences(SettingsFragment.MY_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            .getInt(SettingsFragment.SAVE_THEME, 0)

        when (theme) {
            SettingsFragment.DARK_THEME -> {
                toolbar?.menu?.getItem(0)?.icon?.setTint(ContextCompat.getColor(requireActivity(),
                    R.color.colorWhite
                ))
                toolbar?.setBackgroundColor(resources.getColor(R.color.darkBackground, null))
                descriptionCardView?.setCardBackgroundColor(ContextCompat.getColor(requireActivity(),
                    R.color.darkBackground
                ))
                collapsingToolbar?.setContentScrimColor(ContextCompat.getColor(requireActivity(),
                    R.color.darkBackground
                ))
                app_bar_layout?.setBackgroundColor(ContextCompat.getColor(requireActivity(),
                    R.color.darkBackground
                ))
            }
            SettingsFragment.LIGHT_THEME -> {
                toolbar?.menu?.getItem(0)?.icon?.setTint(ContextCompat.getColor(requireActivity(),
                    R.color.colorTextBody
                ))
                toolbar?.setTitleTextColor(Color.BLACK)
                toolbar?.setBackgroundColor(resources.getColor(R.color.lightBackground, null))
                toolbar?.setBackgroundColor(resources.getColor(R.color.lightBackground, null))
                app_bar_layout?.setBackgroundColor(ContextCompat.getColor(requireActivity(),
                    R.color.lightBackground
                ))
                collapsingToolbar?.setContentScrimColor(ContextCompat.getColor(requireActivity(),
                    R.color.lightBackground
                ))

                collapsingToolbar?.setCollapsedTitleTextColor(Color.BLACK)
                collapsingToolbar?.elevation = 4.0f

            }

        }

    }
}