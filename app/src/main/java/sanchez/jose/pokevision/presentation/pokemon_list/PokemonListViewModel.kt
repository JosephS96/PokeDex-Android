package sanchez.jose.pokevision.presentation.pokemon_list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import sanchez.jose.pokevision.data.PokedexListEntry
import sanchez.jose.pokevision.data.repository.PokemonRepositoryImpl
import sanchez.jose.pokevision.util.Constants.PAGE_SIZE
import sanchez.jose.pokevision.util.Resource
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepositoryImpl
): ViewModel() {

    private var currentPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        // loadPokemonPaginated()
        loadPokemonPaginatedFlow()
    }

    fun loadPokemonPaginatedFlow() {
        viewModelScope.launch {
            repository.getPokemonListFlow(PAGE_SIZE, currentPage * PAGE_SIZE)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            endReached.value = currentPage * PAGE_SIZE >= result.data!!.count()
                            val pokedexEntries = result.data.mapIndexed { index, entry ->
                                // PokedexListEntry(entry.name.capitalize(Locale.ROOT), url)
                                PokedexListEntry(entry.name.capitalize(Locale.ROOT), entry.url, entry.number)
                            }
                            currentPage++
                            println("Current page $currentPage")
                            loadError.value = ""
                            isLoading.value = false
                            pokemonList.value = pokedexEntries ?: emptyList()
                        }
                        is Resource.Error -> {
                            loadError.value = result.message ?: ""
                        }
                        is Resource.Loading -> {
                            isLoading.value = true
                        }
                    }
                }
        }
    }

    /*
    fun loadPokemonPaginated() {
        viewModelScope.launch {
            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    currentPage++

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Loading -> {

                }
            }
        }
    }*/
}