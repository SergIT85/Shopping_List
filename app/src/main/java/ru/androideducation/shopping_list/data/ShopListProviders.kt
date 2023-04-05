package ru.androideducation.shopping_list.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import ru.androideducation.shopping_list.ApplicationMy
import javax.inject.Inject

class ShopListProviders: ContentProvider() {

    private val component by lazy {
        (context as ApplicationMy).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao

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
        return when(uriMatcher.match(p0)) {
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
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val CODE_SHOP_ITEMS = 100
        private const val CODE_SHOP_ITEMS_STRING = 777
    }
}