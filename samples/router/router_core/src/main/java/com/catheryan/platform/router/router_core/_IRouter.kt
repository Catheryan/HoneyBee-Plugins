package com.catheryan.platform.router.router_core

import com.catheryan.platform.router.router_apt.data.RouterData

/**
 * @author yanzhenghao on 2023/8/30
 * @description
 */
interface _IRouter {
    fun init(map: MutableMap<String, RouterData>)
}