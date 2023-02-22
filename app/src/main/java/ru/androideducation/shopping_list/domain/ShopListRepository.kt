package ru.androideducation.shopping_list.domain

interface ShopListRepository {

    fun addShopItem(item: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun changeShopItemById (shopItem: ShopItem)

    fun getShopItemById(ShopItemId: Int): ShopItem

    fun getShopList(): List<ShopItem>

}