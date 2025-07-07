package mrkinfotech.Grocio.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentSignUpBinding


private fun SignUpFragment.Intcent(
    fragment: SignUpFragment,
    klass: Class<LoginActivity>
): Intent {
    return TODO("Provide the return value")
}


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root

        var firebaseAuth = FirebaseAuth.getInstance()

        binding.SinupToLogin.setOnClickListener {
            val email = binding.textTitleEmail.text.toString()
            val password = binding.textTitlePassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val addOnCompleteListener =
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                       if (task.isSuccessful) {
                            Toast.makeText(context, "signup successful", Toast.LENGTH_SHORT)

                           findNavController().navigate(R.id.nav_host_fragment_login_content_main)
                        } else {

                            Toast.makeText(context, "signup successful", Toast.LENGTH_SHORT)

                        }
                        val toast = null
                        toast
                    }
            }else{
                Toast.makeText(context ,"enter email and password", Toast.LENGTH_SHORT)
            }
        }
        binding.main.setOnClickListener {
            startActivity(Intcent( this, LoginActivity::class.java))

        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  binding.SinupToLogin.setOnClickListener {
            findNavController().navigate(R.id.LoginFragment)
        }*/

        binding.signin.setOnClickListener {
            findNavController().navigate(R.id.LoginFragment)
        }
    }
}

