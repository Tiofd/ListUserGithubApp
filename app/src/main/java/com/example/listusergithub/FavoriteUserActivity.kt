package com.example.listusergithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listusergithub.databinding.ActivityFavoriteUserBinding

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFavoriteUserBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites User"

        favoriteViewModel = obtainViewModel(this@FavoriteUserActivity)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager

        getAllFavoriteUsers()

    }

    private fun getAllFavoriteUsers(){
        showLoading(true)
        favoriteViewModel.getAllFavoriteUsers().observe(this) {
            setFollowList(it)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setFollowList(favoriteUserList: List<FavoriteUser>) {
        val adapter = FavoriteAdapter(favoriteUserList)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.setHasFixedSize(true)
    }


    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}