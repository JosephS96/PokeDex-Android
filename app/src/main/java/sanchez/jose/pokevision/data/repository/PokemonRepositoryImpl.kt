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
    private val pokemonDao: PokemonDao,
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
            query = { resultDao.getPokemonList(limit, offset) },
            fetch = { api.getPokemonList(limit, offset) },
            saveFetchResult = { pokemonList ->
                pokemonList.results.forEachIndexed { index, resultResponse ->
                    val entity = ResultEntity(
                        id = index * offset + 1,
                        name = resultResponse.name,
                        url = resultResponse.url
                    )
                    resultDao.insertResult(entity)
                }
                              },
            toDomainType = { list ->
                list.map {
                    Result(
                        name = it.name,
                        url = it.url
                    )
                }
            }
        )
    }
}