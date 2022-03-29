package cn.eeo.debugtool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.eeo.debug.lib.DebugProbe
import cn.eeo.debug.lib.DebugSkip
import kotlin.concurrent.thread


@DebugProbe
class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv).setOnClickListener(this)
        thread {
            testThread()
        }
    }

    override fun onClick(p0: View?) {
        Thread.sleep(300)
    }

    private fun testThread(){
        Thread.sleep(500)
    }

}