package com.wstxda.clippy.utils

import android.util.Log

object Logcat {

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }

    // Specific logging for URL processing (internal naming)
    fun logUrlProcessingStart(tag: String, url: String) {
        d(tag, "Starting URL processing: $url")
    }

    fun logUrlProcessingSuccess(tag: String, url: String) {
        d(tag, "URL processing completed: $url")
    }

    fun logToolExecution(tag: String, toolName: String) {
        d(tag, "Executing tool: $toolName")
    }

    fun logModuleExecution(tag: String, moduleName: String) {
        d(tag, "Executing module: $moduleName")
    }
}