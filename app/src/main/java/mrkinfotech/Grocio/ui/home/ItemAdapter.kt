package mrkinfotech.Grocio.ui.home
import android.R.attr.text
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ItemProductBinding


class ItemAdapter(
    val context: Context,
    private var itemList: ArrayList<itemDataclass>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList[position], onClickListener
        )
    }

    override fun getItemCount(): Int = itemList.size

    class OnClickListener(val clickListener: (itemData: itemDataclass, clickType: Int) -> Unit) {
        fun onClick(itemData: itemDataclass, clickType: Int) = clickListener(itemData, clickType)
    }

    fun updateList(newPlayers: ArrayList<itemDataclass>) {
        itemList.clear()
        itemList.addAll(newPlayers)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataModal: itemDataclass, onClickListener: OnClickListener) {

            binding.productTitle.text = dataModal.nameProduct
            binding.productPrice.text = dataModal.priceProduct.toString()



            Glide.with(context)
                .load(dataModal.imageProduct)
                .centerCrop()
                .into(binding.productImage)

//            // Cart button state
//            if (dataModal.isCarted) {
//                binding.buttonAddToCart.setImageResource(R.drawable.ic_delete_with_bg)
//            } else {
//                binding.buttonAddToCart.setImageResource(R.drawable.ic_add_to_cart)
//            }
//
//            // Like button state
//            if (dataModal.isLiked) {
//                binding.buttonLike.setImageResource(R.drawable.ic_liked)
//            } else {
//                binding.buttonLike.setImageResource(R.drawable.ic_like)
//            }
//
//
//            binding.buttonAddToCart.setOnClickListener {
//                onClickListener.onClick(dataModal, AppConstant.BUTTON_ADD_TO_CART_CLICK)
//            }
//
//            binding.buttonLike.setOnClickListener {
//                onClickListener.onClick(dataModal, AppConstant.ITEM_LIKE_CLICK)
//            }
//
//            binding.root.setOnClickListener {
//                onClickListener.onClick(dataModal, AppConstant.ITEM_CLICK)
//            }
        }
    }
}