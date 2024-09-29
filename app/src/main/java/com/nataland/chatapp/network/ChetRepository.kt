package com.nataland.chatapp.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.nataland.MessageQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val SERVER_URL = "https://chatapp-production-69b3.up.railway.app"

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

interface ChatRepository {
    suspend fun message(input: String): Result<MessageQuery.Data>
}

class ChatRepositoryImpl @Inject constructor() : ChatRepository {
    private val apolloClient = ApolloClient.Builder()
        .serverUrl(SERVER_URL)
        .build()

    override suspend fun message(
        input: String
    ): Result<MessageQuery.Data> {
        return withContext(Dispatchers.IO) {
            val response = apolloClient.query(MessageQuery(input)).execute()
            val data = response.data
            if (response.hasErrors()) {
                Result.Error(
                    ApolloGraphQLException(
                        response.errors?.firstOrNull() ?: Error.Builder(
                            "Something went wrong"
                        ).build()
                    )
                )
            } else if (data == null) {
                Result.Error(
                    ApolloGraphQLException(
                        Error.Builder(
                            "Data should not be empty"
                        ).build()
                    )
                )
            } else {
                Result.Success(data)
            }
        }
    }
}
