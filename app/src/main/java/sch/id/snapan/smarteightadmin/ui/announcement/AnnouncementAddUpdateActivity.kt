package sch.id.snapan.smarteightadmin.ui.announcement

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.utils.DateHelper
import sch.id.snapan.smarteightadmin.R
import sch.id.snapan.smarteightadmin.data.entity.Announcement
import sch.id.snapan.smarteightadmin.databinding.ActivityAnnouncementAddUpdateBinding
import sch.id.snapan.smarteightadmin.other.EventObserver

@AndroidEntryPoint
class AnnouncementAddUpdateActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ANNOUNCEMENT = "extra_announcement"
        const val RESULT_ADD = 101
        const val RESULT_EDIT = 201
    }

    private var _binding: ActivityAnnouncementAddUpdateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnnouncementViewModel by viewModels()
    private var currentImageUri: Uri? = null
    private val imagePostAnnouncement: ImageView by lazy {
        binding.ivPostAnnouncement
    }

    private val galeryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                currentImageUri = it
                imagePostAnnouncement.setImageURI(currentImageUri)
            }
        }
    private var announcement: Announcement? = null
    private var isEdit = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAnnouncementAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeToObserverAdd()
        subscribeToObserverEdit()
        handleOnBackPressedCallback()

        announcement = intent.getParcelableExtra(EXTRA_ANNOUNCEMENT)
        if (announcement != null) isEdit = true else announcement = Announcement()

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = getString(R.string.update)
            btnTitle = getString(R.string.update)
            if (announcement != null) {
                announcement?.let {
                    binding.etTitleAnnouncement.setText(it.title)
                    binding.etBodyAnnouncement.setText(it.message)
                }
            }
        } else {
            actionBarTitle = getString(R.string.add)
            btnTitle = getString(R.string.add)
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnAddUpdate.text = btnTitle
        binding.btnAddUpdate.setOnClickListener {
            handleAddUpdate()
        }
        binding.btnAddImage.setOnClickListener {
            requestSelectImage()
        }
    }

    private fun handleOnBackPressedCallback() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { showAlertDialog() }
        }
        this.onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleAddUpdate() {
        val title = binding.etTitleAnnouncement.text.toString()
        val message = binding.etBodyAnnouncement.text.toString()

        val map = mutableMapOf<String, Any>()
        map["title"] = title
        map["message"] = message
        map["date"] = DateHelper.getCurrentDate()

        if (isEdit) {
            viewModel.editAnnouncement(oldAnnouncement = announcement!!, newAnnouncement = map)
            val intent = Intent()
            intent.putExtra(EXTRA_ANNOUNCEMENT, announcement)
            setResult(RESULT_EDIT)
        } else {
            if (currentImageUri != null) {
                viewModel.addAnnouncement(currentImageUri, title, message)
            } else {
                viewModel.addAnnouncement(null, title, message)
            }
            setResult(RESULT_ADD)
        }
    }

    private fun requestSelectImage() = galeryLauncher.launch("image/*")

    private fun subscribeToObserverEdit() {
        viewModel.editAnnouncementStatus.observe(this, EventObserver(
            onError = {
                binding.progressBarAnnouncement.visibility = View.INVISIBLE
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            },
            onLoading = {
                binding.progressBarAnnouncement.visibility = View.VISIBLE
            }
        ) {
            binding.progressBarAnnouncement.visibility = View.INVISIBLE
            finish()
        })
    }

    private fun subscribeToObserverAdd() {
        viewModel.addAnnouncementStatus.observe(this, EventObserver(
            onError = {
                binding.progressBarAnnouncement.visibility = View.INVISIBLE
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            },
            onLoading = {
                binding.progressBarAnnouncement.visibility = View.VISIBLE
            }
        ) {
            binding.progressBarAnnouncement.visibility = View.INVISIBLE
            finish()

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val dialogTitle = getString(R.string.cancel)
        val dialogMessage = getString(R.string.message_cancel)
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.cancel)) { _, _ ->
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        alertDialogBuilder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}