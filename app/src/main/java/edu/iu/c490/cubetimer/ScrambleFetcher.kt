package edu.iu.c490.cubetimer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.iu.c490.cubetimer.api.ScrambleApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "fetcher"
class ScrambleFetcher {
    private val scrambleApi: ScrambleApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://scrambler-api.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        scrambleApi = retrofit.create(ScrambleApi::class.java)
    }

    fun fetchScramble(puzzle: String): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        scrambleApi.fetchScramble(puzzle).enqueue(object: Callback<List<String>> {

            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            ) {
                val response = response.body()
                responseLiveData.value = response?.get(0)

            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d(TAG, "$t")
            }
        })
        return responseLiveData
    }
}