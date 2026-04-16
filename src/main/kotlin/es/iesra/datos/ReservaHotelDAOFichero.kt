package es.iesra.datos

import es.iesra.dominio.ReservaHotel
import java.io.File

class ReservaHotelDAOFichero(private val ruta: String) : IReservaHotelDAO {

    private val file = File(ruta)

    override fun crear(reserva: ReservaHotel): Boolean {
        file.appendText(reserva.toString() + "\n")
        return true
    }

    override fun obtenerTodas(): List<ReservaHotel> {
        val lista = mutableListOf<ReservaHotel>()
        if (!file.exists()) return lista
        val lineas = file.readLines()
        for (linea in lineas) {
            if (linea.isNotBlank()) {
                val reserva = ReservaHotel.creaInstancia(
                    descripcion = linea,
                    ubicacion = ,      // cambiar esto y el del vuelo
                    numeroNoches =     // cambiar esto y el del vuelo
                )
                lista.add(reserva)
            }
        }
        return lista
    }

    override fun obtenerPorId(id: Int): ReservaHotel? {
        val lista = obtenerTodas()
        for (reserva in lista) {
            if (reserva.id == id) {
                return reserva
            }
        }
        return null
    }

    override fun actualizar(reserva: ReservaHotel): Boolean {
        val lista = obtenerTodas()
        var encontrado = false
        for (i in lista.indices) {
            if (lista[i].id == reserva.id) {
                lista[i] = reserva
                encontrado = true
                break
            }
        }
        if (!encontrado) return false

        file.writeText("")

        for (r in lista) {
            file.appendText(r.toString() + "\n")
        }

        return true
    }

    override fun eliminar(id: Int): Boolean {

        val lista = obtenerTodas()
        val nuevaLista = mutableListOf<ReservaHotel>()
        var eliminado = false

        for (r in lista) {
            if (r.id != id) {
                nuevaLista.add(r)
            } else {
                eliminado = true
            }
        }

        if (!eliminado) return false

        file.writeText("")

        for (r in nuevaLista) {
            file.appendText(r.toString() + "\n")
        }

        return true
    }
}