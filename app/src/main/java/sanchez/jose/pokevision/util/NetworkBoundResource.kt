package sanchez.jose.pokevision.util

import kotlinx.coroutines.flow.*

/**
 * Class to handle data that comes from network and is cached locally,
 * this exposes a flow to the database as single source of truth
 *
 * @param NetworkType: The response type from the network request
 * @param CacheType: The entity type of the local database cache
 * @param DomainType: The simplified type used in the presentation/view layer
 *
 * @property query: local database query
 * @property fetch: remote request to fetch from the network
 * @property saveFetchResult: save the remote response into the local cache
 * @property onFetchFailed: action to take when the remote response fails
 * @property shouldFetch: logic to decide whether to fetch from the network or not
 */
inline fun <CacheType, NetworkType, DomainType> networkBoundResource(
    crossinline query: () -> Flow<CacheType>,
    crossinline fetch: suspend () -> NetworkType,
    crossinline saveFetchResult: suspend (NetworkType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
    crossinline shouldFetch: (CacheType) -> Boolean = { true },
    crossinline toDomainType: (CacheType) -> DomainType
) = flow<Resource<DomainType>> {
    emit(Resource.Loading(null))
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(toDomainType(data)))
        println("Network Bound Resource: Loading")
        try {
            println("Network Bound Resource: Fetching")
            saveFetchResult(fetch())
            println("Network Bound Resource: Success")
            query().map { Resource.Success(toDomainType(it)) }
        } catch (throwable: Throwable) {
            onFetchFailed(throwable)
            println("Network Bound Resource: Failed")
            query().map { Resource.Error("throwable", toDomainType(it)) }
        }
    } else {
        println("Network Bound Resource: Success")
        query().map { Resource.Success(toDomainType(it)) }
    }

    emitAll(flow)
}

/**
 * Example code of how to call it
    This should be called from a repository
fun getItems(request: MyRequest): Flow<Resource<List<MyItem>>> {
    return networkBoundResource(
        query = { dao.queryAll() },
        fetch = { retrofitService.getItems(request) },
        saveFetchResult = { items -> dao.insert(items) }
    )
}
 */

/**
 * Class to handle data that comes from network and is cached locally,
 * this exposes a flow to the database as single source of truth
 *
 * @param NetworkType:


inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow<Resource<ResultType>> {
    emit(Resource.Loading(null))
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            onFetchFailed(throwable)
            query().map { Resource.Error("throwable", it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}
 */