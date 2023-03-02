package ru.androideducation.shopping_list.presentation.itemfragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import ru.androideducation.shopping_list.R
import ru.androideducation.shopping_list.domain.ShopItem
import ru.androideducation.shopping_list.presentation.ShopItemActivity
import ru.androideducation.shopping_list.presentation.ShopItemViewModel
import java.lang.RuntimeException

class FragmentShopItem : Fragment() {

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
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addChangeSetListener()
        launchScreenMod()

        errorInputVisible()
        closeScreen()
    }


    private fun closeScreen() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun errorInputVisible() {
        viewModel.errorInputName.observe(viewLifecycleOwner, Observer {
            val message = if (it) {
                getString(R.string.error)
            } else {
                null
            }
            tilName.error = message
        })

        viewModel.errorInputCount.observe(viewLifecycleOwner, Observer {
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
        viewModel.shopItem.observe(viewLifecycleOwner, Observer { it ->
            elName.setText(it.name)
            etCount.setText(it.id.toString())
        })
        saveButton.setOnClickListener {
            viewModel.editShopItem(elName.text?.toString(), etCount.text?.toString())
        }
        Log.d("ShopItemActivity", "$screenMod id= $shopItemId")
    }

    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Parse in intent screen mod is absent")
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (mode != OPEN_SCREEN_ADD_ITEM && mode != OPEN_SCREEN_EDIT_ITEM) {
            throw RuntimeException("Parse in intent screen mod is unknown - $mode")
        }
        screenMod = mode
        if (screenMod == OPEN_SCREEN_EDIT_ITEM) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Parse in intent id Shop Item is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        titCount = view.findViewById(R.id.tit_count)
        elName = view.findViewById(R.id.el_name)
        etCount = view.findViewById(R.id.et_count)
        saveButton = view.findViewById(R.id.save_button)
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "add_shop_item"
        private const val OPEN_SCREEN_ADD_ITEM = "open_screen_add_item"
        private const val OPEN_SCREEN_EDIT_ITEM = "open_screen_edit_item"
        private const val SHOP_ITEM_ID = "SHOP_ITEM_ID"
        private const val UNKNOWN_MOD = ""

        fun newInstanceAddItem() : FragmentShopItem {
            return FragmentShopItem().apply {
                arguments = Bundle().apply {
                    Log.d("ShopItemActivity", "AAAAAAAAAAAA$screenMod")
                    putString(EXTRA_SCREEN_MODE, OPEN_SCREEN_ADD_ITEM)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int) : FragmentShopItem {
            return FragmentShopItem().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, OPEN_SCREEN_EDIT_ITEM)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }

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