package mrkinfotech.Grocio.ui.login


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ActivityLoginBinding
import mrkinfotech.Grocio.databinding.FragmentLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun onMyButtonClick(view: View){
        setContentView(R.layout.fragment_sign_up)
    }
    fun onMButtonClick(view: View){
        setContentView(R.layout.fragment_login)
    }
}