/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.onboarding

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.edit
import androidx.core.content.withStyledAttributes
import org.mozilla.fenix.R
import org.mozilla.fenix.ext.settings

class OnboardingRadioButton(context: Context, attrs: AttributeSet) : AppCompatRadioButton(context, attrs) {
    private val radioGroups = mutableListOf<OnboardingRadioButton>()
    private var illustration: ImageView? = null
    private var clickListener: (() -> Unit)? = null
    var key: Int = 0

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.OnboardingRadioButton,
            0, 0
        ) {
            key = getResourceId(R.styleable.OnboardingRadioButton_onboardingKey, 0)
        }
    }

    fun addToRadioGroup(radioButton: OnboardingRadioButton) {
        radioGroups.add(radioButton)
    }

    fun addIllustration(illustration: ImageView) {
        this.illustration = illustration
    }

    fun onClickListener(listener: () -> Unit) {
        clickListener = listener
    }

    init {
        setOnClickListener {
            updateRadioValue(true)
            toggleRadioGroups()
            clickListener?.invoke()
        }
    }

    fun updateRadioValue(isChecked: Boolean) {
        this.isChecked = isChecked
        illustration?.let {
            it.isSelected = isChecked
        }
        context.settings().preferences.edit {
            putBoolean(context.getString(key), isChecked)
        }
    }

    private fun toggleRadioGroups() {
        if (isChecked) {
            radioGroups.forEach { it.updateRadioValue(false) }
        }
    }
}
