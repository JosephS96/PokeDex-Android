package sanchez.jose.pokevision.data.repository

import dagger.hilt.android.scopes.ActivityScoped
import sanchez.jose.pokevision.data.remote.PokeApi
import sanchez.jose.pokevision.data.remote.responses.Pokemon
import sanchez.jose.pokevision.data.remote.responses.PokemonList
import sanchez.jose.pokevision.util.Resource
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApi
){
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred")
        }

        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error(e.stackTraceToString())
        }

        return Resource.Success(response)
    }
}