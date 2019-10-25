package com.vanpra.backdrop

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.backdrop_button_group_layout.view.*
import androidx.core.view.setMargins
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.backdrop_button_layout.view.*


class BackdropButtonGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {

    private val buttons = mutableListOf<BackdropButton>()
    var enableOnClick = false
    var closeBackdrop: (String) -> Unit = {}

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.backdrop_button_group_layout, this, true)
    }

    fun addButton(text: String, icon: Drawable? = null, listener: (() -> Unit) = {}) {
        val button = BackdropButton(context)
        button.isSelected = buttons.isEmpty()
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val spacing = resources.getDimensionPixelSize(R.dimen.button_spacing_margin)
        val sides = resources.getDimensionPixelSize(R.dimen.button_side_margin)
        params.setMargins(sides, spacing, sides, spacing)
        button.layoutParams = params

        button.text = text
        if (icon != null) {
            button.icon = icon
        } else {
            button.hideIcon()
        }

        backdrop_layout.addView(button)
        buttons.add(button)
    }

    private fun removeSelected() {
        for (button in buttons) {
            button.isSelected = false
        }
    }

    fun setMenuItemClickListener(listener: ((String) -> Unit)) {
        for (button in buttons){
            button.setOnClickListener{
                removeSelected()
                button.isSelected = true
                listener(button.text)
                closeBackdrop(button.text)
            }
            button.icon_img.setOnClickListener {
                removeSelected()
                button.isSelected = true
                listener(button.text)
                closeBackdrop(button.text)
            }
        }
    }
}


