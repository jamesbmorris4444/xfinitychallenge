package com.sample.wireviewer.repository.network

import com.sample.wireviewer.repository.storage.Meaning
import com.sample.wireviewer.utils.Constants
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {
    @GET("users/{user}/repos")
    fun getMeanings(
        @Path(Constants.URBANDICT_TERM) term: String,
        @Query(Constants.PER_PAGE) perPage: Int,
        @Query(Constants.PAGE) page: Int,
        @Query(Constants.DIRECTION) direction: String
    ): Flowable<List<Meaning>>
}

data class MeaningsResponse (
    val list: List<Meaning>
)