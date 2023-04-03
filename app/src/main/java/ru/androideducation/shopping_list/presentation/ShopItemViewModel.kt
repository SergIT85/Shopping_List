package ru.androideducation.shopping_list.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.androideducation.shopping_list.data.ShopListRepositoryImpl
import ru.androideducation.shopping_list.domain.AddShopItemUseCase
import ru.androideducation.shopping_list.domain.EditShopItemUseCase
import ru.androideducation.shopping_list.domain.GetShopItemUseCase
import ru.androideducation.shopping_list.domain.ShopItem
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val getShopItemUseCase : GetShopItemUseCase,
    private val addShopItemUseCase : AddShopItemUseCase,
    private val editShopItemUseCase : EditShopItemUseCase,
) : ViewModel() {


    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val shopItem = getShopItemUseCase.getShopItemById(id)
            _shopItem.postValue(shopItem)
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {

            val name = parsName(inputName)
            val count = parsCount(inputCount)
            val validInput = validInput(name, count)

            if (validInput) {
                viewModelScope.launch(Dispatchers.IO) {
                    val shopItem = ShopItem(name, count, true)
                    addShopItemUseCase.addShopItem(shopItem)
                    finishWork()
                }
            }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {

            val name = parsName(inputName)
            val count = parsCount(inputCount)
            val validInput = validInput(name, count)

            if (validInput) {
                _shopItem.value?.let {
                    viewModelScope.launch(Dispatchers.IO) {
                        val item = it.copy(name = name, count = count)
                        editShopItemUseCase.changeShopItemById(item)
                        finishWork()
                    }
                }
            }
    }

    private fun parsName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parsCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            Log.d("parsCountException", "Count: $inputCount in not number")
            0
        }
    }

    private fun validInput(inputName: String, inputCount: Int): Boolean {
        var result = true
        if (inputName.isBlank()) {
            _errorInputName.value = true
            result = false
        } else {
            _errorInputName.value = false
        }
        if (inputCount <= 0) {
            _errorInputCount.value = true
            result = false
        } else {
            _errorInputCount.value = false
        }
        return result
    }

    private fun finishWork() {
        _shouldCloseScreen.postValue(Unit)
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }
}