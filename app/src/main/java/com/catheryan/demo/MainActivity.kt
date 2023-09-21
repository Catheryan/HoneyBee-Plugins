package com.catheryan.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.catheryan.demo.ui.main.MainFragment

//import com.catheryan.platform.router.router_annotation.RouterAnnotation

//@RouterAnnotation(url = "main", desc = "主页面")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

}