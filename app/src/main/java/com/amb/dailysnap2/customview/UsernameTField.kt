package com.amb.dailysnap2.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.amb.dailysnap2.R

class UsernameTField : AppCompatEditText {

    private var doesUsernameValid: Boolean = false
    private lateinit var personIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        personIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_24) as Drawable
        setCompoundDrawablesRelativeWithIntrinsicBounds(personIcon, null, null, null)

        addTextChangedListener(onTextChanged = {p0: CharSequence?, p1: Int, p2: Int, p3: Int ->
            val name = text?.trim()
            if (name.isNullOrEmpty()) {
                doesUsernameValid = false
                error = resources.getString(R.string.name_required)
            } else {
                doesUsernameValid = true
            }
        })
    }


}