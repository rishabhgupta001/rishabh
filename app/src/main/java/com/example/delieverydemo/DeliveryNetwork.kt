package com.example.delieverydemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.delievery.datasource.factory.NetDeliveryDataSourceFactory
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE
import javax.security.auth.callback.Callback

class DeliveryNetwork {
    private val TAG = DeliveryNetwork::class.java.simpleName
    private lateinit var deliveryPaged: LiveData<PagedList<DeliveryResponseModel>>
    private lateinit var networkState: LiveData<NetworkState>

    operator fun invoke(
        dataSourceFactory: NetDeliveryDataSourceFactory,
        boundryCallback: PagedList.BoundaryCallback<DeliveryResponseModel>
    ) {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(LOADING_PAGE_SIZE)
            .build()

        //networkState = Transformations.switchMap(dataSourceFactory)

    }
}

/* final private static String TAG = MoviesNetwork.class.getSimpleName();
    final private LiveData<PagedList<Movie>> moviesPaged;
    final private LiveData<NetworkState> networkState;

    public MoviesNetwork(NetMoviesDataSourceFactory dataSourceFactory, PagedList.BoundaryCallback<Movie> boundaryCallback){
        PagedList.Config pagedListConfig = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(LOADING_PAGE_SIZE).setPageSize(LOADING_PAGE_SIZE).build();
        networkState = Transformations.switchMap(dataSourceFactory.getNetworkStatus(),
                (Function<NetMoviesPageKeyedDataSource, LiveData<NetworkState>>)
                        NetMoviesPageKeyedDataSource::getNetworkState);
        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);
        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
        moviesPaged = livePagedListBuilder.
                setFetchExecutor(executor).
                setBoundaryCallback(boundaryCallback).
                build();

    }


    public LiveData<PagedList<Movie>> getPagedMovies(){
        return moviesPaged;
    }



    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }
*/