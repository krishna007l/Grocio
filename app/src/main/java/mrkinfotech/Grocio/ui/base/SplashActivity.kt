package mrkinfotech.Grocio.ui.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shopify.utils.PreferenceHelper
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.ui.home.HomeMainActivity
import mrkinfotech.Grocio.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
                if(PreferenceHelper.getonbody(this))
                {
                    if (PreferenceHelper.isUserLoggedIn(this)){
                    startActivity(Intent(this,HomeMainActivity::class.java))
                }
                else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            else {
                startActivity(Intent(this, OnBoardingActivity::class.java))
            }
            finish() // Closes splash activity so user can't return to it
        }, 2000)
    }
}

