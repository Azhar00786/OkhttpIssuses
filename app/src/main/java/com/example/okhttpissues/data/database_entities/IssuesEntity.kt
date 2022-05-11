package com.example.okhttpissues.data.database_entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "IssuesData")
data class IssuesEntity(
    val title: String,
    val issueDescription: String?,
    val username: String,
    val userAvatar: String,
    val updatedOn: String,
    val commentsUrl: String,
    val commentCount: Int,
    val userNumber: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}