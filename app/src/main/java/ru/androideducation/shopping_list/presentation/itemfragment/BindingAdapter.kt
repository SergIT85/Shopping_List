package ru.androideducation.shopping_list.presentation.itemfragment

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.androideducation.shopping_list.R

@BindingAdapter("errorInputName")
fun errorInputName(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.error)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("errorInputCount")
fun errorInputCount(textInputLayout: TextInputLayout, error: Boolean) {
    val message = if (error) {
        textInputLayout.context.getString(R.string.error_count)
    } else {
        null
    }
    textInputLayout.error = message
}
