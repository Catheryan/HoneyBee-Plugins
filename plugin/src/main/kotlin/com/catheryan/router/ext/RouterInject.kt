package com.catheryan.router.ext

import groovyjarjarasm.asm.Opcodes
import org.jetbrains.kotlin.konan.file.File
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

/**
 * @author yanzhenghao on 2023/8/31
 * @description
 */
object RouterInject {
    private const val NAVIGATION = "com/catheryan/platform/router/router_core/RouterNavigation"
    private const val NAVIGATION_INJECT_METHOD = "init"
    private const val NAVIGATION_FIELD_NAME = "routerMap"

    fun inject(origin: ByteArray, targetList: MutableList<String>): ByteArray {
        val cr = ClassReader(origin)
        val cw = ClassWriter(cr, 0)
        val cv = RouterClassVisitor(cw, targetList)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

    class RouterClassVisitor(cw: ClassWriter, private val clas: MutableList<String>) : ClassVisitor(
        Opcodes.ASM9,cw) {
        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
            if (name == NAVIGATION_INJECT_METHOD) {
                return RouterMethodVisitor(mv, clas)
            }
            return mv
        }

    }

    class RouterMethodVisitor(mv: MethodVisitor?, private val clas: MutableList<String>) : MethodVisitor(Opcodes.ASM9,mv) {
        override fun visitInsn(opcode: Int) {
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                clas.map {
                    it.replace(".class", "")
                }.forEach { className ->
                    mv.visitTypeInsn(Opcodes.NEW, className)
                    mv.visitInsn(Opcodes.DUP)
                    mv.visitMethodInsn(
                        Opcodes.INVOKESPECIAL,
                        className,
                        "<init>",
                        "()V",
                        false
                    )
                    mv.visitFieldInsn(
                        Opcodes.GETSTATIC,
                        NAVIGATION,
                        NAVIGATION_FIELD_NAME,
                        "Ljava/util/Map;"
                    )
                    mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        className,
                        "init",
                        "(Ljava/util/Map;)V",
                        false
                    )
                }
            }
            super.visitInsn(opcode)
        }
        override fun visitMaxs(maxStack: Int, maxLocals: Int) {
            super.visitMaxs(maxStack + clas.size, maxLocals)
        }
    }

}