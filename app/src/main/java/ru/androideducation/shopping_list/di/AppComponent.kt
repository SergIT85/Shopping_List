package ru.androideducation.shopping_list.di

import android.app.Activity
import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.androideducation.shopping_list.ApplicationMy
import ru.androideducation.shopping_list.presentation.MainActivity
import ru.androideducation.shopping_list.presentation.ShopItemActivity
import ru.androideducation.shopping_list.presentation.itemfragment.FragmentShopItem

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: ShopItemActivity)
    fun inject(fragment: FragmentShopItem)
    fun inject(application: ApplicationMy)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }
}