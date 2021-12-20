package sch.id.snapan.smarteightadmin.ui.announcement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.ui.snackbar
import sch.id.snapan.smarteightadmin.R
import sch.id.snapan.smarteightadmin.adapter.ListAnnouncementAdapter
import sch.id.snapan.smarteightadmin.databinding.FragmentAnnouncementBinding
import sch.id.snapan.smarteightadmin.other.EventObserver


@AndroidEntryPoint
class AnnouncementFragment : Fragment() {

    private var _binding: FragmentAnnouncementBinding? = null
    private val binding get() = _binding!!

    private lateinit var announcementAdapter: ListAnnouncementAdapter
    private val viewModel: AnnouncementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnouncementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        viewModel.getListAnnouncement()

        announcementAdapter = ListAnnouncementAdapter()
        setupRecyclerView()

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                101 -> { snackbar(getString(R.string.message_add)) }
            }
        }
        binding.fabAddAnnouncement.setOnClickListener {
            val intent = Intent(requireActivity(), AnnouncementAddUpdateActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun subscribeToObservers() {
        viewModel.getListAnnouncementStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBarAnnouncement.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.progressBarAnnouncement.visibility = View.VISIBLE
            }
        ) {list ->
            binding.progressBarAnnouncement.visibility = View.INVISIBLE
            announcementAdapter.announcements = list
            if (list.isNotEmpty()) {
                setupRecyclerView()
            } else {
                stateDataEmpty()
            }
        })

    }

    private fun stateDataEmpty() {
        binding.rvAnnouncements.visibility = View.GONE
        binding.tvDataEmpty.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() = binding.rvAnnouncements.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = announcementAdapter
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