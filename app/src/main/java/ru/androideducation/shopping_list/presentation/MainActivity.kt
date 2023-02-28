package ru.androideducation.shopping_list.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.androideducation.shopping_list.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListadapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setAdapter()
        viewModel.shopList.observe(this) {
            shopListadapter.submitList(it)
        }
        val buttonAddShopItem = findViewById<FloatingActionButton>(R.id.bottom_add_shop_item)
        buttonAddShopItem.setOnClickListener {
            val intent = ShopItemActivity.addShopItem(this)
            startActivity(intent)
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
                val item = shopListadapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callbackObject)
        itemTouchHelper.attachToRecyclerView(rvShopListAdapter)
    }

    private fun setClickListener() {
        shopListadapter.onShopItemClickListener = {
            Log.d("onShopItemClickListener", "shopItem: $it")
            val intent = ShopItemActivity.editShopItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun setLongClickListener() {
        shopListadapter.onShopItemLingClickListener = {
            viewModel.changeShopItem(it)

        }
    }
}