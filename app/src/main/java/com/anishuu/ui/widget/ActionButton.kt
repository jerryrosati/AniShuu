package com.anishuu.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import com.anishuu.R

class ActionButton(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private var iconDrawable: Drawable?
    private var titleStr: String?

    private lateinit var icon: ImageView
    private lateinit var title: TextView

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ActionButton, 0, 0).apply {
            try {
                iconDrawable = this.getDrawable(R.styleable.ActionButton_actionIcon)
                titleStr = this.getString(R.styleable.ActionButton_actionText)
            } finally {
                recycle()
            }
        }

        if (attrs != null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.widget_action_button, this)

            icon = findViewById(R.id.action_button_icon)
            title = findViewById(R.id.action_button_text)

            if (iconDrawable != null) {
                icon.setImageDrawable(iconDrawable)
            }

            if (titleStr != null) {
                title.text = titleStr
            }
        }
    }

    fun setText(newText: String) {
        title.text = newText
    }

    fun setDrawable(drawableRes: Int) {
        icon.setImageDrawable(getDrawable(context, drawableRes))
    }
}