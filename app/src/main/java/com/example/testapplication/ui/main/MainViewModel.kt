package com.example.testapplication.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.model.Response
import com.example.testapplication.repository.MainRepository
import com.example.testapplication.utils.API_KEY
import com.example.testapplication.utils.NetworkHelper
import com.example.testapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel(){

    /*private val _users = MutableLiveData<Resource<List<User>>>()

    val users: LiveData<Resource<List<User>>>
        get() = _users
*/
    init {
        fetchUsers()
    }

    fun fetchUsers() {
        /*viewModelScope.launch {
            _users.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUsers().let {
                    if (it.isSuccessful) {
                        _users.postValue(Resource.success(it.body()))
                    } else _users.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _users.postValue(Resource.error("No internet connection", null))
        }*/
        mainRepository.searchForecast("London" , API_KEY)
            .subscribeOn(Schedulers.io())
            // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( getSingleObserver())

    }

    private fun getSingleObserver(): SingleObserver<Response?>{
        return object : SingleObserver<Response?> {
            override fun onSubscribe(d: Disposable) {
                Log.d(
                    "TAG",
                    " onSubscribe : " + d.isDisposed
                )
            }

            override fun onSuccess(value: Response) {
                Log.d(
                    "TAG",
                    " onNext value : $value"
                )
            }

            override fun onError(e: Throwable) {
                Log.d(
                    "TAG",
                    " onError : " + e.message
                )
            }
        }
    }

}
