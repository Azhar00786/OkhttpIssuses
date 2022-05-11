package com.example.okhttpissues.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.okhttpissues.data.api.ApiConnectorSingelton
import com.example.okhttpissues.data.api.ApiServiceInterface
import com.example.okhttpissues.data.api_model.IssuesItem
import com.example.okhttpissues.data.database.DatabaseGetter
import com.example.okhttpissues.data.database_entities.IssuesEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(
    private val apiCaller: ApiConnectorSingelton,
    private val databaseGetter: DatabaseGetter
) {
    var issuesList: MutableLiveData<ArrayList<IssuesItem>> = MutableLiveData()

    companion object {
        const val TAG = "MainRepository"
    }

    /*
    *   Methods for First Screen
    */


    private fun getApiConnector(): ApiServiceInterface {
        val retrofitInstance = apiCaller.getRetrofitInstance()
        return retrofitInstance.create(ApiServiceInterface::class.java)
    }

    fun getApiData() {
        val call = getApiConnector().getOkhttpIssueData()
        call.enqueue(object : Callback<ArrayList<IssuesItem>> {
            override fun onResponse(
                call: Call<ArrayList<IssuesItem>>,
                response: Response<ArrayList<IssuesItem>>
            ) {
                issuesList.value = response.body()
                Log.d(TAG, "Issue apiData ${response.body()}   ")
            }

            override fun onFailure(call: Call<ArrayList<IssuesItem>>, t: Throwable) {
                Log.d(TAG, "MainRepository issueApi onFailure: ${t.cause} || ${t.stackTrace}")
            }
        })
    }

    fun getUserNumber(position: Int): Int = databaseGetter.IssuesDao().getUserName(position)

    fun cacheApiData(dataList: List<IssuesEntity>) =
        databaseGetter.IssuesDao().cacheIssueData(dataList)

    fun getCachedIssuesData(): List<IssuesEntity> = databaseGetter.IssuesDao().getCachedData()

    fun getTableRowCount(): Int = databaseGetter.IssuesDao().getTableSize()

    fun deleteCachedIssuesData() = databaseGetter.IssuesDao().deleteCachedData()
}