package com.example.lazy_cache

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.core.Storage
import com.example.core.Strategy
import com.example.core.trackRemote
import com.example.core.trackRequest
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun getDataFromRemote(args: Map<String, Any>): ResultTest {
        trackRemote(Strategy.LRU)
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
        trackRequest(Strategy.LRU)
        return lazyStorage.getData(
            context,
            Strategy.LRU,
            ResultTest::class.java,
            mapOf("page" to page)
        )
    }
}