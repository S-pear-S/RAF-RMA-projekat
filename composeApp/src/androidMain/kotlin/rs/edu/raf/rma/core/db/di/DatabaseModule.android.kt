package rs.edu.raf.rma.core.db.di

import org.koin.dsl.module
import rs.edu.raf.rma.core.db.AppDatabase
import rs.edu.raf.rma.core.db.buildAppDatabase
import rs.edu.raf.rma.core.db.getDatabaseBuilder
import rs.edu.raf.rma.movies.db.MovieDao

actual fun databaseModule() = module {
    single<AppDatabase> {
        buildAppDatabase(builder = getDatabaseBuilder(context = get()))
    }
    single<MovieDao> { get<AppDatabase>().movieDao() }
}
