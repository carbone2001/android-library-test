package com.carbonesoftware.test.pagination

interface Paginator<Page, Item> {
    /**
     * Function para obtener los proximos items
     */
    suspend fun loadNextItems()

    /**
     * Reinicia la pagina al valor inicial
     */
    fun reset()
}