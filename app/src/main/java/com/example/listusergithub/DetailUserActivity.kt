package com.example.listusergithub


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.listusergithub.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailViewModel
    private var isChecked: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailViewModel = getViewModel(this@DetailUserActivity)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.setDetailUsers(username)

        detailViewModel.detailUsers.observe(this){
            getUserData(it)
            addOrDeleteFavorite(it)
        }
        val userSectionPagerAdapter = UserSectionPagerAdapter(this)
        userSectionPagerAdapter.username = username.toString()
        val viewPager2 : ViewPager2 = binding.viewPager
        viewPager2.adapter = userSectionPagerAdapter

        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager2) {
                tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getUserData (user : DetailUserResponse){
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowers.text = resources.getString(R.string.followers, user.followers)
            tvFollowing.text = resources.getString(R.string.following, user.following)
            Glide.with(this@DetailUserActivity)
                .load(user.avatarUrl)
                .into(ivAvatar)
        }
    }
    private fun addOrDeleteFavorite(user: DetailUserResponse) {
        CoroutineScope(Dispatchers.IO).launch {

            if (detailViewModel.getFavoriteUser(user.id) != 0) {
                isChecked = true
                binding.addFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailUserActivity,
                        R.drawable.baseline_favorite_24
                    )
                )
            } else {
                isChecked = false
                binding.addFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailUserActivity,
                        R.drawable.baseline_favorite_border_24
                    )
                )
            }
        }

        binding.addFav.setOnClickListener {

            if (isChecked == true) {

                isChecked = false
                val favoriteUser = FavoriteUser(user.id, user.login, user.avatarUrl)
                detailViewModel.deleteToFavorites(favoriteUser.id)
                binding.addFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.baseline_favorite_border_24
                    )
                )

            } else {

                isChecked = true
                val favoriteUser = FavoriteUser(user.id, user.login, user.avatarUrl)
                detailViewModel.insertToFavorites(favoriteUser)
                binding.addFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.baseline_favorite_24
                    )
                )
            }

        }

    }
    private fun getViewModel(activity: AppCompatActivity): DetailViewModel {
        val factoryVM = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factoryVM)[DetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_USERNAME = "extra_user"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}