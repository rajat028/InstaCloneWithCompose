package com.example.instaclonewithcompose.utils

import android.text.TextUtils
import android.util.Patterns

fun CharSequence?.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() = this.length in 6..15
