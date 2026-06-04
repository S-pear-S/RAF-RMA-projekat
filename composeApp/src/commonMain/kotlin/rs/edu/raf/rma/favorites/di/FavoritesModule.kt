package rs.edu.raf.rma.favorites.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import rs.edu.raf.rma.favorites.FavoritesViewModel
import rs.edu.raf.rma.watchlist.WatchlistViewModel

val favoritesModule = module {
    viewModel { FavoritesViewModel(repository = get()) }
    viewModel { WatchlistViewModel(repository = get()) }
}
