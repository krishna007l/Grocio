package mrkinfotech.Grocio.ui.home

import CategoryAdapter
import ProductAdapter
import ProductItem
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import mrkinfotech.Grocio.R
import java.util.Timer
import java.util.TimerTask


class HomeMainActivity : AppCompatActivity() {
    private lateinit var categoriesRecycler: RecyclerView
    private lateinit var productsRecycler: RecyclerView
    private lateinit var viewPager: ViewPager2
    private var timer: Timer? = null
    private val images = listOf(R.drawable.logo, R.drawable.login_image, R.drawable.icon)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoriesRecycler = findViewById(R.id.categoriesRecycler)
        productsRecycler = findViewById(R.id.productsRecycler)

        // Sample categories
        val categories = listOf("vegitabel")
        val categoryAdapter = CategoryAdapter(categories)
        categoriesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoriesRecycler.adapter = categoryAdapter

        // Sample products
        val products = listOf(
            ProductItem("apple", "$0.30"),
        )

        val productAdapter = ProductAdapter(products)
        productsRecycler.layoutManager = LinearLayoutManager(this)
        productsRecycler.adapter = productAdapter

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ImageAdapter(images)

        startAutoRotation()
    }

    private fun startAutoRotation() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val next = (viewPager.currentItem + 1) % images.size
                    viewPager.setCurrentItem(next, true)
                }
            }
        }, 3000, 3000) // Start after 3s, repeat every 3s
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
