package mrkinfotech.Grocio.ui.login

import android.annotation.SuppressLint
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
import mrkinfotech.Grocio.databinding.FragmentLoginBinding
import mrkinfotech.Grocio.ui.home.HomeFragment
import mrkinfotech.Grocio.ui.home.HomeMainActivity


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginbutton.setOnClickListener {
            val userEmail = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (userEmail == "demo@gmail.com" && password == "100") {
                PreferenceHelper.setUserEmail(requireContext(), userEmail)
                startActivity(Intent(requireContext(), HomeMainActivity::class.java))
            } else {
                Toast.makeText(
                    requireContext(),
                    "Enter Valid UserName & Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}