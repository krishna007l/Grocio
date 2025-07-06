package mrkinfotech.Grocio.ui.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.R

class ImageSlideAdapter(
    private val context: Context,
    private var imageList: ArrayList<String>,
) : PagerAdapter() {

    override fun getCount(): Int = imageList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutId =
            R.layout.item_image_slider

        val view = LayoutInflater.from(context).inflate(layoutId, container, false)
        val ivImage = view.findViewById<ImageView>(R.id.imageView)

        Glide.with(context)
            .load(imageList[position])
            .placeholder(R.color.black)
            .error(R.drawable.ic_error)
            .into(ivImage)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}