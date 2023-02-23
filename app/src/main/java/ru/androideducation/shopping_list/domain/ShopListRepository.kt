package ru.androideducation.shopping_list.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun changeShopItemById (shopItem: ShopItem)

    fun getShopItemById(shopItemId: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

    fun updateShopListLD()

}