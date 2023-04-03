package ru.androideducation.shopping_list.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.androideducation.shopping_list.presentation.MainViewModel
import ru.androideducation.shopping_list.presentation.ShopItemViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindViewModel(impl: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItem(impl: ShopItemViewModel): ViewModel
}