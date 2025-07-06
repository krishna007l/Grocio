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
            val userEmail = binding.loginEmail.text.toString()
            val userPassword = binding.loginPassword.text.toString()
            val auth = FirebaseAuth.getInstance()
            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener { task ->
                        auth.signInWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    CustomDialog.showTostMessage(requireContext(),"Loding...")
                                    PreferenceHelper.setUserEmail(requireContext(),userEmail)
                                    startActivity(Intent(requireContext(), HomeMainActivity::class.java))
                                } else {
                                    CustomDialog.showTostMessage(requireContext(),"Enter Valid Email & Password  ")
                                }
                            }
                    }
            }else{
                CustomDialog.showTostMessage(requireContext(),"Enter Email & Password ")
            }
        }
    }
}