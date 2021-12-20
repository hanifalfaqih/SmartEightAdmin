package sch.id.snapan.smarteightadmin.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.ui.snackbar
import sch.id.snapan.smarteightadmin.adapter.ListUserAdapter
import sch.id.snapan.smarteightadmin.databinding.FragmentUserBinding
import sch.id.snapan.smarteightadmin.other.EventObserver

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: ListUserAdapter
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        viewModel.getListAnnouncement()

        userAdapter = ListUserAdapter()
        setupRecyclerView()
    }

    private fun subscribeToObservers() {
        viewModel.getListUserStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBarUser.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.progressBarUser.visibility = View.VISIBLE
            }
        ) {list ->
            binding.progressBarUser.visibility = View.INVISIBLE
            userAdapter.users = list
            if (!list.isNullOrEmpty()) {
                setupRecyclerView()
            } else {
                stateDataEmpty()
            }
        })

    }

    private fun stateDataEmpty() {
        binding.rvUsers.visibility = View.GONE
        binding.tvDataEmpty.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() = binding.rvUsers.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = userAdapter
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