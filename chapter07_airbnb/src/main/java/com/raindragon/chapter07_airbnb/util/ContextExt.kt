package com.raindragon.chapter07_airbnb.util

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

// Created by raindragonn on 2021/05/10.


fun Context.dpToPx(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics)
        .toInt()
}