package rs.edu.raf.rma.movies.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import rs.edu.raf.rma.movies.data.MoviesRepositoryImpl
import rs.edu.raf.rma.movies.domain.MoviesRepository
import rs.edu.raf.rma.movies.list.MoviesListViewModel
import rs.edu.raf.rma.movies.detail.MovieDetailViewModel
import rs.edu.raf.rma.networking.MoviesApi
import rs.edu.raf.rma.networking.di.Qualifiers

val moviesModule = module {
    single<MoviesRepository> {
        MoviesRepositoryImpl(
            api = get<MoviesApi>(Qualifiers.Unauthenticated),
            authApi = get<MoviesApi>(Qualifiers.Authenticated),
            dao = get(),
        )
    }
    viewModel { MoviesListViewModel(get()) }
    viewModel { (movieId: String) -> MovieDetailViewModel(movieId, get()) }
}
