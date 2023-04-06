package ru.androideducation.shopping_list.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import ru.androideducation.shopping_list.ApplicationMy
import ru.androideducation.shopping_list.domain.ShopItem
import javax.inject.Inject

class ShopListProviders : ContentProvider() {

    private val component by lazy {
        (context as ApplicationMy).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("ru.androideducation.shopping_list", "shop_items", CODE_SHOP_ITEMS)
        addURI("ru.androideducation.shopping_list", "shop_items/*", CODE_SHOP_ITEMS_STRING)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return when (uriMatcher.match(p0)) {
            CODE_SHOP_ITEMS -> {
                shopListDao.getShopListCursor()
            }
            CODE_SHOP_ITEMS_STRING -> {

                null
            }
            else -> {
                null
            }
        }


    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        when (uriMatcher.match(p0)) {
            CODE_SHOP_ITEMS -> {
                if (p1 == null) return null
                val id = p1.getAsInteger("id")
                val name = p1.getAsString("name")
                val count = p1.getAsInteger("count")
                val enabled = p1.getAsBoolean("enabled")
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enadled = enabled
                )
                shopListDao.addShopItem(mapper.mapShopItemToShopItemDbModel(shopItem))
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when (uriMatcher.match(uri)) {
            CODE_SHOP_ITEMS -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                return shopListDao.deleteShopItemSinc(id)
            }
        }
        return 0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val CODE_SHOP_ITEMS = 100
        private const val CODE_SHOP_ITEMS_STRING = 777
    }
}