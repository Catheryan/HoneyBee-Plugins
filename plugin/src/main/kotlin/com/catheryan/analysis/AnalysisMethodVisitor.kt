package com.catheryan.analysis

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AnalysisMethodVisitor(
    private val className: String,
) : MethodVisitor(Opcodes.ASM9){

    var buildType: String = ""

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        MethodAnalysisUtils.filterAndAddMethod(buildType, currentClass = className, currentMethod = descriptor!!, invokeClassName = owner!!,invokeMethod = name!!)
    }
}