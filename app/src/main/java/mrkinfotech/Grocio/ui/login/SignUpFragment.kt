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
import mrkinfotech.Grocio.databinding.FragmentSingUpBinding
import mrkinfotech.Grocio.utils.CustomDialog


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSingUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            findNavController().navigate(R.id.LoginFragment)
        }

        binding.SinupButton.setOnClickListener {
            val username = binding.editusername.text.toString()
            val email = binding.SignEmail.text.toString()
            val password = binding.SignPassword.text.toString()
            var auth = FirebaseAuth.getInstance()
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            findNavController().navigate(R.id.LoginFragment)
                        } else {
                            CustomDialog.showTostMessage(requireContext(),"enter vaild email and password")
                        }
                    }
            }else{
                CustomDialog.showTostMessage(requireContext(),"Enter UserName And Password")
            }
        }
    }
}
