package ru.androideducation.shopping_list.domain

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun changeShopItemById (shopItem: ShopItem) {
        shopListRepository.changeShopItemById(shopItem)
    }
}