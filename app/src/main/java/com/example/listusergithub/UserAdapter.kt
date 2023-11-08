package com.example.listusergithub
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listusergithub.databinding.ItemUserBinding

class UserAdapter(private val listUser : List<User>) : RecyclerView.Adapter<UserAdapter.ListUserViewHolder>() {

    inner class ListUserViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val layout = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListUserViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        val username = listUser[position].login
        val avatarURL = listUser[position].avatarUrl
        holder.binding.tvUser.text = username
        Glide.with(holder.itemView.context)
            .load(avatarURL)
            .into(holder.binding.ivUser)
        holder.binding.cardView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailUserActivity::class.java)
            detailIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
            holder.itemView.context.startActivity(detailIntent)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}