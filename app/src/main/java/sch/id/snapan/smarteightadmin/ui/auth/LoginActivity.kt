package sch.id.snapan.smarteightadmin.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.ui.snackbar
import sch.id.snapan.smarteight.utils.FieldValidators
import sch.id.snapan.smarteightadmin.R
import sch.id.snapan.smarteightadmin.databinding.ActivityLoginBinding
import sch.id.snapan.smarteightadmin.other.EventObserver
import sch.id.snapan.smarteightadmin.ui.home.HomeActivity

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (FirebaseAuth.getInstance().currentUser != null) {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        subscribeToStateObservers()

        binding.btnLogin.setOnClickListener {
            setupTextFieldValidation()
            if (isValidate()) {
                viewModel.loginUser(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        }

    }

    private fun subscribeToStateObservers() {
        viewModel.loginStatusUser.observe(this, EventObserver(
            onError = {
                binding.progressBarLogin.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = { binding.progressBarLogin.visibility = View.VISIBLE }
        ) {
            binding.progressBarLogin.visibility = View.INVISIBLE
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        })
    }

    private fun isValidate() = validateEmail() && validatePassword()

    private fun setupTextFieldValidation() {
        binding.etEmail.addTextChangedListener(TextFieldValidation(binding.etEmail))
        binding.etPassword.addTextChangedListener(TextFieldValidation(binding.etPassword))
    }

    private fun validateEmail(): Boolean {
        if (!FieldValidators.isFormatEmailValid(binding.etEmail.text.toString())) {
            binding.tilEmail.error = getString(R.string.error_et_email_format_wrong)
            return false
        } else {
            binding.tilEmail.error = null
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.etPassword.text.toString().length <= 5) {
            binding.tilPassword.error = getString(R.string.error_et_password_less_than_5)
            return false
        } else {
            binding.tilPassword.error = null
        }
        return true
    }

    inner class TextFieldValidation(private val view: View): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when (view.id) {
                R.id.et_email -> { validateEmail() }
                R.id.et_password -> { validatePassword() }
            }
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}