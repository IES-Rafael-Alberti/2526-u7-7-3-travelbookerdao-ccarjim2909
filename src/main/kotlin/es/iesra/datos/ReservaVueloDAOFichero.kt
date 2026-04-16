package es.iesra.datos

import es.iesra.dominio.ReservaVuelo
import java.io.File

class ReservaVueloDAOFichero(private val ruta: String) : IReservaVueloDAO {

    private val file = File(ruta)

    private fun leer(): List<ReservaVuelo> {
        if (!file.exists()) return emptyList()

        return file.readLines()
            .filter { it.isNotBlank() }
            .mapIndexed { _, linea ->
                ReservaVuelo.creaInstancia(
                    descripcion = linea,
                    origen = "N/A",
                    destino = "N/A",
                    horaVuelo = "00:00"
                )
            }
    }

    private fun escribir(lista: List<ReservaVuelo>) {
        file.writeText(lista.joinToString("\n") { it.toString() })
    }

    override fun crear(reserva: ReservaVuelo): Boolean {
        file.appendText(reserva.toString() + "\n")
        return true
    }

    override fun obtenerTodas(): List<ReservaVuelo> = leer()

    override fun obtenerPorId(id: Int): ReservaVuelo? =
        leer().find { it.id == id }

    override fun actualizar(reserva: ReservaVuelo): Boolean {
        val lista = leer().toMutableList()
        val i = lista.indexOfFirst { it.id == reserva.id }
        if (i == -1) return false

        lista[i] = reserva
        escribir(lista)
        return true
    }

    override fun eliminar(id: Int): Boolean {
        val nueva = leer().filter { it.id != id }
        if (nueva.size == leer().size) return false

        escribir(nueva)
        return true
    }
}