package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.AppDatabase

val filtersQualifier = named("filters")
val dataModule = module {
    single<SharedPreferences>(filtersQualifier) {
        androidContext().getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").fallbackToDestructiveMigration()
            .build()
    }

    factory { Gson() }
}
