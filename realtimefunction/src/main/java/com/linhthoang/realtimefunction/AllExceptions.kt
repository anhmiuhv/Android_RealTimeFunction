package com.linhthoang.realtimefunction

/**
 * Created by linh on 8/3/17.
 */
internal class TimeIntervalValueException : Throwable() {
    override val message: String?
        get() = super.message + "\nTime interval cannot be below or equal to zero"
}

internal class CapValueException: Throwable() {
    override val message: String?
        get() = super.message + "\nStart value cannot be bigger than the small value"
}