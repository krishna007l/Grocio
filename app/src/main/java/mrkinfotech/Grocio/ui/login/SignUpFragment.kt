package mrkinfotech.Grocio.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentSignUpBinding
import mrkinfotech.Grocio.utils.CustomDialog


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            findNavController().navigate(R.id.nav_host_fragment_login_content_main)
        }

        binding.SinupButton.setOnClickListener {
            val email = binding.SignEmail.text.toString()
            val cpassword = binding.SignPassword.text.toString()
            var auth = FirebaseAuth.getInstance()
            if (email.isNotEmpty() && cpassword.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, cpassword)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            findNavController().navigate(R.id.LoginFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
            }else{
                CustomDialog.showTostMessage(requireContext(),"Enter UserName And Password")
            }
        }
    }
}
