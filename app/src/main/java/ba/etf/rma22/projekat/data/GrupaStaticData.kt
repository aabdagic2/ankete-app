package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.Grupa

suspend fun allgrupe(): List<Grupa> {
    return getgrupe()
}
