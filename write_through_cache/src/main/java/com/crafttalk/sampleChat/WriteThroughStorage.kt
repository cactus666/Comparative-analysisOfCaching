package com.crafttalk.sampleChat

import android.content.Context
import com.example.core.Storage
import com.example.core.Strategy
import com.example.core.trackRemote

class WriteThroughStorage : Storage<ResultTest>() {
    override fun getDataFromRemote(context: Context, args: Map<String, Any>): ResultTest {
        trackRemote(context, Strategy.WRITE_THROUGH)
        val page = try {
            args["page"]!!
        } catch (e: Exception) {
            1
        }

        return ResultTest(
            when (page) {
                1 -> "САМЫЙ БОЛЬШОЙ КОММЕНТАРИЙ1"
                2 -> "САМЫЙ БОЛЬШОЙ КОММЕНТАРИЙ2"
                3 -> "САМЫЙ БОЛЬШОЙ КОММЕНТАРИЙ3"
                4 -> "САМЫЙ БОЛЬШОЙ КОММЕНТАРИЙ4"
                else -> "САМЫЙ БОЛЬШОЙ КОММЕНТАРИЙ5"
            }
        )
    }
}