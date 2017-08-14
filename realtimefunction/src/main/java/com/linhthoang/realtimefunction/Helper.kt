package com.linhthoang.realtimefunction

import android.content.res.Resources

/**
 * Created by linh on 8/3/17.
 */
internal fun capWithin(value: Float, start: Float, end: Float): Float =
    if (start > end) throw CapValueException()
    else if (value > end) {
         end
    } else if (value < start) {
         start
    } else value

internal fun dp2px(resources: Resources, dp: Float): Float {
    val scale = resources.getDisplayMetrics().density
    return dp * scale + 0.5f
}

internal fun sp2px(resources: Resources, sp: Float): Float {
    val scale = resources.getDisplayMetrics().scaledDensity
    return sp * scale
}