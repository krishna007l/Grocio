package mrkinfotech.Grocio.ui.Account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopify.utils.PreferenceHelper
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentAccountBinding
import mrkinfotech.Grocio.ui.base.SplashActivity


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogOut.setOnClickListener {
            PreferenceHelper.setUserEmail(requireContext(), "")
            startActivity(Intent(requireContext(), SplashActivity::class.java))
            requireActivity().finish()
//            findNavController().navigateUp()
        }

}
}