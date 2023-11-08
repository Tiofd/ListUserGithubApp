package com.example.listusergithub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listusergithub.databinding.FragmentUserBinding


class UserFollowFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private lateinit var followViewModel: FollowViewModel
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0
        var username = ""

        followViewModel = ViewModelProvider(requireActivity())[FollowViewModel::class.java]

        followViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }
        if (position == 1){
            followViewModel.getFollowers(username)
            followViewModel.userFollowers.observe(viewLifecycleOwner){
                setUserFollow(it)
            }
        }
        else{
            followViewModel.getFollowing(username)
            followViewModel.userFollowing.observe(viewLifecycleOwner){
                setUserFollow(it)
            }
        }
    }
    private fun setUserFollow(users: List<User>) {

        val adapter = UserAdapter(users)
        binding.rvFollow.adapter = adapter
        binding.rvFollow.setHasFixedSize(true)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}