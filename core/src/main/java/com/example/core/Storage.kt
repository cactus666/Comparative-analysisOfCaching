package com.example.core

abstract class Storage <R> {

    fun getData(args: Map<String, Any>): R? {
        return getDataFromRemote(args)
//        TODO()
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



