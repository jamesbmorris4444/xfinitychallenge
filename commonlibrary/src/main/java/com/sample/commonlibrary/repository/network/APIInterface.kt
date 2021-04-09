package com.sample.commonlibrary.repository.network


import com.sample.commonlibrary.repository.storage.Header
import io.reactivex.Flowable
import okhttp3.HttpUrl
import retrofit2.http.GET
import retrofit2.http.Url

interface APIInterface {
    @GET
    fun getMeanings(@Url page: HttpUrl): Flowable<Header>
}