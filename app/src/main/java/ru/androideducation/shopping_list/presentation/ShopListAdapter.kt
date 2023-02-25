package ru.androideducation.shopping_list.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem


class ShopListAdapter : ListAdapter<
        ShopItem,
        ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLingClickListener: ((ShopItem) -> Unit )? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val view: View = if (viewType == ENADLED) {
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shop_enabled,
                parent,
                false
            )
        } else {
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shop_disabled,
                parent,
                false
            )
        }
        return ShopItemViewHolder(view)

    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
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
        val shopItem = getItem(position)
        return if (shopItem.enadled) {
            ENADLED
        } else {
            DISABLED
        }
    }

    companion object {
        const val ENADLED = 1
        const val DISABLED = 0
        const val MAX_VIEW_SIZE = 18
    }
}