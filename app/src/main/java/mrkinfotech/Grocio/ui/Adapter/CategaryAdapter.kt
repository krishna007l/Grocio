package mrkinfotech.Grocio.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ItemCategaryBinding
import mrkinfotech.Grocio.ui.Datamodel.CategaryItem
import mrkinfotech.Grocio.ui.Datamodel.itemDataclass

class CategaryAdapter (
    val context: Context,
    private var categaryList: ArrayList<CategaryItem>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<CategaryAdapter.ItemViewHolderCategary>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolderCategary {
        val binding = ItemCategaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolderCategary(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolderCategary, position: Int) {
        holder.bind(categaryList[position], onClickListener
        )
    }

    override fun getItemCount(): Int = categaryList.size

    class OnClickListener(val clickListener: (itemData: itemDataclass, clickType: Int) -> Unit) {
        fun onClick(itemData: itemDataclass, clickType: Int) = clickListener(itemData, clickType)
    }

    fun updateList(newPlayers: ArrayList<CategaryItem>) {
        categaryList.clear()
        categaryList.addAll(newPlayers)
        notifyDataSetChanged()
    }

    inner class ItemViewHolderCategary(private val binding: ItemCategaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataModal: CategaryItem, onClickListener: OnClickListener) {

            Glide.with(context)
                .load(dataModal.Categary)
                .centerCrop()
                .placeholder(R.drawable.logo)
                .into(binding.categaryitemimage)
        }
    }
}

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
