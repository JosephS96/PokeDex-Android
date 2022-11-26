package sanchez.jose.pokevision.data.remote.responses


data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<ResultResponse>
)