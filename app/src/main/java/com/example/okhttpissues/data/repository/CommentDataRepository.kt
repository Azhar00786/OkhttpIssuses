package com.example.okhttpissues.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.okhttpissues.data.api.ApiConnectorTwo
import com.example.okhttpissues.data.api.ApiServiceTwoInterface
import com.example.okhttpissues.data.api_model.CommentDataItem
import com.example.okhttpissues.data.database.DatabaseGetter
import com.example.okhttpissues.data.database_entities.CommentDataEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentDataRepository(private val databaseGetter: DatabaseGetter) {
    var commentList: MutableLiveData<ArrayList<CommentDataItem>> = MutableLiveData()

    companion object {
        const val TAG = "CommentDataRepository"
    }

    /*
    *   Methods for Second Screen
    */

    private fun getCommentDataApiConnector(commentUrl: String): ApiServiceTwoInterface {
        val apiCallerTwo = ApiConnectorTwo(commentUrl)
        val retrofitInstanceTwo = apiCallerTwo.getRetrofitInstance()
        return retrofitInstanceTwo.create(ApiServiceTwoInterface::class.java)
    }

    fun getCommentApiData(commentUrl: String) {
        val call = getCommentDataApiConnector(commentUrl).getCommentsData()
        call.enqueue(object : Callback<ArrayList<CommentDataItem>> {
            override fun onResponse(
                call: Call<ArrayList<CommentDataItem>>,
                response: Response<ArrayList<CommentDataItem>>
            ) {
                commentList.value = response.body()
                Log.d(TAG, "Comment apiData ${response.body()}")
            }

            override fun onFailure(call: Call<ArrayList<CommentDataItem>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.cause} || ${t.stackTrace}")
            }
        })
    }

    fun cacheApiData(dataList: List<CommentDataEntity>) =
        databaseGetter.CommentDao().cacheCommentData(dataList)

    fun getCommentUrls(): List<String> = databaseGetter.IssuesDao().getAllCommentUrl()

    fun getSingleUrl(id: Int): String = databaseGetter.IssuesDao().getSingleCommentUrl(id)

    fun getAllIssueId(): List<Int> = databaseGetter.IssuesDao().getAllIssueId()

    fun getCommentTableRowCount(): Int = databaseGetter.CommentDao().getCommentTableSize()

    fun deleteCachedCommentData() = databaseGetter.CommentDao().deleteCommentCachedData()

    fun getDataUsingIssueId(id: Int): List<CommentDataEntity> =
        databaseGetter.CommentDao().getDataByIssueId(id)
}