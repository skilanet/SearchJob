package ru.practicum.android.diploma.data.network.impl

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.network.HeadHunterApi
import ru.practicum.android.diploma.data.network.NetworkClient

class RetrofitNetworkClient(private val headHunterService: HeadHunterApi): NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return Response(BAD_REQUEST)
    }
    companion object{
        const val BAD_REQUEST = 500
    }
}
