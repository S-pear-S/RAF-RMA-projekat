package rs.edu.raf.rma.posts.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import rs.edu.raf.rma.posts.data.PostRepositoryImpl
import rs.edu.raf.rma.posts.details.PostDetailsViewModel
import rs.edu.raf.rma.posts.domain.PostRepository
import rs.edu.raf.rma.posts.list.PostsListViewModel
import rs.edu.raf.rma.posts.pager.PostsPagerViewModel

val postsModule = module {
    single { PostRepositoryImpl(beskarApi = get()) } bind PostRepository::class
    viewModelOf(::PostsListViewModel)
    viewModelOf(::PostDetailsViewModel)
    viewModelOf(::PostsPagerViewModel)
}
