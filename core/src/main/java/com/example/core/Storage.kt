package com.example.core

abstract class Storage <R> {

    fun getData(args: Map<String, Any>): R? {
        TODO()
    }

    abstract fun getDataFromRemote(args: Map<String, Any>): R?

    fun getDataFromLazyCache(): R? {
        TODO()
    }

    fun getDataFromSynchronizedCache(): R? {
        TODO()
    }

    fun getDataFromWriteThroughCache(): R? {
        TODO()
    }
}



