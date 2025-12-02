package com.example.bls.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
  private const val BASE_URL = "http://10.0.2.2:8000/"

  fun client(tokenProvider: () -> String?): Retrofit {
    val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val auth = Interceptor { chain ->
      val req = chain.request()
      val b = req.newBuilder()
      // Usamos header() en lugar de addHeader() para evitar duplicados si Retrofit ya añadió uno
      tokenProvider()?.let { b.header("Authorization", "Bearer $it") }
      // Eliminamos Content-Type forzado para permitir @Multipart y otros tipos gestionados por Retrofit
      chain.proceed(b.build())
    }
    val ok = OkHttpClient.Builder()
      .addInterceptor(auth)
      .addInterceptor(logging)
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .build()

    return Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .client(ok)
      .build()
  }
}
