package ru.androideducation.shopping_list.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var titCount: TextInputLayout
    private lateinit var elName: EditText
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button

    private var screenMod = UNKNOWN_MOD
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        addChangeSetListener()
        launchScreenMod()
        errorInputVisible()
        closeScreen()
    }

    private fun closeScreen() {
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun errorInputVisible() {
        viewModel.errorInputName.observe(this, Observer {
            val message = if (it) {
                getString(R.string.error)
            } else {
                null
            }
            tilName.error = message
        })

        viewModel.errorInputCount.observe(this, Observer {
            val message = if (it) {
                getString(R.string.error)
            } else {
                null
            }
            titCount.error = message
        })
    }

    private fun launchScreenMod() {
        when (screenMod) {
            OPEN_SCREEN_ADD_ITEM -> launchAddMod()
            OPEN_SCREEN_EDIT_ITEM -> launchEditMod()
        }

    }

    private fun addChangeSetListener() {
        elName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun launchAddMod() {
        saveButton.setOnClickListener {
            viewModel.addShopItem(elName.text.toString(), etCount.text.toString())
            Log.d("ShopItemActivity", "$screenMod")
        }
    }

    private fun launchEditMod() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this, Observer { it ->
            elName.setText(it.name)
            etCount.setText(it.id.toString())
        })
        saveButton.setOnClickListener {
            viewModel.editShopItem(elName.text?.toString(), etCount.text?.toString())
        }
        Log.d("ShopItemActivity", "$screenMod id= $shopItemId")
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

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        titCount = findViewById(R.id.tit_count)
        elName = findViewById(R.id.el_name)
        etCount = findViewById(R.id.et_count)
        saveButton = findViewById(R.id.save_button)
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
}