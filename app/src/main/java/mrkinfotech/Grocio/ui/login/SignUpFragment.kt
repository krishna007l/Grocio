package mrkinfotech.Grocio.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentSignUpBinding
import mrkinfotech.Grocio.ui.data.ProfileData
import mrkinfotech.Grocio.ui.home.HomeMainActivity
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.PreferenceHelper

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignUp.setOnClickListener {
            val userName = binding.editTextUserName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val pass = binding.editTextPassword.text.toString().trim()
            if (userName.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        PreferenceHelper.saveProfileData(
                            requireContext(),
                            ProfileData(name = userName, email = email)
                        )
                        CustomDialog.showToast(requireContext(), getString(R.string.str_signup_success))
                        startActivity(Intent(requireContext(), HomeMainActivity::class.java))
                        requireActivity().finish()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                CustomDialog.showToast(requireContext(), getString(R.string.str_empty_fields_not_allowed))
            }
        }
        binding.buttonLogin.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
