package com.sutonglabs.tracestore.di

import com.sutonglabs.tracestore.api.TraceStoreAPI
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
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideProductRepository(traceStoreAPIService: TraceStoreAPI): ProductRepository {
        return ProductRepositoryImp(traceStoreAPIService)
    }

    @Provides
    fun provideCartRepository(
        // Add dependencies if necessary, e.g., DAOs, Retrofit services, etc.
        traceStoreAPIService: TraceStoreAPI
    ): CartRepository {
        return CartRepositoryImp(traceStoreAPIService)
    }

    @Provides
    fun provideAddressRepository(
        traceStoreAPIService: TraceStoreAPI
    ): AddressRepository {
        return AddressRepositoryImp(traceStoreAPIService)
    }

    @Provides
    fun provideOrderRepository(
        traceStoreAPIService: TraceStoreAPI
    ): OrderRepository {
        return OrderRepositoryImp(traceStoreAPIService)
    }
}