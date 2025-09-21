package mrkinfotech.Grocio.ui.Account

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import mrkinfotech.Grocio.databinding.FragmentFavoriteBinding
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.MasterDataUtils

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: ItemAdapter
    val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(requireContext(), "✅ Camera granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "❌ Camera denied", Toast.LENGTH_SHORT).show()
            }
        }
    val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fine || coarse) {
                Toast.makeText(requireContext(), "✅ Location granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "❌ Location denied", Toast.LENGTH_SHORT).show()
            }
        }
    val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted =
                permissions.values.any { it } // If at least one storage permission is granted
            if (granted) {
                Toast.makeText(requireContext(), "✅ Storage granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "❌ Storage denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewFavourite.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        itemAdapter = ItemAdapter(
            requireContext(), MasterDataUtils.productItem(requireContext()),
            ItemAdapter.OnClickListener { itemData, clickType -> })
        binding.recyclerViewFavourite.adapter = itemAdapter

        binding.btnCamera.setOnClickListener {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        binding.btnStorage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                storagePermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                )
            } else {
                storagePermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
            binding.btnLocation.setOnClickListener {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            binding.idBtnGenerateQR.setOnClickListener {
                if (TextUtils.isEmpty(
                        binding.idEdt.getText().toString()
                    )
                ) {
                    CustomDialog.showTostMessage(requireContext(),"create QR code")
                }
                else {
                    generateQRCode(
                        binding.idEdt.getText().toString()
                    )
                }
            }

        }
    }
    fun generateQRCode(text: String) {
        val barcodeEncoder = BarcodeEncoder()
        try {
            // This method returns a Bitmap image of the
            // encoded text with a height and width of 400
            // pixels.
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400)
            binding.imgQrCode.setImageBitmap(bitmap) // Sets the Bitmap to ImageView
        } catch (e: WriterException) {
            e.printStackTrace()
        }
}
}