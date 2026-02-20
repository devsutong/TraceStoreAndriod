package com.sutonglabs.tracestore.use_case

import android.content.Context
import com.sutonglabs.tracestore.models.Product
import com.sutonglabs.tracestore.common.Resource
import com.sutonglabs.tracestore.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(id: Int, context: Context): Flow<Resource<Product?>> = flow {
        emit(Resource.Loading()) // Emit loading state

        try {
            // Fetch product details from the repository
            val product = productRepository.getProductDetail(id)

            // Emit success state with the fetched product
            if (product != null) {
                emit(Resource.Success(product))
            } else {
                // If no product found, emit an error state
                emit(Resource.Error("Product not found"))
            }
        } catch (e: Exception) {
            // Handle any exceptions and emit error state
            emit(Resource.Error("An error occurred: ${e.message}"))
        }
    }
}
