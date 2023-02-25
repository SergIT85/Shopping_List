package ru.androideducation.shopping_list.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem
import kotlin.math.log

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel
    private lateinit var shopListadapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setAdapter()
        viewModel.shopList.observe(this) {
            shopListadapter.shopList = it
        }
    }

    fun setAdapter() {

        val rvShopListAdapter = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopListAdapter) {
            shopListadapter = ShopListAdapter()
            adapter = shopListadapter

            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENADLED,
                ShopListAdapter.MAX_VIEW_SIZE
            )

            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLED,
                ShopListAdapter.MAX_VIEW_SIZE
            )
        }
        setLongClickListener()
        setClickListener()

        swipeMetod(rvShopListAdapter)


    }

    private fun swipeMetod(rvShopListAdapter: RecyclerView) {
        val callbackObject = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListadapter.shopList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callbackObject)
        itemTouchHelper.attachToRecyclerView(rvShopListAdapter)
    }

    private fun setClickListener() {
        shopListadapter.onShopItemClickListener = {
            Log.d("onShopItemClickListener", "shopItem: $it")
        }
    }

    private fun setLongClickListener() {
        shopListadapter.onShopItemLingClickListener = {
            viewModel.changeShopItem(it)
        }
    }
}