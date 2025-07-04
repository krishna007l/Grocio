package mrkinfotech.Grocio.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shopify.utils.PreferenceHelper
import mrkinfotech.Grocio.databinding.ActivityOnBoardingBinding

import mrkinfotech.Grocio.ui.login.LoginActivity

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGetStarted.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            PreferenceHelper.setonbody(this,true)
        }
    }
}