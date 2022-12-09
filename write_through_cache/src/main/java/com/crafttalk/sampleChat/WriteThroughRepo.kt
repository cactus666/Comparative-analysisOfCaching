package com.crafttalk.sampleChat

import android.content.Context
import com.example.core.Strategy
import com.example.core.trackRequest

class WriteThroughRepo(
    private val storage: WriteThroughStorage
) {

    fun getTestData(context: Context, page: Int): ResultTest? {
        trackRequest(context, Strategy.WRITE_THROUGH)
        return storage.getData(
            context,
            Strategy.WRITE_THROUGH,
            ResultTest::class.java,
            mapOf("page" to page)
        )
    }
}