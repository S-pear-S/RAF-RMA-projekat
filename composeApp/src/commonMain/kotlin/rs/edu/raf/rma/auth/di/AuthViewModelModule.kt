package rs.edu.raf.rma.auth.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import rs.edu.raf.rma.auth.LoginViewModel
import rs.edu.raf.rma.auth.RegisterViewModel
import rs.edu.raf.rma.networking.MoviesApi
import rs.edu.raf.rma.networking.di.Qualifiers

val authViewModelModule = module {
    viewModel { LoginViewModel(api = get<MoviesApi>(Qualifiers.Unauthenticated), authStore = get()) }
    viewModel { RegisterViewModel(api = get<MoviesApi>(Qualifiers.Unauthenticated), authStore = get()) }
}
