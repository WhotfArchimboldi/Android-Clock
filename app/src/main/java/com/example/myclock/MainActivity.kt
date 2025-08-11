package com.example.myclock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Typeface
import android.view.Gravity
import android.widget.LinearLayout
import android.graphics.Color
import android.util.TypedValue
import android.view.ViewTreeObserver

class MainActivity : AppCompatActivity() {
    private lateinit var timeTextView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            val currentTime = timeFormat.format(Date())
            timeTextView.text = currentTime
            // 计算距离下一个整分钟的毫秒数
            val now = Calendar.getInstance()
            val msToNextMinute = (60 - now.get(Calendar.SECOND)) * 1000 - now.get(Calendar.MILLISECOND)
            handler.postDelayed(this, msToNextMinute.toLong())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeTextView = TextView(this).apply {
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.WHITE)
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            gravity = Gravity.CENTER
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            // 字体大小自适应屏幕宽度
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val width = width.takeIf { it > 0 } ?: return
                    // 估算字体大小：宽度的 1/3，适合"hh:mm"
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, width / 3f)
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
        setContentView(timeTextView)
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateTimeRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateTimeRunnable)
    }
}