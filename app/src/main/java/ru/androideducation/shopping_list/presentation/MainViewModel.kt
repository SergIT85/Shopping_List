package ru.androideducation.shopping_list.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.androideducation.shopping_list.data.ShopListRepositoryImpl
import ru.androideducation.shopping_list.domain.DeleteShopItemUseCase
import ru.androideducation.shopping_list.domain.EditShopItemUseCase
import ru.androideducation.shopping_list.domain.GetShopListUseCase
import ru.androideducation.shopping_list.domain.ShopItem
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase
) : ViewModel() {

    val shopList = getShopListUseCase.getShopList()


    fun changeShopItem(shopItem: ShopItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (shopList.value?.isNotEmpty() == true) {
                val newItem = shopItem.copy(enadled = !shopItem.enadled)
                editShopItemUseCase.changeShopItemById(newItem)
            }
        }

    }

    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (shopList.value?.isNotEmpty() == true) {
                deleteShopItemUseCase.deleteShopItem(shopItem)
            }
        }
    }
}