package ru.androideducation.shopping_list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ru.androideducation.shopping_list.data.ShopListRepositoryImpl
import ru.androideducation.shopping_list.domain.*

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun changeShopItem(shopItem: ShopItem) {
        if (shopList.value?.isNotEmpty() == true) {
            val newItem = shopItem.copy(enadled = !shopItem.enadled)
            editShopItemUseCase.changeShopItemById(newItem)
        }
    }

    fun deleteShopItem(shopItem: ShopItem) {
        if (shopList.value?.isNotEmpty() == true) {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }
}