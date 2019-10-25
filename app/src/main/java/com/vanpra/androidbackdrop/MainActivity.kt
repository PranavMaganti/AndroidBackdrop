package com.vanpra.androidbackdrop

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.graphics.Color
import android.graphics.Interpolator
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.vanpra.backdrop.BackdropButtonGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        backdrop.apply {
            setFrontView(R.layout.button_layout)
            val buttonGroup = backView.findViewById<BackdropButtonGroup>(R.id.button_group)

            buttonGroup.addButton("Home", ContextCompat.getDrawable(context, R.drawable.home))
            buttonGroup.addButton("Artist", ContextCompat.getDrawable(context, R.drawable.artist))
            buttonGroup.setMenuItemClickListener {
                toggleBackdrop()
                Log.d("STRING", it)
            }
        }
    }
}
