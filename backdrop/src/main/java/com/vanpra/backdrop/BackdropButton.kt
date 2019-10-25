package com.vanpra.backdrop

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.backdrop_button_layout.view.*
import kotlinx.android.synthetic.main.backdrop_layout.view.*


class BackdropButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {

    var text: String = ""
        set(value) {
            field = value
            title_txt.text = value
        }

    var icon: Drawable? = null
        set(value) {
            field = value
            icon_img.setImageDrawable(value)
        }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.backdrop_button_layout, this, true)
    }

    private fun setBackgroundTint(view: View, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            view.background.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_OVER)
        } else {
            view.background.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    fun hideIcon() {
        icon_img.visibility = View.GONE
        val params = title_txt.layoutParams as LayoutParams
        params.marginStart = resources.getDimensionPixelSize(R.dimen.no_icon_margin)
        title_txt.layoutParams = params
    }
}