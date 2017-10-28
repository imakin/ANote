package xyz.izzulmakin.anotek

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.EditText
import android.content.Intent



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_scrolling_activity();
    }
    fun start_scrolling_activity() {
        val intent = Intent(this, ScrollingActivity::class.java)
        startActivity(intent)
    }
}
