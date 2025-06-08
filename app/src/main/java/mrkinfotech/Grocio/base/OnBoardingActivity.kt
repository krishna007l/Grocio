package mrkinfotech.Grocio.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mrkinfotech.Grocio.databinding.ActivityOnBoardingBinding

import mrkinfotech.Grocio.login.LoginActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGetStarted.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}