package com.sutonglabs.tracestore.use_case

import com.sutonglabs.tracestore.common.Resource
import com.sutonglabs.tracestore.models.CartResponse
import com.sutonglabs.tracestore.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<Resource<CartResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = repository.getCart()

            emit(Resource.Success(response))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check internet connection."))
        }
    }
}
