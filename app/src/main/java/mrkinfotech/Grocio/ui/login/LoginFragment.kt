package mrkinfotech.Grocio.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentLoginBinding
import mrkinfotech.Grocio.ui.data.ProfileData
import mrkinfotech.Grocio.ui.home.HomeMainActivity
import mrkinfotech.Grocio.utils.AppConstant
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.PreferenceHelper

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val pass = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentProfile = PreferenceHelper.getProfileData(requireContext())
                        PreferenceHelper.saveProfileData(
                            requireContext(),
                            currentProfile.copy(
                                name = currentProfile.name.ifBlank {
                                    email.substringBefore("@").trim()
                                },
                                email = email
                            )
                        )
                        startActivity(Intent(requireContext(), HomeMainActivity::class.java))
                        PreferenceHelper.setUserEmail(requireContext(), email)
                        CustomDialog.showToast(requireContext(), getString(R.string.str_login_success))
                        requireActivity().finish()
                    } else {
                        CustomDialog.showToast(
                            requireContext(),
                            it.exception?.message ?: getString(R.string.str_login_failed)
                        )
                    }
                }
            } else {
                CustomDialog.showToast(requireContext(), getString(R.string.str_empty_fields_not_allowed))
            }
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AppConstant.WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.buttonContinueWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.SignUpFragment)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            CustomDialog.showToast(
                requireActivity(),
                getString(R.string.str_sign_in_failed, e.message.orEmpty())
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    CustomDialog.showToast(requireActivity(), getString(R.string.str_login_success))
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
                    startActivity(Intent(requireActivity(), HomeMainActivity::class.java))
                    requireActivity().finish()
                } else {
                    CustomDialog.showToast(requireActivity(), getString(R.string.str_login_failed))
                }
            }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
