package mrkinfotech.Grocio.ui.login


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mrkinfotech.Grocio.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}