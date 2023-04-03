package ru.androideducation.shopping_list.domain

import javax.inject.Inject

class EditShopItemUseCase @Inject constructor(
    private val shopListRepository: ShopListRepository
    ) {
    suspend fun changeShopItemById (shopItem: ShopItem) {
        shopListRepository.changeShopItemById(shopItem)
    }
}