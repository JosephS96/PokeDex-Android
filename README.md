
# PokeDex - Android

This project showcase a simple pokedex app which fetches data from the internet
and caches it in a local database.

This project aims to showcase in the future some of the best practices of modern
Android Development.



## Tech Stack

Made entirely with **Kotlin**.

**Android**: Minimum SDK 24

[Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + 
[Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/):
 Asyncronous work and reactive programming


**Jetpack**

- Compose: Declarative UI for Android
- ViewModel: Exposes state to the UI and encapsulates related business logic
- Room: Local database to persist data, used as a cache mechanisms for offline in this case
- Hilt: Dependency Injection

[**Retrofit**](https://github.com/square/retrofit): For network requests

## Architecture

PokeDex uses a MVVM + repository Architecture


## API Reference

Pokedex using the [PokeAPI](https://pokeapi.co) for constructing RESTful API.
PokeAPI provides a RESTful API interface to highly detailed objects built from 
thousands of lines of data related to Pokémon.

