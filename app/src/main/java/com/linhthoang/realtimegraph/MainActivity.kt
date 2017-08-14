package com.linhthoang.realtimegraph

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.linhthoang.realtimefunction.RealtimeFunctionView

class MainActivity : AppCompatActivity() {
    var FunctionView: RealtimeFunctionView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FunctionView = findViewById(R.id.fv) as? RealtimeFunctionView
//        FunctionView?.manager?.interpolator = Interpolator.Linear()

    }
    val start = System.currentTimeMillis()
    override fun onStart() {
        super.onStart()
        update()
    }

    private fun update() {
        FunctionView?.let {
            it.manager.addPoint((20f * Math.sin(2 * Math.PI * 0.5 * ((System.currentTimeMillis() - start) / 1000f))).toFloat(), 1000)
        }
        Handler(mainLooper).postDelayed({update()}, 1000)
    }

}

