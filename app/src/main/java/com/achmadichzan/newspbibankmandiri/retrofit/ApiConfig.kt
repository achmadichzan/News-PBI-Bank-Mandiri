package com.achmadichzan.newspbibankmandiri.retrofit

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.achmadichzan.newspbibankmandiri.BuildConfig.API_KEY
import com.achmadichzan.newspbibankmandiri.BuildConfig.BASE_URL
import com.achmadichzan.newspbibankmandiri.util.CacheInterceptor
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object ApiConfig {
    fun getApiService(application: Application): ApiService {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY")
                .build()
            chain.proceed(requestHeaders)
        }

        val forceCacheInterceptor = ForceCacheInterceptor(application)

        val client = OkHttpClient().newBuilder()
            .cache(Cache(File(
                application.applicationContext.cacheDir, "http-cache"),
                10L * 1024L * 1024L)
            )
            .addNetworkInterceptor(CacheInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(forceCacheInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}


class ForceCacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        if (!isInternetAvailable()) {
            Log.d(TAG, "Using force cache.")
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }

        val response = chain.proceed(builder.build())
        Log.d(TAG, "Response headers: ${response.headers}")
        return response
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork)
            if (capabilities != null) {
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }
        return false
    }

    companion object {
        private const val TAG = "ForceCacheInterceptor"
    }

}