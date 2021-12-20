package sch.id.snapan.smarteightadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sch.id.snapan.smarteightadmin.data.entity.User
import sch.id.snapan.smarteightadmin.databinding.ListItemUserBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    private val diffCallback = object: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var users: List<User>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserAdapter.UserViewHolder {
        val listBinding = ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(listBinding)
    }

    override fun onBindViewHolder(
        holder: ListUserAdapter.UserViewHolder,
        position: Int
    ) {
        val list = users[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(private val binding: ListItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvNameUser.text = user.name
                tvEmailUser.text = user.email
                tvStatusUser.text = user.status
            }
        }
    }
}