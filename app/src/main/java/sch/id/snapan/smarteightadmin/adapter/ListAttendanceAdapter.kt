package sch.id.snapan.smarteightadmin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import sch.id.snapan.smarteightadmin.data.entity.Attendance
import sch.id.snapan.smarteightadmin.databinding.ListItemAttendanceBinding
import javax.inject.Inject

class ListAttendanceAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ListAttendanceAdapter.AttendanceViewHolder>() {

    private val diffCallback = object: DiffUtil.ItemCallback<Attendance>() {
        override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var attendances: List<Attendance>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAttendanceAdapter.AttendanceViewHolder {
        val listBinding = ListItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(listBinding)
    }

    override fun onBindViewHolder(
        holder: ListAttendanceAdapter.AttendanceViewHolder,
        position: Int
    ) {
        val list = attendances[position]
        holder.bind(list)
    }

    override fun getItemCount(): Int = attendances.size

    inner class AttendanceViewHolder(private val binding: ListItemAttendanceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: Attendance) {
            with(binding) {
                tvNameAttendance.text = attendance.name
                tvStatusAttendance.text = attendance.status
                tvActivityAttendance.text = attendance.activity
                tvDateAttendance.text = attendance.date
                tvTimeAttendance.text = attendance.time
                glide.load(attendance.imageUrl).into(ivItemAttendance)
            }
        }
    }
}