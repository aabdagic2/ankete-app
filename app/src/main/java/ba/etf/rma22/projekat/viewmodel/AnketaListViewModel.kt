package ba.etf.rma22.projekat.viewmodel


import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import kotlinx.coroutines.*

class AnketaListViewModel {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    public suspend fun getAllAnkete(offset:Int=-1):List<Anketa>{
        return AnketaRepository.all(offset)
    }
    public suspend fun getMyAnkete():List<Anketa>{
        return AnketaRepository.getUpisane()

    }
    public suspend fun getDoneAnkete(): List<Anketa>{
        return AnketaRepository.getDone()
    }
    public suspend fun getNotTakenAnkete(): List<Anketa>{
        return AnketaRepository.getNotTaken()
    }
    public suspend fun getFuture():List<Anketa>{
        return AnketaRepository.getFuture()
    }
}