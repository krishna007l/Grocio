package mrkinfotech.Grocio.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.shopify.utils.PreferenceHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import mrkinfotech.Grocio.databinding.FragmentLoginBinding
import mrkinfotech.Grocio.ui.home.HomeFragment
import mrkinfotech.Grocio.ui.home.HomeMainActivity




class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        binding.loginbutton.setOnClickListener {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "signInWithEmail:success")
                                    val user = auth.currentUser
                                    startActivity(
                                        Intent(
                                            requireContext(),
                                            HomeMainActivity::class.java
                                        )
                                    )
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        requireContext(),
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
            }
        }
    }
}
