package mrkinfotech.Grocio.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.shopify.utils.PreferenceHelper
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentSignUpBinding
import mrkinfotech.Grocio.ui.home.HomeMainActivity


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater , container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.signinbutton.setOnClickListener {
//            val username = binding.editTextEmail.text.toString()
//            val userEmail = binding.editTextEmail.text.toString()
//            val password = binding.editTextPassword.text.toString()
//
//            if (userEmail == "test@gmail.com" && password == "123" && username == "Test") {
//                startActivity(Intent(requireContext(), LoginFragment::class.java))
//            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "Enter Valid UserName & Password",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }

        binding.signinbutton.setOnClickListener(){

            findNavController().navigate(R.id.LoginFragment)
        }
        

    }
}
