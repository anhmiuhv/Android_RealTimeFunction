package com.linhthoang.realtimefunction

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View


/**
 * Created by linh on 8/2/17.
 * View to display
 * Add data to queue -> queue add points to point list -> view display points in point lists
 */


open class RealtimeFunctionView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    /**
     * paint for the line
     */
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = lineColor
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = lineWidth
    }

    private val mData = PointList()
    /**
     * background color
     */
    var background_color = Color.BLACK
    /**
     * scroll rate (higher is faster)
     */
    var scrollRate = 30f
    set(value) {
        field = capWithin(value + value % 2, 5f, 500f)
    }
    private var points = FloatArray(1)
    private var mScaleFactor = 1f
    private val mScaleDetector = ScaleGestureDetector(context, Listener())
    private var lineWidth = dp2px(resources, 10f)
    private var lineColor = Color.RED
    /**
     * refresh rate for updating the value point.
     * For examples, if refreshrate = 33 then each value point correspond to 33 ms
     */
    var refreshRate = 33
    set(value) {
        manager.refreshRate = value
    }
    private inner class Listener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            mScaleFactor *= detector!!.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            return true;
        }
    }
    private val mGestureDetector = GestureDetectorCompat(context, GestureListener())
    private inner class GestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            if (e1 == null || e2 == null) return false
            if (distanceX < 0)
                mData.modifySize(scrollRate)
            else mData.modifySize(-scrollRate)
            return true
        }
    }

    /**
     * this is for adding th data point and change the value interpolator
     */
    val manager = Manager()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mData.default(h, w)
        points = mData.getArray()
        manager.mHeight = h

    }
    private var startUpdate = false
    private fun update() {
        handler.post {
            mData.add(manager.getCurrentHeight(points[points.size - 1]))
            points = mData.getArray()
            handler.postDelayed({
                invalidate()
                update()
                }, refreshRate.toLong())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        canvas.save()
        canvas.scale(1f,mScaleFactor, (width / 2).toFloat(), (height /2).toFloat())
        canvas.drawColor(background_color)
        paint.strokeWidth = lineWidth
        paint.color = lineColor
        canvas.drawLines(points, paint)
        if (!startUpdate) {
            startUpdate = true
            update()
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event)
        return true;
    }

    class Manager {
        /**
         * how prev value change to the new value
         */
        var interpolator: Interpolator = Interpolator.Cosine()
        /**
         * the range of value (-interval < value < interval)
         */
        var interval = 20f
            set(value) {
                if (value < 0) field = 0.1f else field = value
            }

        internal var mHeight = 300
        internal var refreshRate = 33
        private val queue: Queue<Point> = Queue()
        private var currentPoint: Point? = null
        private var currentTime = -refreshRate
        private var startPoint: Point? = null

        /**
         * add data points to queue
         * @param height data point value
         * @param timeInterval the delta time to change to this value
         */
        fun addPoint(height: Float, timeInterval: Long = 1000) {
            if (timeInterval <= 0) throw TimeIntervalValueException()
            val mHeight = capWithin(height, -interval, interval)
            queue.push(Point(toCanvasSpace(mHeight), 0, timeInterval))
        }

        internal fun getCurrentHeight(prevHeight: Float): Float {
            currentTime += refreshRate
            if (currentPoint == null || currentTime > currentPoint!!.time) {
                val oldPoint = currentPoint
                currentPoint = queue.pop()?.apply { time = currentTime + timeInterval }
                if (currentPoint == null) return prevHeight
                startPoint = Point(prevHeight, currentTime.toLong(), 0)
            }
            if (currentPoint!!.timeInterval < 34) return currentPoint!!.height
            return interpolator.interpolate(startPoint!!.height, currentPoint!!.height,getPercentage())
        }

        private fun getPercentage() = (currentTime - startPoint!!.time) / currentPoint!!.timeInterval.toFloat()
        private fun toCanvasSpace(height: Float): Float = (height + interval) / (interval * 2) * mHeight
    }

    private data class Point(val height: Float, var time: Long, val timeInterval: Long)

    init {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.RealtimeFunctionView)
        lineWidth = a.getDimension(R.styleable.RealtimeFunctionView_lineWidth, paint.strokeWidth)
        background_color = a.getColor(R.styleable.RealtimeFunctionView_background_color, background_color)
        lineColor = a.getColor(R.styleable.RealtimeFunctionView_line_color, paint.color)
        manager.interval = a.getFloat(R.styleable.RealtimeFunctionView_interval, manager.interval)
        a.recycle()
    }
}


