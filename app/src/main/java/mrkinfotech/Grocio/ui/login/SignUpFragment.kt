package mrkinfotech.Grocio.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentSignUpBinding
import mrkinfotech.Grocio.ui.data.ProfileData
import mrkinfotech.Grocio.ui.home.HomeMainActivity
import mrkinfotech.Grocio.utils.AppConstant
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.PreferenceHelper

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val googleSignUpRequestCode = 9002

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AppConstant.WEB_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

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

        binding.buttonContinueWithGoogle.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, googleSignUpRequestCode)
        }

        binding.buttonLogin.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun handleGoogleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (exception: ApiException) {
            CustomDialog.showToast(
                requireContext(),
                getString(R.string.str_google_signup_failed, exception.message.orEmpty())
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == googleSignUpRequestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleResult(task)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val email = account.email.orEmpty()
                    PreferenceHelper.setUserEmail(requireContext(), email)
                    PreferenceHelper.saveProfileData(
                        requireContext(),
                        ProfileData(
                            name = account.displayName.orEmpty(),
                            email = email,
                            imageUri = account.photoUrl?.toString().orEmpty()
                        )
                    )
                    CustomDialog.showToast(requireContext(), getString(R.string.str_signup_success))
                    startActivity(Intent(requireContext(), HomeMainActivity::class.java))
                    requireActivity().finish()
                } else {
                    CustomDialog.showToast(requireContext(), getString(R.string.str_google_signup_generic_error))
                }
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
