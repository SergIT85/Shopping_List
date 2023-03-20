package ru.androideducation.shopping_list.data


import ru.androideducation.shopping_list.domain.ShopItem

class ShopListMapper {

    fun mapShopItemToShopItemDbModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enadled = shopItem.enadled
    )

    fun mapShopItemDbModelToShopItem (shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enadled = shopItemDbModel.enadled
    )

    fun mapListDbModelToShopItem(list: List<ShopItemDbModel>) = list.map {
            mapShopItemDbModelToShopItem(it)
    }

}