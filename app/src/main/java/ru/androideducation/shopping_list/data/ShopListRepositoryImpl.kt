package ru.androideducation.shopping_list.data

import ru.androideducation.shopping_list.domain.ShopItem
import ru.androideducation.shopping_list.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl: ShopListRepository {

    private val mutableShowList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        mutableShowList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        mutableShowList.remove(shopItem)
    }

    override fun changeShopItemById(shopItem: ShopItem) {

        val oldElementShopItem = getShopItemById(shopItem.id)
        mutableShowList.remove(oldElementShopItem)
        mutableShowList.add(shopItem)
    }

    override fun getShopItemById(shopItemId: Int): ShopItem {
        return mutableShowList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element not found with id =$shopItemId ")
    }

    override fun getShopList(): List<ShopItem> {
        return mutableShowList.toList()
    }
}