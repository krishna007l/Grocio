package mrkinfotech.Grocio.ui.login


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    fun onMyButtonClick(view: View){
        setContentView(R.layout.fragment_sign_up)
    }
    fun onMButtonClick(view: View){
        setContentView(R.layout.fragment_login)
    }
}