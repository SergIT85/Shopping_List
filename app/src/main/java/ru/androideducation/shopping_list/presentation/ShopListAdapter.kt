package ru.androideducation.shopping_list.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem



class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var onShopItemLingClickListener: ((ShopItem) -> Unit )? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    var count = 0
    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopList, value)
            val diffCallback = DiffUtil.calculateDiff(callback)
            diffCallback.dispatchUpdatesTo(this)
            field = value

        }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("onCreateViewHolder", "count = ${++count}")

        val view: View
        if (viewType == ENADLED) {
            view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_shop_enabled,
                parent,
                false
            )
        } else {
            view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_shop_disabled,
                parent,
                false
            )

        }
        return ShopItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        with(holder.view) {
            setOnLongClickListener {
                onShopItemLingClickListener?.invoke(shopItem)
                true
            }
            setOnClickListener {
                onShopItemClickListener?.invoke(shopItem)
            }

        }

        if (shopItem.enadled) {
            holder.tvName.text = shopItem.name
            holder.tvCount.text = shopItem.count.toString()
            holder.tvName.setTextColor(ContextCompat.getColor(holder.view.context,
                android.R.color.holo_red_dark
                )
            )
        } else {
            holder.tvName.text = shopItem.name
            holder.tvCount.text = shopItem.count.toString()
            holder.tvName.setTextColor(ContextCompat.getColor(holder.view.context,
                android.R.color.holo_blue_dark
            )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList[position]
        if (shopItem.enadled) {
            return ENADLED
        } else {
            return DISABLED
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    companion object {
        const val ENADLED = 1
        const val DISABLED = 0
        const val MAX_VIEW_SIZE = 18
    }
}