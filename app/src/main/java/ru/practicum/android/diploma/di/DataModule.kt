package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.HeadHunterApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.impl.RetrofitNetworkClient
import ru.practicum.android.diploma.util.mappers.AreaMapper
import ru.practicum.android.diploma.util.mappers.VacancyMapper

val filtersQualifier = named("filters")
val dataModule = module {
    single<SharedPreferences>(filtersQualifier) {
        androidContext().getSharedPreferences(
            "local_storage",
            Context.MODE_PRIVATE
        )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        ).fallbackToDestructiveMigration().build()
    }

    single<HeadHunterApi> {
        Retrofit.Builder().baseUrl("https://api.hh.ru").addConverterFactory(GsonConverterFactory.create()).build()
            .create(HeadHunterApi::class.java)
    }

    factory { Gson() }

    single { VacancyMapper(get()) }

    single { AreaMapper() }

    single<ConnectivityManager> {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            get(),
            get()
        )
    }
}
