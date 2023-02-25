package ru.androideducation.shopping_list.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.androideducation.shopping_list.domain.ShopItem
import ru.androideducation.shopping_list.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val mutableShowList = sortedSetOf<ShopItem>({o1,o2 -> o1.id.compareTo(o2.id)})

    private var autoIncrementId = 0

    init {
        for(i in 0..1000) {
            val item = ShopItem("Item ${i+1}", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        mutableShowList.add(shopItem)
        updateShopListLD()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        mutableShowList.remove(shopItem)
        updateShopListLD()
    }

    override fun changeShopItemById(shopItem: ShopItem) {

        val oldElementShopItem = getShopItemById(shopItem.id)
        mutableShowList.remove(oldElementShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItemById(shopItemId: Int): ShopItem {
        return mutableShowList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element not found with id =$shopItemId ")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun updateShopListLD() {
        shopListLD.value = mutableShowList.toList()
    }
}