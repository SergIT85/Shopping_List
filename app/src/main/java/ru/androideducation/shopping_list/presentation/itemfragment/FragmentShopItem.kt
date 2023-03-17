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
import ru.androideducation.shopping_list.databinding.FragmentShopItemBinding
import ru.androideducation.shopping_list.domain.ShopItem
import ru.androideducation.shopping_list.presentation.ShopItemActivity
import ru.androideducation.shopping_list.presentation.ShopItemViewModel
import kotlin.RuntimeException

class FragmentShopItem : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMod = UNKNOWN_MOD
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShopItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        addChangeSetListener()
        launchScreenMod()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        closeScreen()
    }


    private fun closeScreen() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinishedListener()
        }
    }


    private fun launchScreenMod() {
        when (screenMod) {
            OPEN_SCREEN_ADD_ITEM -> launchAddMod()
            OPEN_SCREEN_EDIT_ITEM -> launchEditMod()
        }

    }

    private fun addChangeSetListener() {
        binding.elName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.etCount.addTextChangedListener(object : TextWatcher {
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
        binding.saveButton.setOnClickListener {
            viewModel.addShopItem(binding.elName.text.toString(), binding.etCount.text.toString())
            Log.d("ShopItemActivity", "$screenMod")
        }
    }

    private fun launchEditMod() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner, Observer { it ->
            binding.elName.setText(it.name)
            binding.etCount.setText(it.id.toString())
        })
        binding.saveButton.setOnClickListener {
            viewModel.editShopItem(binding.elName.text?.toString(), binding.etCount.text?.toString())
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

    companion object {
        private const val EXTRA_SCREEN_MODE = "add_shop_item"
        private const val OPEN_SCREEN_ADD_ITEM = "open_screen_add_item"
        private const val OPEN_SCREEN_EDIT_ITEM = "open_screen_edit_item"
        private const val SHOP_ITEM_ID = "SHOP_ITEM_ID"
        private const val UNKNOWN_MOD = ""

        fun newInstanceAddItem(): FragmentShopItem {
            return FragmentShopItem().apply {
                arguments = Bundle().apply {
                    Log.d("ShopItemActivity", "AAAAAAAAAAAA$screenMod")
                    putString(EXTRA_SCREEN_MODE, OPEN_SCREEN_ADD_ITEM)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): FragmentShopItem {
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

    interface OnEditingFinishedListener {
        fun onEditingFinishedListener()
    }
}