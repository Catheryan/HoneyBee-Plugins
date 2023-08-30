package com.catheryan.demo

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.catheryan.platform.router.router_annotation.RouterAnnotation

/**
 * @author yanzhenghao on 2023/8/30
 * @description
 */
@RouterAnnotation(url = "login", desc = "登陆页面")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER
            text = "登录"
            textSize = 30F
        })
    }
}