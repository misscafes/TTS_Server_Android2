package com.github.jing332.script.engine

import android.content.Context
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import com.eclipsesource.v8.V8Object
import com.github.jing332.script.JavaScriptEngine
import com.github.jing332.script.source.ReaderScriptSource
import com.github.jing332.script.source.ScriptSource
import com.github.jing332.script.source.StringScriptSource
import java.util.concurrent.atomic.AtomicBoolean

/**
 * V8 JavaScript Engine - Google V8 引擎，完全支持 ES6+
 */
open class V8ScriptEngine(
    private val androidContext: Context? = null,
    private val classLoader: ClassLoader? = null
) : JavaScriptEngine() {
    private var v8: V8? = null
    private val isInitialized = AtomicBoolean(false)
    private val isDestroyed = AtomicBoolean(false)

    override fun init() {
        check(!isDestroyed.get()) { "Engine has been destroyed" }
        if (isInitialized.compareAndSet(false, true)) {
            v8 = V8.createV8Runtime()
        }
    }

    private fun ensureV8(): V8 {
        check(!isDestroyed.get()) { "Engine has been destroyed" }
        return v8 ?: throw IllegalStateException("Engine not initialized. Call init() first.")
    }

    override fun execute(source: ScriptSource): Any? {
        val v8Runtime = ensureV8()
        val script = when (source) {
            is StringScriptSource -> source.script
            is ReaderScriptSource -> source.reader.readText()
            else -> throw IllegalArgumentException("Unsupported source type: ${source::class.java.name}")
        }

        return try {
            val result = v8Runtime.executeScript(script)
            convertV8Result(result)
        } catch (e: Exception) {
            throw RuntimeException("Failed to execute JavaScript: ${e.message}", e)
        }
    }

    private fun convertV8Result(result: Any?): Any? {
        return when {
            result == null || result == V8.getUndefined() -> null
            result is Boolean -> result
            result is Number -> result
            result is String -> result
            result is V8Object -> {
                try {
                    result.toString()
                } finally {
                    result.release()
                }
            }
            result is V8Array -> {
                try {
                    (0 until result.length()).map { convertV8Result(result.get(it)) }
                } finally {
                    result.release()
                }
            }
            else -> result.toString()
        }
    }

    override fun put(key: String, value: Any?) {
        val v8Runtime = ensureV8()
        when (value) {
            null -> v8Runtime.addNull(key)
            is String -> v8Runtime.add(key, value)
            is Int -> v8Runtime.add(key, value)
            is Long -> v8Runtime.add(key, value)
            is Double -> v8Runtime.add(key, value)
            is Boolean -> v8Runtime.add(key, value)
            else -> v8Runtime.add(key, value.toString())
        }
    }

    override fun get(key: String): Any? {
        val v8Runtime = ensureV8()
        return try {
            val result = v8Runtime.getValue(key)
            convertV8Result(result)
        } catch (e: Exception) {
            null
        }
    }

    fun invokeFunction(name: String, vararg args: Any?): Any? {
        val v8Runtime = ensureV8()
        return try {
            val v8Args = args.map { convertToV8(v8Runtime, it) }.toTypedArray()
            val result = v8Runtime.executeJSFunction(name, *v8Args)
            convertV8Result(result)
        } catch (e: Exception) {
            throw RuntimeException("Failed to invoke function $name: ${e.message}", e)
        }
    }

    private fun convertToV8(v8: V8, value: Any?): Any? {
        return when (value) {
            null -> null
            is String -> value
            is Int -> value
            is Long -> value
            is Double -> value
            is Boolean -> value
            else -> value.toString()
        }
    }

    override fun destroy() {
        if (isDestroyed.compareAndSet(false, true)) {
            try {
                v8?.release()
                v8 = null
                isInitialized.set(false)
            } catch (e: Exception) {
                android.util.Log.e("V8ScriptEngine", "Error destroying engine: ${e.message}")
            }
        }
    }

    override fun runOnUiThread(block: () -> Unit) {
        if (androidContext != null) {
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            handler.post(block)
        } else {
            block()
        }
    }
}
