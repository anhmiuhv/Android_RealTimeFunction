package com.linhthoang.realtimefunction

/**
 * Created by linh on 8/2/17.
 */
 abstract class Interpolator {
    /**
     * Linear interpolator
     */
    class Linear: Interpolator() {
        override fun interpolate(start: Float, end: Float, percent: Float): Float {
            return (start * (1 - percent) + end * percent)
        }
    }

    /**
     * Cosine interpolator
     */
    class Cosine: Interpolator() {
        override fun interpolate(start: Float, end: Float, percent: Float): Float {
            if (percent <= 0) return start
            if (percent >= 1) return end
            val mu2: Double

            mu2 = (1 - Math.cos(percent * Math.PI)) / 2f
            return (start * (1 - mu2) + end * mu2).toFloat()

        }
    }
    abstract fun interpolate(start: Float, end: Float, percent: Float): Float
}