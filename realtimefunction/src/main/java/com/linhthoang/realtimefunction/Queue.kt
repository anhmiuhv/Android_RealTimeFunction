package com.linhthoang.realtimefunction

/**
 * Created by linh on 8/2/17.
 */
internal class Queue<T> {
    private class Item<T>(val data : T, var next : Item<T>?)

    private var head : Item<T>? = null
    private var tail : Item<T>?= null
    private var mLock: Int

    init {
        mLock = lock++
    }

    companion object {
        private var lock = 0
    }
    fun push(item : T) {
        synchronized(mLock) {
            val i = Item(item, null)
            if (tail == null) {
                head = i
                tail = head
            } else {
                tail!!.next = i
                tail = i
            }
        }
    }

    fun pop(): T? {
        synchronized(mLock) {
            if (head == null)
                return null
            else {
                val result = head!!.data
                head = head!!.next
                if (head == null)
                    tail = null
                return result
            }
        }
    }

    val isEmpty
        get() = head == null


}