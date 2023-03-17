package ru.androideducation.shopping_list.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.databinding.ItemShopDisabledBinding
import ru.androideducation.shopping_list.databinding.ItemShopEnabledBinding
import ru.androideducation.shopping_list.domain.ShopItem


class ShopListAdapter : ListAdapter<
        ShopItem,
        ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLingClickListener: ((ShopItem) -> Unit )? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when (viewType) {
            ENADLED -> R.layout.item_shop_enabled
            DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown Layout")
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = holder.binding
        with(binding.root) {
            setOnLongClickListener {
                onShopItemLingClickListener?.invoke(shopItem)
                true
            }
            setOnClickListener {
                onShopItemClickListener?.invoke(shopItem)
            }
        }


        when (binding) {
              is ItemShopDisabledBinding -> {
                binding.shopItem = shopItem
            }
            is ItemShopEnabledBinding -> {
                binding.shopItem = shopItem
            }
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