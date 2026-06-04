package rs.edu.raf.rma.profile.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import rs.edu.raf.rma.networking.MoviesApi
import rs.edu.raf.rma.networking.di.Qualifiers
import rs.edu.raf.rma.profile.ProfileViewModel

val profileModule = module {
    viewModel {
        ProfileViewModel(
            authApi = get<MoviesApi>(Qualifiers.Authenticated),
            authStore = get(),
            repository = get(),
        )
    }
}
