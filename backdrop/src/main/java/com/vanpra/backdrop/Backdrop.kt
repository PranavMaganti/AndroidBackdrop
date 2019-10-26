package com.vanpra.backdrop

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.compose.Composable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.ui.core.setContent
import androidx.ui.material.MaterialTheme
import kotlinx.android.synthetic.main.backdrop_button_layout.view.*
import kotlinx.android.synthetic.main.backdrop_layout.view.*
import kotlin.properties.Delegates


class BackdropError(message: String) : Exception(message)

@SuppressLint("RestrictedApi")
class Backdrop @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {

    var frontView by Delegates.notNull<View>()
    var backView by Delegates.notNull<View>()
    private var backViewSet = false
    var buttonGroup by Delegates.notNull<BackdropButtonGroup>()
    private var buttonGroupSet = false

    var title = ""
        set(value) {
            field = value
            front_title.text = value
        }

    private val animatorSet = AnimatorSet()
    private var backdropShown = false
    private var interpolator: TimeInterpolator? = null
    private var firstButton = true

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.backdrop_layout, this, true)


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.Backdrop, 0, 0
            )

            val text = typedArray.getText(R.styleable.Backdrop_title)
            val menuRef = typedArray.getResourceId(R.styleable.Backdrop_backdropMenu, -1)
            val backLayoutRef = typedArray.getResourceId(R.styleable.Backdrop_backLayout, -1)

            if (menuRef != -1 && !backViewSet) {
                setupBackViewMenu()
                val menu = MenuBuilder(context)
                MenuInflater(context).inflate(menuRef, menu)
                for (menuIndex in 0 until menu.size()) {
                    val menuItem = menu.getItem(menuIndex)
                    val title = menuItem.title.toString()
                    val icon = menuItem.icon
                    addBackdropButton(title, icon)
                }
            }

            if (backLayoutRef != -1 && !buttonGroupSet) {
                setBackView(backLayoutRef)
            }

            toolbar_title.text = text
            front_title.requestFocus()
            typedArray.recycle()
        }

        menu.setOnClickListener {
            toggleBackdrop()
        }

        opacity_view.setOnClickListener {
            if (backdropShown) {
                toggleBackdrop()
            }
        }

        val listener: ((String) -> Unit) = {
            if (backdropShown) {
                toggleBackdrop()
                front_title.text = it
            }
        }
        if (buttonGroupSet) buttonGroup.closeBackdrop = listener

    }

    fun setBackView(layoutId: Int) {
        val view = LayoutInflater.from(context)
            .inflate(layoutId, null)

        view.id = View.generateViewId()
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        view.layoutParams = params
        scrollview.addView(view)
        backView = view
    }

    private fun setupBackViewMenu() {
        val view = BackdropButtonGroup(this.context)
        view.id = View.generateViewId()
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        view.layoutParams = params
        scrollview.addView(view)
        buttonGroup = view
    }

     fun toggleBackdrop() {
        backdropShown = !backdropShown
        val endAlpha = if (backdropShown) {
            resetScrollView()
            menu.setImageDrawable(context.getDrawable(R.drawable.menu_to_cross))
            (menu.drawable as AnimatedVectorDrawable).start()
            opacity_view.visibility = View.VISIBLE
            0.3f

        } else {
            menu.setImageDrawable(context.getDrawable(R.drawable.cross_to_menu))
            (menu.drawable as AnimatedVectorDrawable).start()
            0f
        }

        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        var translateY = scrollview.height + 20
        val backView = back_view.height - resources.getDimensionPixelSize(R.dimen.ab_padding)
        val titleY = backView - resources.getDimensionPixelSize(R.dimen.title_margin)

        if (translateY > titleY) {
            translateY = titleY
        }

        val animatorView = ObjectAnimator.ofFloat(
            front_view,
            "translationY",
            (if (backdropShown) translateY else 0).toFloat()
        )
        val animatorOpacityView = ObjectAnimator.ofFloat(
            opacity_view,
            "translationY",
            (if (backdropShown) translateY else 0).toFloat()
        )

        animatorView.duration = 500
        animatorOpacityView.duration = 500

        if (interpolator != null) {
            animatorView.interpolator = interpolator
            animatorOpacityView.interpolator = interpolator
        }

        animatorSet.play(animatorView)
        animatorSet.play(animatorOpacityView)

        animatorView.start()
        animatorOpacityView.start()
        opacity_view.visibility = View.VISIBLE
        opacity_view.animate()
            .alpha(endAlpha)
            .setDuration(500)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    if (!backdropShown) {
                        opacity_view.visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
    }

    private fun addBackdropButton(text: String, icon: Drawable? = null) {
        buttonGroup.addButton(text, icon)
        if (firstButton) {
            front_title.text = text
            firstButton = false
        }
    }

    fun setFrontView(layoutId: Int) {
        val view = LayoutInflater.from(context)
            .inflate(layoutId, null)
        view.id = View.generateViewId()
        val set = ConstraintSet()
        set.clone(front_view)
        front_view.addView(view)

        set.connect(view.id, ConstraintSet.TOP, front_title.id, ConstraintSet.BOTTOM, 16)
        set.connect(view.id, ConstraintSet.RIGHT, front_view.id, ConstraintSet.RIGHT)
        set.connect(view.id, ConstraintSet.LEFT, front_view.id, ConstraintSet.LEFT)
        set.connect(view.id, ConstraintSet.BOTTOM, front_view.id, ConstraintSet.BOTTOM)

        set.applyTo(front_view)

        frontView = view
    }

    fun setFrontViewComposable(content: @Composable() () -> Unit) {
        (front_view as ViewGroup).setContent {
            MaterialTheme {
                content()
            }

        }
    }


    private fun resetScrollView() {
        scrollview.fullScroll(ScrollView.FOCUS_UP);

    }

}