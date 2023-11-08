package com.example.listusergithub

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listusergithub.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        val favoriteMenu = menu.findItem(R.id.favorite)
        val settingMenu = menu.findItem(R.id.setting)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }


        mainViewModel.userList.observe(this) {
            setUserList(it)
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.setSearchUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        favoriteMenu.setOnMenuItemClickListener {

            val moveIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
            startActivity(moveIntent)

            return@setOnMenuItemClickListener true
        }

        settingMenu.setOnMenuItemClickListener {

            val moveIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(moveIntent)

            return@setOnMenuItemClickListener true
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun setUserList(users: List<User>) {
        val adapter = UserAdapter(users)
        binding.rvUser.adapter = adapter
        binding.rvUser.setHasFixedSize(true)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}