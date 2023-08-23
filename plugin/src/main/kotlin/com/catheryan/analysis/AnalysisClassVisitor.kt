package com.catheryan.analysis

import groovyjarjarasm.asm.Opcodes
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class AnalysisClassVisitor(classVisitor: ClassVisitor, private var buildType: String) :
    ClassVisitor(Opcodes.ASM9, classVisitor) {
    private var className: String = ""
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name ?: ""
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return AnalysisMethodVisitor(
            className
        ).apply {
            buildType = this@AnalysisClassVisitor.buildType
        }
    }
}