package ru.androideducation.shopping_list.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.androideducation.shopping_list.ApplicationMy
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.databinding.ActivityMainBinding
import ru.androideducation.shopping_list.di.AppComponent
import ru.androideducation.shopping_list.presentation.factory.ViewModelFactory
import ru.androideducation.shopping_list.presentation.itemfragment.FragmentShopItem
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FragmentShopItem.OnEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListadapter: ShopListAdapter

    private val component by lazy {
        (application as ApplicationMy).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        setAdapter()
        viewModel.shopList.observe(this) {
            shopListadapter.submitList(it)
        }
        binding.bottomAddShopItem.setOnClickListener {
            if (isOnePaneMod()) {
                val intent = ShopItemActivity.addShopItem(this)
                startActivity(intent)
            } else {
                launchFragment(FragmentShopItem.newInstanceAddItem())
            }
        }
    }

    private fun isOnePaneMod(): Boolean {
        return binding.fragmentShopItemContainer == null
    }
    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack() // для удаления одного фрагмента из стэка если он там есть
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_shop_item_container, fragment)
                .addToBackStack(null) // для добавления фрагмента в стэк
                .commit()
    }

    private fun setAdapter() {
        with(binding.rvShopList) {
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
        swipeMetod(binding.rvShopList)
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

    override fun onEditingFinishedListener() {
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }

}