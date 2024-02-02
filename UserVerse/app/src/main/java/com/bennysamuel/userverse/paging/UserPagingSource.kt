//package com.bennysamuel.userverse.paging
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.bennysamuel.userverse.userdetailsretrofit.UserData
//import com.bennysamuel.userverse.userdetailsretrofit.UserDataApiService
//
//class UserPagingSource(val userApi: UserDataApiService):PagingSource<Int, UserData>()
//{
//    override fun getRefreshKey(state: PagingState<Int, UserData>): Int? {
//
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserData> {
//        val position = params.key?: 1
//        val response = userApi.getUserData(position.toString())
//        return LoadResult.Page(
//            data = response.body()
//
//        )
//
//    }
//}