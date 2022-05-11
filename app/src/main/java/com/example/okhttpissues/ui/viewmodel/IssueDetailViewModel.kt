package com.example.okhttpissues.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.okhttpissues.data.api_model.CommentDataItem
import com.example.okhttpissues.data.database.DatabaseGetter
import com.example.okhttpissues.data.database_entities.CommentDataEntity
import com.example.okhttpissues.data.repository.CommentDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IssueDetailViewModel(databaseCall: DatabaseGetter) : ViewModel() {
    private val mainRepo = CommentDataRepository(databaseCall)

    fun getCommentApiData(commentUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.getCommentApiData(commentUrl)
        }
    }

    fun getDataUsingIssueId(id: Int): MutableLiveData<List<CommentDataEntity>> {
        val data = MutableLiveData<List<CommentDataEntity>>()
        viewModelScope.launch(Dispatchers.IO) {
            data.postValue(mainRepo.getDataUsingIssueId(id))
        }
        return data
    }

    fun cacheApiData(dataList: List<CommentDataEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.cacheApiData(dataList)
        }
    }

    fun getCommentList(): MutableLiveData<ArrayList<CommentDataItem>> {
        return mainRepo.commentList
    }

    fun getCommentTblRowCount(): MutableLiveData<Int> {
        val count = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            count.postValue(mainRepo.getCommentTableRowCount())
        }
        return count
    }

    fun getAllCommentUrl(): MutableLiveData<List<String>> {
        val urls = MutableLiveData<List<String>>()
        viewModelScope.launch(Dispatchers.IO) {
            urls.postValue(mainRepo.getCommentUrls())
        }
        return urls
    }

    fun getSingleUrl(id: Int): MutableLiveData<String> {
        val url = MutableLiveData<String>()
        viewModelScope.launch(Dispatchers.IO) {
            url.postValue(mainRepo.getSingleUrl(id))
        }
        return url
    }

    fun getAllIssueId(): MutableLiveData<List<Int>> {
        val ids = MutableLiveData<List<Int>>()
        viewModelScope.launch(Dispatchers.IO) {
            ids.postValue(mainRepo.getAllIssueId())
        }
        return ids
    }

    fun deleteCommentCachedData() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.deleteCachedCommentData()
        }
    }
}

class IssueDetailVMFactory(private val databaseCall: DatabaseGetter) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IssueDetailViewModel::class.java)) {
            return IssueDetailViewModel(databaseCall) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}