@file:Suppress("DEPRECATION")

package mrkinfotech.Grocio.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopify.utils.PreferenceHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentLoginBinding
import mrkinfotech.Grocio.ui.home.HomeMainActivity
import mrkinfotech.Grocio.utils.AppConstant
import mrkinfotech.Grocio.utils.CustomDialog


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
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



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AppConstant.WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.Googlewithsingin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            CustomDialog.showTostMessage(requireActivity(), "Sign-in failed: ${e.message}")
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    CustomDialog.showTostMessage(requireActivity(), "Login Successful")
                    PreferenceHelper.setUserEmail(requireContext(), account.email)
                    startActivity(Intent(requireActivity(), HomeMainActivity::class.java))
                } else {
                    CustomDialog.showTostMessage(requireActivity(), "Login Failed")
                }
            }
        }
    }
