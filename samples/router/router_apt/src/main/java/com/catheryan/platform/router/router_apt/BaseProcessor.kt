package com.catheryan.platform.router.router_apt

import com.catheryan.platform.router.router_apt.utils.Logger
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * @author yanzhenghao on 2023/8/30
 * @description
 */
abstract class BaseProcessor : AbstractProcessor() {
    lateinit var elementUtils: Elements
    lateinit var types: Types
    lateinit var mFiler: Filer
    lateinit var logger: Logger

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mFiler = processingEnv.filer
        types = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        logger = Logger(processingEnv.messager)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}