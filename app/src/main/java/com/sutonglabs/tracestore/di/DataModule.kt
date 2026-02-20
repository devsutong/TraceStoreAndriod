package com.sutonglabs.tracestore.di

import android.content.Context
import com.sutonglabs.tracestore.api.TraceStoreAPI
import com.sutonglabs.tracestore.data.auth.TokenProvider
import com.sutonglabs.tracestore.repository.AddressRepository
import com.sutonglabs.tracestore.repository.AddressRepositoryImp
import com.sutonglabs.tracestore.repository.CartRepository
import com.sutonglabs.tracestore.repository.CartRepositoryImp
import com.sutonglabs.tracestore.repository.OrderRepository
import com.sutonglabs.tracestore.repository.OrderRepositoryImp
import com.sutonglabs.tracestore.repository.ProductRepository
import com.sutonglabs.tracestore.repository.ProductRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideProductRepository(
        traceStoreAPIService: TraceStoreAPI,
        tokenProvider: TokenProvider
    ): ProductRepository {
        return ProductRepositoryImp(traceStoreAPIService, tokenProvider)
    }


    @Provides
    fun provideCartRepository(
        traceStoreAPIService: TraceStoreAPI,
        @ApplicationContext context: Context
    ): CartRepository {
        return CartRepositoryImp(traceStoreAPIService, context)
    }


    @Provides
    fun provideAddressRepository(
        traceStoreAPIService: TraceStoreAPI
    ): AddressRepository {
        return AddressRepositoryImp(traceStoreAPIService)
    }
    @Singleton
    @Provides
    fun provideOrderRepository(
        traceStoreAPIService: TraceStoreAPI
    ): OrderRepository {
        return OrderRepositoryImp(traceStoreAPIService)
    }
}