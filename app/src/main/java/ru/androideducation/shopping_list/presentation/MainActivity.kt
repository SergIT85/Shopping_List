package ru.androideducation.shopping_list.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.presentation.itemfragment.FragmentShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListadapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.fragment_shop_item_container)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setAdapter()
        viewModel.shopList.observe(this) {
            shopListadapter.submitList(it)
        }
        val buttonAddShopItem = findViewById<FloatingActionButton>(R.id.bottom_add_shop_item)
        buttonAddShopItem.setOnClickListener {
            if (isOnePaneMod()) {
                val intent = ShopItemActivity.addShopItem(this)
                startActivity(intent)
            } else {
                launchFragment(FragmentShopItem.newInstanceAddItem())
            }
        }



    }

    private fun isOnePaneMod(): Boolean {
        return shopItemContainer == null
    }
    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack() // для удаления одного фрагмента из стэка если он там есть
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_shop_item_container, fragment)
                .addToBackStack(null) // для добавления фрагмента в стэк
                .commit()
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
            if (isOnePaneMod()) {
                val intent = ShopItemActivity.editShopItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(FragmentShopItem.newInstanceEditItem(it.id))
            }

        }
    }

    private fun setLongClickListener() {

        shopListadapter.onShopItemLingClickListener = {
            viewModel.changeShopItem(it)

        }
    }

}