package ru.androideducation.shopping_list.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

class ShopListProviders: ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("ru.androideducation.shopping_list", "shop_items", CODE_SHOP_ITEMS)
        addURI("ru.androideducation.shopping_list", "shop_items/*", CODE_SHOP_ITEMS_STRING)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val code = uriMatcher.match(p0)

        when(code) {
            CODE_SHOP_ITEMS -> {
                Log.d("ShopListProviders", "ShopListProviders query $p0 code - $code" )
            }
            CODE_SHOP_ITEMS_STRING -> {
                Log.d("ShopListProviders", "ShopListProviders query $p0 code - $code" )
            }
        }

        return null
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