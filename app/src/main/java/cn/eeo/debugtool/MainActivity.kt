package cn.eeo.debugtool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.eeo.debug.lib.DebugProbe

class MainActivity : AppCompatActivity(), View.OnClickListener {



    @DebugProbe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tv).setOnClickListener(this)

    }

    @DebugProbe
    override fun onClick(p0: View?) {

        Thread.sleep(300);
    }


}