package rs.edu.raf.rma.passwords.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import rs.edu.raf.rma.passwords.details.PasswordDetailsViewModel
import rs.edu.raf.rma.passwords.domain.PasswordRepository
import rs.edu.raf.rma.passwords.list.PasswordsListViewModel
import rs.edu.raf.rma.passwords.repository.InMemoryPasswordRepository

val passwordsModule = module {
    single { InMemoryPasswordRepository() } bind PasswordRepository::class
    viewModelOf(::PasswordsListViewModel)
    viewModelOf(::PasswordDetailsViewModel)
}
