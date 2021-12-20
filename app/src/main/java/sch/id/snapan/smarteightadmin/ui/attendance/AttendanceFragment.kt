package sch.id.snapan.smarteightadmin.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.ui.snackbar
import sch.id.snapan.smarteightadmin.adapter.ListAttendanceAdapter
import sch.id.snapan.smarteightadmin.databinding.FragmentAttendanceBinding
import sch.id.snapan.smarteightadmin.other.EventObserver
import javax.inject.Inject

@AndroidEntryPoint
class AttendanceFragment : Fragment() {

    @Inject
    lateinit var glide: RequestManager

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private lateinit var attendanceAdapter: ListAttendanceAdapter
    private val viewModel: AttendanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        viewModel.getListAnnouncement()

        attendanceAdapter = ListAttendanceAdapter(glide)
        setupRecyclerView()
    }

    private fun subscribeToObservers() {
        viewModel.getListAttendanceStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBarAttendance.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.progressBarAttendance.visibility = View.VISIBLE
            }
        ) {list ->
            binding.progressBarAttendance.visibility = View.INVISIBLE
            attendanceAdapter.attendances = list
            if (!list.isNullOrEmpty()) {
                setupRecyclerView()
            } else {
                stateDataEmpty()
            }
        })

    }

    private fun stateDataEmpty() {
        binding.rvAttendances.visibility = View.GONE
        binding.tvDataEmpty.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() = binding.rvAttendances.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = attendanceAdapter
        setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListAnnouncement()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}