package mrkinfotech.Grocio.ui.Account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import mrkinfotech.Grocio.databinding.FragmentCategaryBinding
import mrkinfotech.Grocio.ui.Adapter.CategaryAdapter
import mrkinfotech.Grocio.ui.Adapter.ItemAdapter
import mrkinfotech.Grocio.ui.Datamodel.CategaryItem
import mrkinfotech.Grocio.utils.MasterDataUtils
import mrkinfotech.Grocio.utils.MasterDataUtils.Categaryitem

class CategaryFragment : Fragment() {

    private var _binding: FragmentCategaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategaryBinding.inflate(inflater, container, false)
        return binding.root
    }

}