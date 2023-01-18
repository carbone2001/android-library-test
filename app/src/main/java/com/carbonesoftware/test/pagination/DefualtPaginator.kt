package com.carbonesoftware.test.pagination

class DefualtPaginator<Page, Item>(
    //Pagina inicial. Normalmente es 0
    private val initialPage: Page,

    //Avisa cuando la request empieza y cuando termina
    private inline val onLoadUpdated: (Boolean) -> Unit,

    //Callback que se usara para obtener los items
    private inline val onRequest: suspend (nextPage: Page) -> Result<List<Item>>,

    //Callback para obtener la proxima pagina
    private inline val getNextPage: suspend (List<Item>) -> Page,

    //Callback en caso de error
    private inline val onError: suspend (Throwable?) -> Unit,

    //Callback en caso exitoso.
    private inline val onSuccess: suspend (items: List<Item>, nextPage: Page) -> Unit


): Paginator<Page, Item> {

    private var currentPage: Page = initialPage
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        //Evitar hacer otra request si hay una en curso
        if(isMakingRequest){
            return
        }

        isMakingRequest = true
        onLoadUpdated(true)

        //Obtener items
        val result = onRequest(currentPage)
        isMakingRequest = false


        val items = result.getOrElse {
            //Ocurrió un error al obtener resultados
            onError(it)
            onLoadUpdated(false)
            return
        }

        //Cargar la siguiente pagina
        currentPage = getNextPage(items)

        //Resultado exitoso
        onSuccess(items, currentPage)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentPage = initialPage
    }

}