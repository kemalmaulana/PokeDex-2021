package com.kemsky.pokedex.data.remote.interceptor

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.kemsky.pokedex.core.annotation.MustBeAuthenticated
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class AuthInterceptor(context: Context) : Interceptor {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("token", MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val invocation =
            chain.request().tag(Invocation::class.java) ?: return chain.proceed(chain.request())

        val shouldAttachAuthHeader = invocation.method().annotations.any {
            it.annotationClass == MustBeAuthenticated::class
        }

        return if (shouldAttachAuthHeader && sharedPreferences.contains("token")) {
            chain.proceed(
                chain.request().newBuilder().addHeader("Authorization", "Bearer ${sharedPreferences.getString("token", null)}").build()
            )
        } else chain.proceed(chain.request())
    }

}