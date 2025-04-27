package com.korikmat.watchtogether.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.korikmat.data.source.remote.tmdbService.TMDBApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.GsonBuilder
import com.korikmat.data.repositories.MoviesRepositoryImpl
import com.korikmat.data.repositories.SessionRepositoryImpl
import com.korikmat.data.source.local.DatabaseLocalSource
import com.korikmat.data.source.local.SessionDataSource
import com.korikmat.data.source.local.roomDatabase.RoomRemoteImpl
import com.korikmat.data.source.local.session.SessionDataSourceImpl
import com.korikmat.data.source.remote.RemoteMoviesSource
import com.korikmat.data.source.remote.tmdbService.TMDBRemoteMoviesImpl
import com.korikmat.domain.repositories.MoviesRepository
import com.korikmat.domain.repositories.SessionRepository
import com.korikmat.domain.usecases.CreateNewUserUseCase
import com.korikmat.domain.usecases.DeleteUsersUseCase
import com.korikmat.domain.usecases.DislikeMoviesUseCase
import com.korikmat.domain.usecases.GetAllUsersUseCase
import com.korikmat.domain.usecases.GetCurrentUserUseCase
import com.korikmat.domain.usecases.GetMoviesUseCase
import com.korikmat.domain.usecases.LikeMoviesUseCase
import com.korikmat.domain.usecases.LogOutUseCase
import com.korikmat.domain.usecases.ResetUserDataUseCase
import com.korikmat.domain.usecases.SearchInDislikedMoviesUseCase
import com.korikmat.domain.usecases.SearchInLikedMoviesUseCase
import com.korikmat.domain.usecases.SetCurrentUserUseCase
import com.korikmat.domain.usecases.UpdateUserDetailsUseCase
import com.korikmat.watchtogether.presentation.ui.viewModels.CreateUserViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.DislikedMoviesViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.EditUserProfileViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.FavoriteMoviesViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.MatchesViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.MovieSelectionViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.SelectUserScreenViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.UserProfileViewModel
import com.korikmat.watchtogether.presentation.ui.viewModels.WatchTogetherAppViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.viewModel
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val networkModule = module {

    single {
        GsonBuilder()
            .create()
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    single<TMDBApiService> {
        val retrofit: Retrofit = get()
        retrofit.create(TMDBApiService::class.java)
    }
}

val databaseModule = module {
    single<DatabaseLocalSource> {
        Room.databaseBuilder(
            get(),
            RoomRemoteImpl::class.java,
            "movie_database"
        ).build()
    }

}

val dataModule = module {
    single<RemoteMoviesSource> { TMDBRemoteMoviesImpl(get(), "5f000ff0bb4a038162e26c4088a2b617") }
    single<MoviesRepository> { MoviesRepositoryImpl(get(), get()) }
    single<SessionRepository> { SessionRepositoryImpl(get(), get()) }
}

val dataStoreModule = module {
    single<CoroutineDispatcher> { Dispatchers.IO }
    single<CoroutineScope> {
        val io = get<CoroutineDispatcher>()
        CoroutineScope(SupervisorJob() + io)
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(get<CoroutineDispatcher>()),
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { get<Context>().preferencesDataStoreFile("settings_ds") }
        )
    }

    single<SessionDataSource> { SessionDataSourceImpl(get()) }

}

val domainModule = module {
    factory { GetMoviesUseCase(get(), get()) }
    factory { LikeMoviesUseCase(get(), get()) }
    factory { DislikeMoviesUseCase(get(), get()) }
    factory { SearchInLikedMoviesUseCase(get(), get()) }
    factory { SearchInDislikedMoviesUseCase(get(), get()) }
    factory { GetCurrentUserUseCase(get(), get()) }
    factory { UpdateUserDetailsUseCase(get()) }
    factory { ResetUserDataUseCase(get(), get()) }
    factory { CreateNewUserUseCase(get()) }
    factory { GetAllUsersUseCase(get()) }
    factory { SetCurrentUserUseCase(get()) }
    factory { DeleteUsersUseCase(get()) }
    factory { LogOutUseCase(get()) }
}

val viewModelModule = module {
    viewModel { WatchTogetherAppViewModel(get(), get()) }
    viewModel { MovieSelectionViewModel(get(), get(), get()) }
    viewModel { MatchesViewModel() }
    viewModel { FavoriteMoviesViewModel(get(), get()) }
    viewModel { DislikedMoviesViewModel(get(), get()) }
    viewModel { UserProfileViewModel(get(), get(), get()) }
    viewModel { EditUserProfileViewModel(get(), get()) }
    viewModel { CreateUserViewModel(get())}
    viewModel { SelectUserScreenViewModel(get(), get(), get()) }
}

val appModules = listOf(
    networkModule,
    dataModule,
    domainModule,
    viewModelModule,
    databaseModule,
    dataStoreModule,
)