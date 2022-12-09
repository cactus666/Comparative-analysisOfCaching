package com.crafttalk.sampleChat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.crafttalk.sampleChat.databinding.ActivityMainBinding
import com.example.core.Strategy
import com.example.core.getCountRequestAfter
import com.example.core.getCountRequestBefore

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val comments = listOf(
            binding.comment1,
            binding.comment2,
            binding.comment3,
            binding.comment4,
            binding.comment5,
        )

        listOf(
            binding.loadBtn1,
            binding.loadBtn2,
            binding.loadBtn3,
            binding.loadBtn4,
            binding.loadBtn5,
        ).withIndex().forEach { b ->
            b.value.setOnClickListener {
                val res = WriteThroughRepo(WriteThroughStorage()).getTestData(this, b.index + 1)
                Log.d(TAG, "onGetComment: $res ${b.index}")
                comments[b.index].text = res?.resPage ?: "Ошибка при получении данных"

                Log.d(TAG, "request before: ${getCountRequestBefore(this, Strategy.WRITE_THROUGH)}")
                Log.d(TAG, "request after: ${getCountRequestAfter(this, Strategy.WRITE_THROUGH)}")
            }
        }
    }
}