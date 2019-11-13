package edu.iu.c490.cubetimer.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ScrambleApi {
    @GET("/{puzzle}/1")
    fun fetchScramble(@Path("puzzle") puzzle: String): Call<List<String>>
}