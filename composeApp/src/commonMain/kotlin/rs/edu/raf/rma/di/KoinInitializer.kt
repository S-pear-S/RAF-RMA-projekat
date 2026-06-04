package rs.edu.raf.rma.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import rs.edu.raf.rma.auth.di.authViewModelModule
import rs.edu.raf.rma.core.auth.di.authModule
import rs.edu.raf.rma.core.db.di.databaseModule
import rs.edu.raf.rma.favorites.di.favoritesModule
import rs.edu.raf.rma.movies.di.moviesModule
import rs.edu.raf.rma.networking.di.networkingModule
import rs.edu.raf.rma.posts.splash.di.splashModule
import rs.edu.raf.rma.profile.di.profileModule
import rs.edu.raf.rma.quiz.di.quizModule

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(
            databaseModule(),
            networkingModule,
            authModule,
            splashModule,
            authViewModelModule,
            moviesModule,
            favoritesModule,
            quizModule,
            profileModule,
        )
    }
}
