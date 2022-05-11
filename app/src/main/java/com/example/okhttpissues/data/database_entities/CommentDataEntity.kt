package com.example.okhttpissues.data.database_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CommentData")
class CommentDataEntity(
    val issueId: Int?,
    val body: String?,
    val userName: String?
) {
    @PrimaryKey(autoGenerate = true)
    var idComment: Int? = 0
}