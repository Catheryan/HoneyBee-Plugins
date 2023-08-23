package com.catheryan.analysis

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

/**
 * 主要给到ASMVisitor的传参作用
 * @author catheryan
 */
interface AnalysisParameters : InstrumentationParameters{
    @get:Input
    val buildType: Property<String>
}