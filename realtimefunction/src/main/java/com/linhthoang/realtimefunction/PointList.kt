package com.linhthoang.realtimefunction

/**
 * Created by linh on 8/2/17.
 */
const private val MAX_SIZE = 10000

internal class PointList {
    var size = 1000


    private var array: ArrayList<Float> = ArrayList<Float>().apply { addAll(Array(MAX_SIZE, { 0f })) }
    private var mHeight = 0
    private var mWidth = 0

    fun default(height: Int, width: Int) {
        mHeight = height
        mWidth = width
        synchronized(array) {
            array = array.mapIndexed { index, fl ->
                if (index >= MAX_SIZE - size) {
                    val mindex = index - (MAX_SIZE - size)
                    if (mindex % 2 == 0) return@mapIndexed width * mindex / size.toFloat()
                }
                return@mapIndexed height / 2f
            } as ArrayList<Float>
        }
    }

    fun getArray(height: Int = mHeight, width: Int = mWidth): FloatArray {
        synchronized(array) {
            val ar = array.takeLast(size).mapIndexed { index, fl ->
                if (index % 2 == 0) return@mapIndexed width * index / size.toFloat()
                return@mapIndexed fl
            } as ArrayList<Float>
            val ar2 = ArrayList<Float>()
            for (i in ar.indices) {
                if (i % 2 == 1) {
                    ar2.add(ar[i - 1])
                    ar2.add(ar[i])
                    ar2.add(ar[i - 1])
                    ar2.add(ar[i])
                }
            }
            ar2.removeAt(0)
            ar2.removeAt(0)
            ar2.removeAt(ar2.size - 1)
            ar2.removeAt(ar2.size - 1)
            return ar2.toFloatArray()
        }
    }

    fun add(height: Float) {
        synchronized(array) {
            array.add(0f)
            array.add(height)
            array.removeAt(0)
            array.removeAt(0)
        }
    }

    fun modifySize(delta: Float) {
        synchronized(array) {
            size = capWithin(size + delta, 1000f, 10000f).toInt()
        }
    }
}

