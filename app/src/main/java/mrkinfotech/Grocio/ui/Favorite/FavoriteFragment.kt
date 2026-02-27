package mrkinfotech.Grocio.ui.Account


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.databinding.FragmentFavoriteBinding
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.ui.Api.RetrofitClient
import mrkinfotech.Grocio.ui.Datamodel.Post
import mrkinfotech.Grocio.utils.CustomDialog
import mrkinfotech.Grocio.utils.MasterDataUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: ItemAdapter


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

        binding.btnCamera.setOnClickListener(View.OnClickListener{
            requstcamera.launch(android.Manifest.permission.CAMERA)
        })

        binding.btnLocation.setOnClickListener(View.OnClickListener{
            requstlocation.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requstlocation.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            requstlocation.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        })

        binding.btnStorage.setOnClickListener(View.OnClickListener{
            requststorage.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            requststorage.launch(android.Manifest.permission.READ_MEDIA_VIDEO)
            requststorage.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
        })
    }
    val requstcamera = registerForActivityResult(ActivityResultContracts.RequestPermission(),{
        if (it){
            CustomDialog.showTostMessage(requireContext(),"permission granted")
        }else{
            CustomDialog.showTostMessage(requireContext(),"permission not granted")
        }
    })

    val requstlocation = registerForActivityResult(ActivityResultContracts.RequestPermission(),{
        if (it){
            CustomDialog.showTostMessage(requireContext(),"permission granted")
        }else{
            CustomDialog.showTostMessage(requireContext(),"permission not granted")
        }
    })

    val requststorage = registerForActivityResult(ActivityResultContracts.RequestPermission(),{
        if (it){
            CustomDialog.showTostMessage(requireContext(),"permission granted")
        }else{
            CustomDialog.showTostMessage(requireContext(),"permission not granted")
        }
    })
    private fun getPosts() {
        RetrofitClient.apiService.getPosts()
            .enqueue(object : Callback<List<Post>> {

                override fun onResponse(
                    call: Call<List<Post>>,
                    response: Response<List<Post>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.forEach {
                            Log.d("API_DATA", it.Value)
                        }
                    }
                }

                 override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    Log.e("API_ERROR", t.message.toString())
                }
            })
    }

}

