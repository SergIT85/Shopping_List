package ru.androideducation.shopping_list.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import ru.androideducation.shopping_list.R
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    var count = 9
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this) {
            Log.d("TAGSHOP", "ShopList${it.toString()}")
            if(count > 0) {
                count--
                val item = it[0]
                viewModel.changeShopItem(item)
            }

        }

    }
}