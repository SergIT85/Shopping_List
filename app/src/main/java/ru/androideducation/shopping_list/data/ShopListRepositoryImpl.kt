package ru.androideducation.shopping_list.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.androideducation.shopping_list.domain.ShopItem
import ru.androideducation.shopping_list.domain.ShopListRepository


class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {

    private val shopListDao = AppDatabase.getInstanceDb(application).shopListDao()

    private val mapper = ShopListMapper()

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapShopItemToShopItemDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun changeShopItemById(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapShopItemToShopItemDbModel(shopItem))
    }

    override suspend fun getShopItemById(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapShopItemDbModelToShopItem(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = shopListDao.getShopList().map {
        mapper.mapListDbModelToShopItem(it)
    }

    override fun updateShopListLD() {
        TODO("Not yet implemented")
    }
}