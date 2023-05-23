package ba.etf.rma22.projekat.data.repositories

object ApiConfig {
   public var baseURL="https://rma22ws.herokuapp.com"
    fun postaviBaseURL(baseUrl:String):Unit{
        baseURL=baseUrl;
    }
}