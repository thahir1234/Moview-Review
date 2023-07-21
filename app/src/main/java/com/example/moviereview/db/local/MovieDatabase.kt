package com.example.moviereview.db.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviereview.db.local.dao.*
import com.example.moviereview.db.local.entities.*

@Database(
    entities = [
    Accounts::class,Casts::class,Comments::class, FavouriteMovies::class,Genres::class,LikedLists::class,LikedReviews::class,ListMovies::class,Lists::class,Movies::class,Reviews::class,SimilarMovies::class,UserMovies::class,Users::class,TrendingMovies::class,RecentMovies::class,PopularMovies::class,ActionMovies::class,AdventureMovies::class,AnimationMovies::class,RomanceMovies::class,CrimeMovies::class,FavouriteLists::class,LoadedStatus::class],
    version = 1,
    exportSchema = false
    )
@TypeConverters(TypeConvertor::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun accountsDao() : AccountsDao

    abstract fun castsDao() : CastsDao

    abstract fun commentsDao() : CommentsDao

    abstract fun favouriteMoviesDao() : FavouriteMoviesDao

    abstract fun genresDao() : GenresDao

    abstract fun likedListsDao() : LikedListsDao

    abstract fun likedReviewsDao() : LikedReviewsDao

    abstract fun listMoviesDao() : ListMoviesDao

    abstract fun listsDao() : ListsDao

    abstract fun moviesDao() : MoviesDao

    abstract fun reviewsDao() : ReviewsDao

    abstract fun similarMoviesDao() : SimilarMoviesDao

    abstract fun userMoviesDao() : UserMoviesDao

    abstract fun usersDao() : UsersDao

    abstract fun trendingMoviesDao(): TrendingMoviesDao

    abstract fun recentMoviesDao(): RecentMoviesDao

    abstract fun popularMoviesDao(): PopularMoviesDao

    abstract fun actionMoviesDao() : ActionMoviesDao

    abstract fun adventureMoviesDao() : AdventureMoviesDao

    abstract fun animationMoviesDao() : AnimationMoviesDao

    abstract fun romanceMoviesDao() : RomanceMoviesDao

    abstract fun crimeMoviesDao() : CrimeMoviesDao

    abstract fun favouriteListsDao() : FavouriteListsDao

    abstract fun loadedStatusDao() : LoadedStatusDao

    companion object{
        @Volatile
        private var Instance:MovieDatabase? = null

        fun getDatabase(context: Context) : MovieDatabase?
        {
            val tempInstance = Instance

            if(tempInstance!=null)
            {
                return tempInstance
            }

            synchronized(this)
            {
                Instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                ).build()
                return Instance
            }

        }
    }

}