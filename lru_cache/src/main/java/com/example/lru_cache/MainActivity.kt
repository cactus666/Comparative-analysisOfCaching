package com.example.lru_cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.lruCache
import com.example.core.*
import com.google.gson.Gson
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_lru)


        Log.d("TEST_LOG_ANAL", "Get count request before: ${getCountRequestBefore(this, Strategy.LRU)};")
        Log.d("TEST_LOG_ANAL", "Get count request after: ${getCountRequestAfter(this, Strategy.LRU)};")

        val im1 = findViewById<ImageView>(R.id.test1)
        val im2 = findViewById<ImageView>(R.id.test2)


        findViewById<TextView>(R.id.test3).setOnClickListener {
            val res = LRURepository(LRUStorage()).getTestData(this, 1)
            try {
                im1.setImageBitmap(Gson().fromJson(res?.image, Bitmap::class.java))
            }
            catch (e: Exception){
                Log.d("TEST_LOG", "${e.message}")
            }
            Log.d("TEST_LOG", "res: ${res};")
        }

        findViewById<TextView>(R.id.test4).setOnClickListener {
            val res = LRURepository(LRUStorage()).getTestData(this, 1)
            try {
                im2.setImageBitmap(Gson().fromJson(res?.image, Bitmap::class.java))
            }
            catch (e: Exception){
                Log.d("TEST_LOG", "${e.message}")
            }
            Log.d("TEST_LOG", "res: ${res};")
        }


    }
}

class BodyTest(
    val imageId: Int
): Serializable

class ResultTest(
    val image: String
): Serializable

class LRUStorage(
): Storage<ResultTest>() {

    override fun getDataFromRemote(context: Context, args: Map<String, Any>): ResultTest {
        trackRemote(context, Strategy.LRU)
        val imageId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            args.getOrDefault("imageId", 1)
        } else {
            args["imageId"]
        }

        val images_array = arrayOf(R.drawable.a13b2c073c, R.drawable.b87a8f)
        val bitMap = BitmapFactory.decodeResource(context.resources, images_array[imageId as Int])
        return ResultTest(
            Gson().toJson(bitMap)
        )
    }
}

class LRURepository(
    private val LRUStorage: LRUStorage
) {

    fun getTestData(context: Context, imageId: Int): ResultTest? {
        trackRemote(context, Strategy.LRU)
        return LRUStorage.getData(
            context,
            Strategy.LRU,
            ResultTest::class.java,
            mapOf("imageId" to imageId)
        )
    }
}
