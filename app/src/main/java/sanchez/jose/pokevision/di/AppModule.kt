package sanchez.jose.pokevision.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sanchez.jose.pokevision.data.local.PokeVisionDatabase
import sanchez.jose.pokevision.data.local.dao.ResultDao
import sanchez.jose.pokevision.data.remote.PokeApi
import sanchez.jose.pokevision.data.repository.PokemonRepositoryImpl
import sanchez.jose.pokevision.util.Constants.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi,
        resultDao: ResultDao
    ) = PokemonRepositoryImpl(api, resultDao)

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }

    @Singleton
    @Provides
    fun providePokeVisionDatabase(@ApplicationContext appContext: Context): PokeVisionDatabase {
        return PokeVisionDatabase.getInstance(appContext)
    }

    @Provides
    fun provideResultDao(pokeVisionDatabase: PokeVisionDatabase): ResultDao {
        return pokeVisionDatabase.resultDao()
    }
}
