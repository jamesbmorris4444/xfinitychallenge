package com.sample.commonlibrary.repository.network

import com.google.gson.GsonBuilder
import com.sample.commonlibrary.logger.LogUtils
import com.sample.commonlibrary.utils.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    val client: APIInterface
        get() {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(
                        LogUtils.TagFilter.API
                    ), String.format("okHttp logging interceptor=%s", message))
                }
            })
            interceptor.level = HttpLoggingInterceptor.Level.BASIC  // BASIC or BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val gson = GsonBuilder()
                .registerTypeAdapter(
                    Constants.URBANDICT_LIST_CLASS_TYPE,
                    MeaningsJsonDeserializer()
                )
                .create()
            val builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(Constants.URBANDICT_BASE_URL)
            return builder.build().create(APIInterface::class.java)
        }

}
