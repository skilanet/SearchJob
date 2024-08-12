package ru.practicum.android.diploma.domain.vacancyinfo.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.domain.favorites.FavoritesRepository
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyInfoResource
import ru.practicum.android.diploma.domain.models.VacancySearchResultType
import ru.practicum.android.diploma.domain.vacancyinfo.VacancyInfoInteractor
import ru.practicum.android.diploma.domain.vacancyinfo.VacancyInfoRepository

class VacancyInteractorImpl(
    private val vacancyRepository: VacancyInfoRepository,
    private val favoritesRepository: FavoritesRepository,
) : VacancyInfoInteractor {
    override suspend fun loadVacancy(id: String): Flow<VacancyInfoResource> = flow {
        vacancyRepository.searchVacancy(id).collect { resource ->
            when (resource.resultType) {
                VacancySearchResultType.SUCCESS -> {
                    if (resource.data != null) {
                        checkAndUpdateFavorites(resource.data)
                        emit(VacancyInfoResource(resource.data, false))
                    }
                }

                VacancySearchResultType.NOT_FOUND -> {
                    favoritesRepository.removeFromFavorites(id)
                    emit(VacancyInfoResource(null, true))
                }

                VacancySearchResultType.NETWORK_ERROR -> {
                    favoritesRepository.getVacancyById(id).collect { vacancy ->
                        if (vacancy != null) {
                            emit(VacancyInfoResource(vacancy, false))
                        } else {
                            emit(VacancyInfoResource(null, true))
                        }
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun checkAndUpdateFavorites(vacancy: VacancyFull) {
        favoritesRepository.checkVacancyInFavorites(vacancy.id).collect { inFavorites ->
            if (inFavorites) {
                favoritesRepository.updateFavoriteVacancy(vacancy)
            }
        }
    }
}
