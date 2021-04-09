package com.sample.wireviewer.repository.network

import com.sample.wireviewer.repository.storage.Header
import io.reactivex.Flowable
import okhttp3.HttpUrl
import retrofit2.http.GET
import retrofit2.http.Url

interface APIInterface {
    @GET
    fun getMeanings(@Url page: HttpUrl): Flowable<Header>
}