package mrkinfotech.Grocio.ui.login

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.shopify.utils.PreferenceHelper
import com.google.firebase.auth.FirebaseAuth
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentLoginBinding
import mrkinfotech.Grocio.ui.home.HomeFragment
import mrkinfotech.Grocio.ui.home.HomeMainActivity
import mrkinfotech.Grocio.utils.CustomDialog


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signin.setOnClickListener {
            findNavController().navigate(R.id.SignUpFragment)
        }
        binding.loginbutton.setOnClickListener {
            val userEmail = binding.editTextEmail.text.toString()
            val Password = binding.editTextPassword.text.toString()
            val auth = FirebaseAuth.getInstance()
            if (userEmail.isNotEmpty() && Password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(userEmail, Password)
                    .addOnCompleteListener { task ->
                        auth.signInWithEmailAndPassword(userEmail, Password)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    CustomDialog.showTostMessage(requireContext(),"sucees full")

                                } else {
                                    CustomDialog.showTostMessage(requireContext(),"enter valid ")
                                }
                            }
                    }
            }else{
                startActivity(Intent(requireContext(), HomeMainActivity::class.java))
            }
        }
    }
}