package com.example.okhttpissues.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.okhttpissues.data.database_entities.CommentDataEntity

@Dao
interface CommentDao {
    @Insert
    fun cacheCommentData(commentData: List<CommentDataEntity>)

    @Query("select count(*) from CommentData")
    fun getCommentTableSize(): Int

    @Query("delete from CommentData")
    fun deleteCachedCommentData()

    @Query("select * from CommentData where issueId = :position")
    fun getDataByIssueId(position: Int): List<CommentDataEntity>

    @Query("delete from CommentData")
    fun deleteCommentCachedData()
}