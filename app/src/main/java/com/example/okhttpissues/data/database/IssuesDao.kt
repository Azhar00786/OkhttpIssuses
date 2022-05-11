package com.example.okhttpissues.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.okhttpissues.data.api_model.Issues
import com.example.okhttpissues.data.database_entities.IssuesEntity

@Dao
public interface IssuesDao {
    @Insert
    fun cacheIssueData(issueData: List<IssuesEntity>)

    @Query("select * from IssuesData")
    fun getCachedData(): List<IssuesEntity>

    @Query("select count(*) from IssuesData")
    fun getTableSize(): Int

    @Query("delete from IssuesData")
    fun deleteCachedData()

    @Query("select userNumber from IssuesData where id = :position")
    fun getUserName(position: Int): Int

    @Query("select commentsUrl from IssuesData")
    fun getAllCommentUrl(): List<String>

    @Query("select id from IssuesData")
    fun getAllIssueId(): List<Int>

    @Query("select commentsUrl from IssuesData where id = :id")
    fun getSingleCommentUrl(id: Int): String
}