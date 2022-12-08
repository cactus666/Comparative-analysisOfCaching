package com.example.core

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson

enum class Strategy {
    LAZY,
    SYNCHRONIZED,
    WRITE_THROUGH,
    LRU
}

abstract class Storage <R> {

    fun getData(context: Context, strategy: Strategy?, clazz: Class<R>, args: Map<String, Any>): R? {
        return when (strategy) {
            Strategy.LAZY -> getDataFromLazyCache(context, clazz, args)
            Strategy.SYNCHRONIZED -> getDataFromSynchronizedCache(context, clazz, args)
            Strategy.WRITE_THROUGH -> getDataFromWriteThroughCache(context, clazz, args)
            Strategy.LRU -> getDataFromLruCache(context, clazz, args)
            else -> getDataFromRemote(context, args)
        }
    }

    abstract fun getDataFromRemote(context: Context, args: Map<String, Any>): R?

    private fun getDataFromLazyCache(context: Context, clazz: Class<R>, args: Map<String, Any>): R? {
        val key = generateKey(args)
        val dataFromCache = getData(context, key, clazz)
        return if (dataFromCache == null) {
            val remoteData = getDataFromRemote(context, args)
            saveData(context, key, remoteData)
            remoteData
        } else {
            dataFromCache
        }
    }

    private fun getDataFromSynchronizedCache(context: Context, clazz: Class<R>, args: Map<String, Any>): R? {
        val isCacheValid = getRandomBoolean()
        return if (isCacheValid) {
            getDataFromLazyCache(context, clazz, args)
        } else {
            val key = generateKey(args)
            removeData(context, key)
            val remoteData = getDataFromRemote(context, args)
            saveData(context, key, remoteData)
            remoteData
        }
    }

    private fun getDataFromWriteThroughCache(context: Context, clazz: Class<R>, args: Map<String, Any>): R? {
        val isCacheValid = getRandomBoolean()
        return if (isCacheValid) {
            getDataFromLazyCache(context, clazz, args)
        } else {
            val key = generateKey(args)
            removeData(context, key)
            val remoteData = getDataFromRemote(context, args)
            val isUploadNewDataSuccess = getRandomBoolean()
            if (isUploadNewDataSuccess) {
                saveData(context, key, remoteData)
                remoteData
            } else {
                null
            }
        }
    }

    private fun getDataFromLruCache(context: Context, clazz: Class<R>, args: Map<String, Any>): R? {
        val needRemoveCache = ((getDataSize(context) ?: 0L) + (getInnerCacheSize(context) ?: 0L) + (getExternalCacheSize(context) ?: 0L)) > 1000000
        if (needRemoveCache) {
            val key = generateKey(args)
            removeData(context, key)
        }
        return getDataFromLazyCache(context, clazz, args)
    }

    @SuppressLint("NewApi")
    fun generateKey(args: Map<String, Any>): String {
        val resultKey = StringBuilder()
        args.forEach { key, value ->
            when (value) {
                is Number, is CharSequence -> resultKey.append(value)
                else -> resultKey.append(Gson().toJson(value))
            }
        }
        return resultKey.toString()
    }

    private fun saveData(context: Context, key: String, data: R?): R? {
        val pref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        pref?.edit()?.apply {
            putString(key, Gson().toJson(data))
            apply()
        }
        return data
    }

    private fun removeData(context: Context, key: String) {
        saveData(context, key, null)
    }

    private fun getData(context: Context, key: String, clazz: Class<R>): R? {
        val pref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val json = pref?.getString(key, null) ?: return null
        return Gson().fromJson(json, clazz)
    }

    private fun getRandomBoolean(): Boolean {
        return System.currentTimeMillis().toInt() % 2 == 0
    }
}