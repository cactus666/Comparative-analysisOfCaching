package com.example.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import retrofit2.Response
import java.io.File

/*
Отбор признаков

ВВедение

Кэширование данных
Кэширование — это стратегия, при которой вы храните копию данных наряду с основным хранилищем данных.

Бывают различные стратегии кэширования: lazy, synchronized, write-through и LRU

Сохранять данные можно, как на устройстве, так и на сервере.

Обработка на клиенте
Признаки необходимые для выбора стратегии кэширования данных:

- тип интернет соединения
- скорость интернет соединения
- размер респонса
- размер памяти на устройстве под это приложение
- существует ли завязка на интернет
- как часто пользователь взаимодействует с приложением

Обработка данных на сервере
Признаки позволяющие определить место хранения данных

- производилась ли очистка данных приложения
- производилась ли очистка внутреннего кэша приложения
- производилась ли очистка внешнего кэша приложения
- размер свободного места на устройстве
- размер данных во внутреннем кэше
- размер данных во внешнем кэше
- размер данных в хранилище приложения

Методы получения признаков:

*/

// Метод получения типа интернет соединения
@RequiresApi(Build.VERSION_CODES.M)
fun getInetInfo(context: Context): String? {
    val connectManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectManager.getNetworkCapabilities(connectManager.activeNetwork) ?: return null
    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> "Wi-Fi Aware"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> "LoWPAN"
        else -> null
    }
}

// Метод получения скорости интернет соединения
@RequiresApi(Build.VERSION_CODES.M)
fun getSpeedInet(context: Context): Int? {
    val connectManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectManager.getNetworkCapabilities(connectManager.activeNetwork)
    return networkCapabilities?.linkDownstreamBandwidthKbps
}

// Метод получения размера респонса
fun <T> getResponseSize(response: Response<T>): Int? {
    return if (response.isSuccessful) {
        response.raw().body?.bytes()?.size
    } else {
        null
    }
}

// метод получения информации производилась ли очистка данных приложения
fun isDataCleared(context: Context): Boolean {
    val pref = context.getSharedPreferences("test", Context.MODE_PRIVATE)
    val isDataCleared = pref?.getBoolean("isDataCleared", true) ?: true
    if (isDataCleared) {
        pref?.edit()?.apply {
            putBoolean("isDataCleared", false)
            apply()
        }
    }
    return isDataCleared
}

// метод получения информации производилась ли очистка внутреннего кэша приложения
fun isInnerCacheCleared(context: Context): Boolean {
    val dir: File? = context.cacheDir
    return if (dir != null && dir.isDirectory) {
        if (dir.list().isEmpty()) {
            File.createTempFile("testInnerFile", null, context.cacheDir)
            true
        } else {
            false
        }
    } else {
        true
    }
}

// метод получения информации производилась ли очистка внешнего кэша приложения
fun isExternalCacheCleared(context: Context): Boolean {
    val dir: File? = context.externalCacheDir
    return if (dir != null && dir.isDirectory) {
        if (dir.list().isEmpty()) {
            File.createTempFile("testExternalFile", null, context.externalCacheDir)
            true
        } else {
            false
        }
    } else {
        true
    }
}

// метод получения размера данных в хранилище приложения
fun getDataSize(context: Context): Long? {
    return context.filesDir?.parentFile?.let { getDirSize(it) }
}

// метод получения размера данных во внутреннем кэше
fun getInnerCacheSize(context: Context): Long? {
    return context.cacheDir?.let { getDirSize(it) }
}

// метод получения размера данных во внешнем кэше
fun getExternalCacheSize(context: Context): Long? {
    return context.externalCacheDir?.let { getDirSize(it) }
}

private fun getDirSize(dir: File): Long {
    var size: Long = 0
    for (file in dir.listFiles()) {
        if (file != null && file.isDirectory) {
            size += getDirSize(file)
        } else if (file != null && file.isFile) {
            size += file.length()
        }
    }
    return size
}

// метод получения размера памяти на устройстве под это приложение
fun getMaxMemory(): Long {
    return Runtime.getRuntime().maxMemory()
}

// метод получения размера свободного места на устройстве при старте приложения
fun getFreeMemory(): Long {
    return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()
}
