package com.catheryan.mall.configure

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

private typealias AndroidBaseExtension = BaseExtension
internal fun Project.configureAndroid() = this.extensions.getByType<AndroidBaseExtension>().run {
    defaultConfig {
        versionCode = 2
        versionName = "1.0.1"
    }
}