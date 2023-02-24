package ru.androideducation.shopping_list.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var lLShopItem: LinearLayout
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lLShopItem = findViewById(R.id.ll_shop_item)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this) {
            showLinearLayoutShopList(it)
        }




    }
    fun showLinearLayoutShopList(list: List<ShopItem>) {
        lLShopItem.removeAllViews()
        for (shopItem in list ) {
            val layoutId = if(shopItem.enadled){
                R.layout.item_shop_enabled
            } else {
                R.layout.item_shop_disabled
            }
            val view = LayoutInflater.from(this).inflate(layoutId, lLShopItem, false)

            val text = view.findViewById<TextView>(R.id.tv_name)
            val count = view.findViewById<TextView>(R.id.tv_count)
            text.text = shopItem.name.toString()
            count.text = shopItem.id.toString()

            view.setOnLongClickListener {
                viewModel.changeShopItem(shopItem)
                true
            }
            lLShopItem.addView(view)


        }
    }
}