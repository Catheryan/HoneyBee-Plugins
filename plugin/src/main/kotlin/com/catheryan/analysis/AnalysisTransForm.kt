package com.catheryan.analysis

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor

/**
 * ASM的操作对象，继承AsmClassVisitorFactory
 * @author catheryan
 * @since 7.0.0
 */
abstract class AnalysisTransForm : AsmClassVisitorFactory<AnalysisParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val buildType = parameters.get().buildType.get()
        return AnalysisClassVisitor(nextClassVisitor,buildType)
    }

    //判定对应的类是否要拦截
    //这里我们主要拦截了 android.x和 R类型
    override fun isInstrumentable(classData: ClassData): Boolean {
        return !classData.className.contains("android.x") && !classData.className.contains(".R")
    }
}