package com.example.lazy_cache

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.core.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("TEST_LOG_ANAL", "Get count request before: ${getCountRequestBefore(this, Strategy.LAZY)};")
        Log.d("TEST_LOG_ANAL", "Get count request after: ${getCountRequestAfter(this, Strategy.LAZY)};")

        findViewById<TextView>(R.id.test1).setOnClickListener {
            val res = LazyRepository(LazyStorage()).getTestData(this, 1)
            Log.d("TEST_LOG", "res: ${res};")
        }

        findViewById<TextView>(R.id.test2).setOnClickListener {
            val res = LazyRepository(LazyStorage()).getTestData(this,2)
            Log.d("TEST_LOG", "res: ${res};")
        }

    }
}

data class ResultTest(
    val resPage: String
): Serializable


class LazyStorage: Storage<ResultTest>() {

    override fun getDataFromRemote(context: Context, args: Map<String, Any>): ResultTest {
        trackRemote(context, Strategy.LAZY)
        val page = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            args.getOrDefault("page", 1)
        } else {
            args["page"]
        }
        return ResultTest(
            when (page) {
                1 -> """Мама мыла раму"""
                2 -> "omae wa mou shindeiru nani"
                else -> "lol"
            }
        )
    }
}

class LazyRepository(
    private val lazyStorage: LazyStorage
) {

    fun getTestData(context: Context, page: Int): ResultTest? {
        trackRequest(context, Strategy.LAZY)
        return lazyStorage.getData(
            context,
            Strategy.LAZY,
            ResultTest::class.java,
            mapOf("page" to page)
        )
    }
}