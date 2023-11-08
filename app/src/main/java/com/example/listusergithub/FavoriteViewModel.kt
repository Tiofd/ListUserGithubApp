package com.example.listusergithub

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class FavoriteViewModel (application: Application) : ViewModel() {

    private val _favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavoriteUsers() : LiveData<List<FavoriteUser>> {

        return _favoriteRepository.getAllFavoriteUser()

    }

}