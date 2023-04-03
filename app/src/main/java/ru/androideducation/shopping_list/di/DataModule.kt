package ru.androideducation.shopping_list.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.androideducation.shopping_list.data.AppDatabase
import ru.androideducation.shopping_list.data.ShopListDao
import ru.androideducation.shopping_list.data.ShopListRepositoryImpl
import ru.androideducation.shopping_list.domain.ShopListRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideDao(
            application: Application
        ): ShopListDao {
            return AppDatabase.getInstanceDb(application).shopListDao()
        }
    }
}