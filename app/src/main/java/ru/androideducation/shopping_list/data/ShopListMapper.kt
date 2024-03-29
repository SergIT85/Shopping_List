package ru.androideducation.shopping_list.data


import ru.androideducation.shopping_list.domain.ShopItem
import javax.inject.Inject

class ShopListMapper @Inject constructor() {

    fun mapShopItemToShopItemDbModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enadled
    )

    fun mapShopItemDbModelToShopItem (shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enadled = shopItemDbModel.enabled
    )

    fun mapListDbModelToShopItem(list: List<ShopItemDbModel>) = list.map {
            mapShopItemDbModelToShopItem(it)
    }

}