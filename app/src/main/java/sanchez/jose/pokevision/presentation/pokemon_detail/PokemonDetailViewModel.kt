package sanchez.jose.pokevision.presentation.pokemon_detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sanchez.jose.pokevision.data.remote.responses.Pokemon
import sanchez.jose.pokevision.data.repository.PokemonRepositoryImpl
import sanchez.jose.pokevision.util.Resource
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepositoryImpl
): ViewModel() {

    var pokemonInfo = mutableStateOf<Pokemon?>(null)

    suspend fun loadPokemonDetails(name: String): Resource<Pokemon> {
        return repository.getPokemonInfo(name)
    }
}