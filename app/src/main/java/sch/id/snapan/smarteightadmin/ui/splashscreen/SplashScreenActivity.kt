package sch.id.snapan.smarteightadmin.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sch.id.snapan.smarteightadmin.databinding.ActivitySplashScreenBinding
import sch.id.snapan.smarteightadmin.ui.auth.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageView = binding.ivSplashScreen
        val title = binding.tvTitle
        val subtitle = binding.tvSubtitle

        imageView.alpha = 0f
        imageView.animate().setDuration(3000).alpha(1f).withEndAction { intentToAuthActivity() }
        title.alpha = 0f
        title.animate().setDuration(3000).alpha(1f).withEndAction { intentToAuthActivity() }
        subtitle.alpha = 0f
        subtitle.animate().setDuration(3000).alpha(1f).withEndAction { intentToAuthActivity() }

    }

    private fun intentToAuthActivity() {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startActivity(intent)
        finish()
    }
}