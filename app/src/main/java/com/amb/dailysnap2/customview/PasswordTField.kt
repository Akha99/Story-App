package com.amb.dailysnap2.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.amb.dailysnap2.R

class PasswordTField: AppCompatEditText {
    private lateinit var lockIcon: Drawable
    private var doesPasswordValid: Boolean = false

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
        lockIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable
        transformationMethod = PasswordTransformationMethod.getInstance()
        setCompoundDrawablesRelativeWithIntrinsicBounds(lockIcon, null, null, null)
        addTextChangedListener { text ->
            val pass = text?.trim().toString()
            doesPasswordValid = pass.length >= 8
            error = if (doesPasswordValid) null else resources.getString(R.string.pass_length)
            if (pass.isNullOrEmpty()) {
                error = resources.getString(R.string.input_pass)
                doesPasswordValid = false
            }
        }
    }
}