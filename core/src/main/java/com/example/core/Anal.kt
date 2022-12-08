package com.example.core

import android.content.Context

fun trackRemote(context: Context, strategy: Strategy) {
    val pref = context.getSharedPreferences("track_remote", Context.MODE_PRIVATE)
    pref?.edit()?.apply {
        putInt(strategy.name, pref.getInt(strategy.name, 0) + 1)
        apply()
    }
}

fun trackRequest(context: Context, strategy: Strategy) {
    val pref = context.getSharedPreferences("track_request", Context.MODE_PRIVATE)
    pref?.edit()?.apply {
        putInt(strategy.name, pref.getInt(strategy.name, 0) + 1)
        apply()
    }
}

fun getCountRequestBefore(context: Context, strategy: Strategy): Int {
    val pref = context.getSharedPreferences("track_request", Context.MODE_PRIVATE)
    return pref?.getInt(strategy.name, 0) ?: 0
}

fun getCountRequestAfter(context: Context, strategy: Strategy): Int {
    val pref = context.getSharedPreferences("track_remote", Context.MODE_PRIVATE)
    return pref?.getInt(strategy.name, 0) ?: 0
}