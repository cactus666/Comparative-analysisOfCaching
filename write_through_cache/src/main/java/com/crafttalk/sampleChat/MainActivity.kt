package com.crafttalk.sampleChat

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.core.Storage
import com.example.core.toData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val res = WtRepository(WtStorage()).getTestData(2)

    }

class BodyTest(
    val article_page: Int
): Serializable

class ResultTest(
    val res_article: String
): Serializable

class WtStorage: Storage<ResultTest>() {

    override fun getDataFromRemote(args: Map<String, Any>): ResultTest {
        val page = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            args.getOrDefault("article_page", 1)
        } else {
            args["page"]
        }
        return ResultTest(
            when (page) {
                1 -> """asdasda das dasd sa da"""
                2 -> """2"""
                else -> "Hello"
            }
        )
    }


class WtRepository(
    private val wtStorage: WtStorage
) {
    fun getTestData(page: Int): ResultTest? {
        return wtStorage.getData(
            mapOf("article_page" to page)
        )
    }
}