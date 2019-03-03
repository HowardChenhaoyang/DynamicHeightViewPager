package viewpager.chy.howard.com.dynamicheightviewpager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView1.setOnClickListener {
            SecondActivity.start(this)
        }
        textView2.setOnClickListener {
            ThirdActivity.start(this)
        }
    }
}
