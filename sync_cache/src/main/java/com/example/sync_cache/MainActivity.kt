package com.example.sync_cache

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.core.Storage
import com.example.core.TestApi
import com.example.core.toData
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.test).setOnClickListener {
            val res = SyncRepository(SyncStorage()).getTestData(
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

    override fun getDataFromRemote(args: Map<String, Any>): ResultTest? {
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

    fun getTestData(articleId: Int): ResultTest? {
        return syncStorage.getData(
            mapOf("article_id" to articleId)
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