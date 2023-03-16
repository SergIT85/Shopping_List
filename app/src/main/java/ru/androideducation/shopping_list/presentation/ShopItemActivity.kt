package ru.androideducation.shopping_list.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem
import ru.androideducation.shopping_list.presentation.itemfragment.FragmentShopItem
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity(), FragmentShopItem.OnEditingFinishedListener {

    private var screenMod = UNKNOWN_MOD
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if (savedInstanceState == null) {
            launchScreenMod()
        }
    }

    private fun launchScreenMod() {
        val fragment = when (screenMod) {
            OPEN_SCREEN_ADD_ITEM -> {
                Log.d("ShopItemActivity", "BBBBBBBBBBB$screenMod")
                FragmentShopItem.newInstanceAddItem()
            }
            OPEN_SCREEN_EDIT_ITEM -> {
                FragmentShopItem.newInstanceEditItem(shopItemId)
            }
            else -> throw RuntimeException("Unknown screen mod $screenMod")
            //OPEN_SCREEN_EDIT_ITEM -> launchEditMod()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()

    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Parse in intent screen mod is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != OPEN_SCREEN_ADD_ITEM && mode != OPEN_SCREEN_EDIT_ITEM) {
            throw RuntimeException("Parse in intent screen mod is unknown - $mode")
        }
        screenMod = mode
        if (screenMod == OPEN_SCREEN_EDIT_ITEM) {
            if (!intent.hasExtra(SHOP_ITEM_ID)) {
                throw RuntimeException("Parse in intent id Shop Item is absent")
            }
            shopItemId = intent.getIntExtra(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "add_shop_item"
        private const val OPEN_SCREEN_ADD_ITEM = "open_screen_add_item"
        private const val OPEN_SCREEN_EDIT_ITEM = "open_screen_edit_item"
        private const val SHOP_ITEM_ID = "SHOP_ITEM_ID"
        private const val UNKNOWN_MOD = ""

        fun addShopItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, OPEN_SCREEN_ADD_ITEM)
            return intent
        }

        fun editShopItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, OPEN_SCREEN_EDIT_ITEM)
            intent.putExtra(SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    override fun onEditingFinishedListener() {
        Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()
        finish()
    }
}