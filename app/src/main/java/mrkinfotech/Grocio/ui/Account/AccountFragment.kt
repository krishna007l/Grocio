package mrkinfotech.Grocio.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.FragmentAccountBinding
import mrkinfotech.Grocio.ui.data.ProfileData
import mrkinfotech.Grocio.ui.login.LoginActivity
import mrkinfotech.Grocio.ui.map.MapActivity
import mrkinfotech.Grocio.utils.AppConstant
import mrkinfotech.Grocio.utils.AppHelper
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.PreferenceHelper

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    lateinit var googleSignInClient: GoogleSignInClient
    private val db = FirebaseFirestore.getInstance()
    private val addressPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val localBinding = _binding ?: return@registerForActivityResult
            val fragmentContext = context ?: return@registerForActivityResult
            if (result.resultCode == Activity.RESULT_OK) {
                val address = result.data?.getStringExtra("selected_address")
                    ?: return@registerForActivityResult
                localBinding.textDeliveryAddress.text = address

                val profile = PreferenceHelper.getProfileData(fragmentContext)
                PreferenceHelper.saveProfileData(fragmentContext, profile.copy(address = address))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profile = PreferenceHelper.getProfileData(requireContext())
        bindProfile(profile)
        syncProfileFromRemote(profile)

        binding.profileLayout.setOnClickListener {
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.AccountFragment) {
                navController.navigate(R.id.action_AccountFragment_to_ProfileFragment)
            }
        }

        binding.OrdersLayout.setOnClickListener {
            CustomDialog.showToast(requireContext(), getString(R.string.str_orders_coming_soon))
        }

        binding.deliveryAddressLayout.setOnClickListener {
            onDeliveryAddressClicked()
        }

        binding.termAndConditionLayout.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", "Terms and Conditions")
            }
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.AccountFragment) {
                navController.navigate(R.id.action_AccountFragment_to_SettingsFragment, bundle)
            }
        }

        binding.customerSupportLayout.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", "Customer Support")
            }
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.AccountFragment) {
                navController.navigate(R.id.action_AccountFragment_to_SettingsFragment, bundle)
            }
        }

        binding.helpLayout.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", "Help")
            }
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.AccountFragment) {
                navController.navigate(R.id.action_AccountFragment_to_SettingsFragment, bundle)
            }
        }

        binding.aboutLayout.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", "About")
            }
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.AccountFragment) {
                navController.navigate(R.id.action_AccountFragment_to_SettingsFragment, bundle)
            }
        }

        binding.buttonLogOut.setOnClickListener {
            onLogOutClicked()
        }
    }

    private fun onDeliveryAddressClicked() {
        val currentAddress = binding.textDeliveryAddress.text.toString().trim()
        CustomDialog.showPicAddressDialog(
            requireContext(),
            requireActivity(),
            if (currentAddress != getString(R.string.str_delivery_address)) {
                currentAddress
            } else {
                getString(R.string.str_delivery_address)
            },
            { address ->
                binding.textDeliveryAddress.text = address

                val profile = PreferenceHelper.getProfileData(requireContext())
                val updatedProfile = profile.copy(address = address)
                PreferenceHelper.saveProfileData(requireContext(), updatedProfile)

                val userEmail = updatedProfile.email.ifBlank {
                    PreferenceHelper.getUserEmail(requireContext()).orEmpty()
                }
                if (userEmail.isNotBlank()) {
                    db.collection(AppConstant.USER_COLLECTION)
                        .document(userEmail)
                        .set(mapOf("address" to address), SetOptions.merge())
                        .addOnSuccessListener {
                            if (isAdded) {
                                CustomDialog.showToast(
                                    requireActivity(),
                                    getString(R.string.str_address_updated)
                                )
                            }
                        }
                }
            },
            {
                val intent = Intent(requireActivity(), MapActivity::class.java)
                addressPickerLauncher.launch(intent)
            }
        )
    }

    private fun onLogOutClicked() {
        val hostActivity = activity ?: return
        val appContext = hostActivity.applicationContext
        CustomDialog.showConfirmationDialog(
            hostActivity,
            getString(R.string.str_want_to_logout),
            getString(R.string.str_yes),
            getString(R.string.str_no)
        ) {
            val auth = FirebaseAuth.getInstance()
            val shouldSignOutFromGoogle = auth.currentUser?.providerData?.any {
                it.providerId == GoogleAuthProvider.PROVIDER_ID
            } == true

            auth.signOut()

            if (shouldSignOutFromGoogle) {
                googleSignInClient.signOut().addOnCompleteListener {
                    completeLogout(appContext)
                }
            } else {
                completeLogout(appContext)
            }
        }
    }

    private fun completeLogout(appContext: Context) {
        PreferenceHelper.clearUserSession(appContext)

        val intent = Intent(appContext, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        appContext.startActivity(intent)
        activity?.finish()
    }

    private fun bindProfile(profile: ProfileData) {
        val localBinding = _binding ?: return
        localBinding.textUserEmail.text = profile.email
        localBinding.textUserName.text = profile.name.ifBlank {
            getString(R.string.str_grocio_member)
        }
        localBinding.textDeliveryAddress.text = profile.address.ifBlank {
            getString(R.string.str_delivery_address)
        }

        val imageUrl = profile.imageUri.takeIf { it.isNotBlank() }
        if (imageUrl.isNullOrBlank()) {
            localBinding.imageUser.setImageResource(R.drawable.ic_account)
        } else {
            AppHelper.setImageWithPlaceHolderInGlide(
                context = requireContext(),
                imageUrl = imageUrl,
                placeHolder = R.drawable.ic_account,
                imageView = localBinding.imageUser
            )
        }
    }

    private fun syncProfileFromRemote(localProfile: ProfileData) {
        val userEmail = localProfile.email.ifBlank {
            PreferenceHelper.getUserEmail(requireContext()).orEmpty()
        }
        if (userEmail.isBlank()) return
        if (
            localProfile.name.isNotBlank() &&
            localProfile.address.isNotBlank() &&
            localProfile.gender.isNotBlank() &&
            localProfile.imageUri.isNotBlank() &&
            localProfile.age > 0
        ) {
            return
        }

        db.collection(AppConstant.USER_COLLECTION)
            .document(userEmail)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!isAdded || _binding == null || !snapshot.exists()) return@addOnSuccessListener

                val mergedProfile = ProfileData(
                    name = snapshot.getString("name").orEmpty().ifBlank { localProfile.name },
                    email = snapshot.getString("email").orEmpty().ifBlank { userEmail },
                    age = (snapshot.getLong("age") ?: 0L).toInt().takeIf { it > 0 }
                        ?: localProfile.age,
                    gender = snapshot.getString("gender").orEmpty().ifBlank { localProfile.gender },
                    imageUri = snapshot.getString("imageUri").orEmpty()
                        .ifBlank { localProfile.imageUri },
                    address = snapshot.getString("address").orEmpty()
                        .ifBlank { localProfile.address }
                )

                PreferenceHelper.saveProfileData(requireContext(), mergedProfile)
                bindProfile(mergedProfile)
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
