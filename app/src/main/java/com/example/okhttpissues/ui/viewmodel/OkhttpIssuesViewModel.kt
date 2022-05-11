package com.example.okhttpissues.ui.viewmodel

import androidx.lifecycle.*
import com.example.okhttpissues.data.api.ApiConnectorSingelton
import com.example.okhttpissues.data.api_model.IssuesItem
import com.example.okhttpissues.data.database.DatabaseGetter
import com.example.okhttpissues.data.database_entities.IssuesEntity
import com.example.okhttpissues.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OkhttpIssuesViewModel(apiCall: ApiConnectorSingelton, databaseGetter: DatabaseGetter) :
    ViewModel() {
    private val mainRepo = MainRepository(apiCall, databaseGetter)

    fun cacheApiData(dataList: List<IssuesEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.cacheApiData(dataList)
        }
    }

    fun getApiData(): MutableLiveData<ArrayList<IssuesItem>> {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.getApiData()
        }
        return mainRepo.issuesList
    }

    fun getCachedData(): LiveData<List<IssuesEntity>> {
        val data = MutableLiveData<List<IssuesEntity>>()
        viewModelScope.launch(Dispatchers.IO) {
            data.postValue(mainRepo.getCachedIssuesData())
        }
        return data
    }

    fun getTableRowCount(): MutableLiveData<Int> {
        val count = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            count.postValue(mainRepo.getTableRowCount())
        }
        return count
    }

    fun getUserNumber(position: Int): MutableLiveData<Int> {
        var userNumberLD = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            userNumberLD.postValue(mainRepo.getUserNumber(position))
        }
        return userNumberLD
    }

    fun deleteCachedData() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.deleteCachedIssuesData()
        }
    }
}

class OkhttpIssuesVMFactory(
    private val apiCall: ApiConnectorSingelton,
    private val databaseCall: DatabaseGetter
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OkhttpIssuesViewModel::class.java)) {
            return OkhttpIssuesViewModel(apiCall, databaseCall) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}