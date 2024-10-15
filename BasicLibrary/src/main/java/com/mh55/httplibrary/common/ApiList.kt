package com.mh55.httplibrary.common

/**
 * @author Morphling
 */
data class ApiList<T>(
    var total: Int,
    var per_page: Int,
    var pageCount: Int,
    var current_page: Int,
    var last_page: Int,
    var data: MutableList<T>
){
    fun isEmpty(): Boolean {
        return data.isEmpty()
    }

    fun isRefresh(): Boolean {
        return current_page == 1
    }

    fun hasMore(): Boolean {
        return total - current_page > 0
    }
}
