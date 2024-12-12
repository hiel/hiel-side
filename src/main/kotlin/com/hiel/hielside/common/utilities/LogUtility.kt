package com.hiel.hielside.common.utilities

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface LogUtility {
    val log: Logger get() = LoggerFactory.getLogger(this.javaClass)

    companion object {
        fun <T> logger(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)
    }
}
