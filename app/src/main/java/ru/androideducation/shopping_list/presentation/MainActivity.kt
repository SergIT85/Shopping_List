package ru.androideducation.shopping_list.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem
import kotlin.math.log

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setAdapter()
        viewModel.shopList.observe(this) {
            adapter.shopList = it
        }
    }

    fun setAdapter() {

        val rvShopListAdapter = findViewById<RecyclerView>(R.id.rv_shop_list)
        adapter = ShopListAdapter()

        rvShopListAdapter.adapter = adapter

        rvShopListAdapter.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.ENADLED,
            ShopListAdapter.MAX_VIEW_SIZE
        )

        rvShopListAdapter.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.DISABLED,
            ShopListAdapter.MAX_VIEW_SIZE
        )
    }
}