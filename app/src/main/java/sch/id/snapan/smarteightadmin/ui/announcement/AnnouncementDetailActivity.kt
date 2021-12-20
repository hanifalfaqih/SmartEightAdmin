package sch.id.snapan.smarteightadmin.ui.announcement

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.ui.snackbar
import sch.id.snapan.smarteightadmin.R
import sch.id.snapan.smarteightadmin.data.entity.Announcement
import sch.id.snapan.smarteightadmin.databinding.ActivityAnnouncementDetailBinding
import sch.id.snapan.smarteightadmin.other.EventObserver
import javax.inject.Inject

@AndroidEntryPoint
class AnnouncementDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var glide: RequestManager

    private var _binding: ActivityAnnouncementDetailBinding? = null
    private val binding get() = _binding!!
    private var announcement: Announcement? = null
    private lateinit var announcementId: String
    private lateinit var announcementUid: String
    private val authUid = Firebase.auth.uid
    private val viewModel: AnnouncementViewModel by viewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val EXTRA_ANNOUNCEMENT = "extra_announcement"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAnnouncementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_announcement)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        announcement = intent.getParcelableExtra(EXTRA_ANNOUNCEMENT)
        announcementId = announcement?.id.toString()
        announcementUid = announcement?.uid.toString()
        viewModel.getDetailAnnouncement(announcementId)

        subscribeToObserveDetail()
        subscribeToObserverDelete()
        handleOnBackPressedCallback()

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                201 -> {
                    snackbar(getString(R.string.message_update))
                }
            }
        }

        binding.btnEdit.setOnClickListener { handleEdit() }
        binding.btnDelete.setOnClickListener { handleDelete() }
    }

    private fun handleOnBackPressedCallback() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { finish() }
        }
        this.onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun handleEdit() {
        if (announcementUid == authUid) {
            val intent = Intent(this, AnnouncementAddUpdateActivity::class.java)
            intent.putExtra(AnnouncementAddUpdateActivity.EXTRA_ANNOUNCEMENT, announcement)
            activityResultLauncher.launch(intent)
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.message_edit_warning),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun handleDelete() {
        if (announcementUid == authUid) {
            showAlertDialog()
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.message_delete_warning),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun subscribeToObserveDetail() {
        viewModel.getDetailAnnouncementStatus.observe(this, EventObserver(
            onError = {
                binding.progressBarDetail.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.progressBarDetail.visibility = View.VISIBLE
            }
        ) { announcement ->
            binding.progressBarDetail.visibility = View.INVISIBLE
            if (announcement?.imageUrl != null) {
                glide.load(announcement.imageUrl).into(binding.ivPostAnnouncement)
            } else {
                binding.ivPostAnnouncement.visibility = View.GONE
            }
            binding.tvDateDetailAnnouncement.text = announcement?.date
            binding.tvTitleDetailAnnouncement.text = announcement?.title
            binding.tvMessageDetailAnnouncement.text= announcement?.message
        })
    }

    private fun subscribeToObserverDelete() {
        viewModel.deleteAnnouncementStatus.observe(this, EventObserver(
            onError = {
                binding.progressBarDetail.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = {
                binding.progressBarDetail.visibility = View.VISIBLE
            }
        ) {
            binding.progressBarDetail.visibility = View.INVISIBLE
            finish()
        })
    }

    private fun showAlertDialog() {
        val dialogTitle = getString(R.string.delete)
        val dialogMessage = getString(R.string.message_delete_alert)
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                announcement?.let { viewModel.deleteAnnouncement(it) }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        alertDialogBuilder.create().show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDetailAnnouncement(announcementId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}