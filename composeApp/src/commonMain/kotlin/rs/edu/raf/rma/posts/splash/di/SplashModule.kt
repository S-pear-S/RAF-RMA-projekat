package rs.edu.raf.rma.posts.splash.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import rs.edu.raf.rma.posts.splash.SplashViewModel

val splashModule = module {
    viewModelOf(::SplashViewModel)
}
