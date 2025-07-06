package mrkinfotech.Grocio.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mrkinfotech.Grocio.databinding.ActivityMainBinding

class HomeMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}