package com.example.listusergithub

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): ViewModel() {
    private val _detailUsers = MutableLiveData<DetailUserResponse>()
    val detailUsers: LiveData<DetailUserResponse> = _detailUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _favoriteUserRepository: FavoriteRepository = FavoriteRepository(application)

    fun setDetailUsers(username: String?){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object: Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUsers.postValue(response.body())
                    }
                } else {
                    Log.e("UserViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e("UserViewModel", "Error: ${t.message}")
            }

        })
    }
    fun insertToFavorites(favoriteUser : FavoriteUser){
        _favoriteUserRepository.addFavoriteUser(favoriteUser)
    }

    fun deleteToFavorites(id : Int){
        _favoriteUserRepository.removeFavoriteUser(id)
    }

    suspend fun getFavoriteUser(id : Int) : Int{
        return _favoriteUserRepository.getFavoriteUser(id)
    }
}