package com.example.sync_cache

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.core.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("TEST_LOG_ANAL", "Get count request before: ${getCountRequestBefore(this, Strategy.SYNCHRONIZED)};")
        Log.d("TEST_LOG_ANAL", "Get count request after: ${getCountRequestAfter(this, Strategy.SYNCHRONIZED)};")

        findViewById<TextView>(R.id.test).setOnClickListener {
            val res = SyncRepository(SyncStorage()).getTestData(
                this,
                (0..5).random()
            )
            Log.d("TESTEST", "RES: ${res};")
        }
    }
}

class BodyTest(
    val article_id: Int
): Serializable

data class ResultTest(
    val article: String
): Serializable





class SyncStorage(
): Storage<ResultTest>() {

    override fun getDataFromRemote(context: Context, args: Map<String, Any>): ResultTest? {
        trackRemote(context, Strategy.SYNCHRONIZED)
        val articleId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            args.getOrDefault("article_id", 0)
        } else {
            args.get("article_id")
        }

        val articles = arrayOf(
            "article1",
            "article2",
            "article3",
            "article4",
            "article5",
            "default",
        )

        return ResultTest(
            articles[articleId as Int]
        )
    }
}

class SyncRepository(
    private val syncStorage: SyncStorage
) {

    fun getTestData(context: Context, articleId: Int): ResultTest? {
        trackRequest(context, Strategy.SYNCHRONIZED)
        return syncStorage.getData(
            args=mapOf("article_id" to articleId),
            context=context,
            clazz=ResultTest::class.java,
            strategy=Strategy.SYNCHRONIZED
        )
    }
}


//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        TestRepository(TestStorage()).getTestData()
//    }
//}