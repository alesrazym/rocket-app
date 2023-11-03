package cz.quanti.razym.rocketapp.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class TestUtils {
    companion object {
        fun <T> loadJsonResource(filename: String, type: Type): T {
            val json = TestUtils::class.java.classLoader?.getResource(filename)?.readText() ?: ""
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter<T>(type)

            return adapter.fromJson(json)!!
        }
    }
}