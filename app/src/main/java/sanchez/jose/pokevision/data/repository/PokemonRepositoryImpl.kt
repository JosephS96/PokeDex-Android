package sanchez.jose.pokevision.data.repository

import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import sanchez.jose.pokevision.data.local.dao.PokemonDao
import sanchez.jose.pokevision.data.local.dao.ResultDao
import sanchez.jose.pokevision.data.local.entities.ResultEntity
import sanchez.jose.pokevision.data.remote.PokeApi
import sanchez.jose.pokevision.data.remote.responses.Pokemon
import sanchez.jose.pokevision.data.remote.responses.PokemonListResponse
import sanchez.jose.pokevision.domain.model.PokemonList
import sanchez.jose.pokevision.domain.model.Result
import sanchez.jose.pokevision.util.Resource
import sanchez.jose.pokevision.util.networkBoundResource
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApi,
    //private val pokemonDao: PokemonDao,
    private val resultDao: ResultDao
){
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonListResponse> {
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

    fun getPokemonListFlow(limit: Int, offset: Int): Flow<Resource<List<Result>>> {
        return networkBoundResource(
            query = { resultDao.getPokemonList() },
            fetch = { api.getPokemonList(limit, offset) },
            saveFetchResult = { pokemonList ->
                pokemonList.results.forEachIndexed { index, resultResponse ->

                    val number = if(resultResponse.url.endsWith("/")) {
                        resultResponse.url.dropLast(1).takeLastWhile { it.isDigit() }
                    } else {
                        resultResponse.url.takeLastWhile { it.isDigit() }
                    }

                    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                    // Create database entity
                    val entity = ResultEntity(
                        id = number.toInt(),
                        name = resultResponse.name,
                        url = imageUrl
                    )
                    resultDao.insertResult(entity)
                }
                              },
            toDomainType = { list ->
                list.map {
                    Result(
                        name = it.name,
                        url = it.url,
                        number = it.id
                    )
                }
            },
            onFetchFailed = {
                println("Network Bound Resource: ${it.message}")
                println("Network Bound Resource: ${it.stackTraceToString()}")
            }
        )
    }
}