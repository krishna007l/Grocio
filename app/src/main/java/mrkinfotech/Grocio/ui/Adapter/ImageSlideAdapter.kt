package mrkinfotech.Grocio.ui.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.viewpager.widget.PagerAdapter
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.ui.Datamodel.ImageData

class ImageSlideAdapter(
    private val context: Context,
    private var imageList: ArrayList<ImageData>
) : PagerAdapter() {

    override fun getCount(): Int = imageList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId = R.layout.item_image_slider

        val view = LayoutInflater.from(context).inflate(layoutId, container, false)
        val ivImage = view.findViewById<ImageView>(R.id.imageView)

        Glide.with(context)
            .load(imageList[position])
            .placeholder(R.drawable.logo)
            .into(ivImage)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}