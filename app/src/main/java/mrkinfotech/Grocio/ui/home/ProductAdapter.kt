package mrkinfotech.Grocio.ui.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mrkinfotech.Grocio.R


class ProductAdapter(val requiredcontext: Context, private val itemdataclass : ArrayList<itemdataclass>): RecyclerView.Adapter<ProductAdapter.viewHolderAdepter>() {

    class viewHolderAdepter(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name1: TextView=itemView.findViewById(R.id.productTitle)
        val price2: TextView=itemView.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderAdepter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        return viewHolderAdepter(view)
    }


    override fun onBindViewHolder(holder: viewHolderAdepter, position: Int) {
        holder.name1.text=itemdataclass[position].name
        holder.price2.text=itemdataclass[position].price
    }

    override fun getItemCount(): Int {
        return itemdataclass.size
    }



}