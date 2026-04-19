package es.iesra.datos

import es.iesra.dominio.ReservaVuelo
import java.io.File

class ReservaVueloDAOFichero(private val ruta: String) : IReservaVueloDAO {

    private val file = File(ruta)

    override fun crear(reserva: ReservaVuelo): Boolean {
        file.appendText("${reserva.descripcion}|${reserva.origen}|${reserva.destino}|${reserva.horaVuelo}\n")
        return true
    }

    override fun obtenerTodas(): List<ReservaVuelo> {
        val lista = mutableListOf<ReservaVuelo>()
        if (!file.exists()) return lista
        val lineas = file.readLines()
        for (linea in lineas) {
            if (linea.isNotBlank()) {
                val parts = linea.split("|")
                if (parts.size >= 4) {
                    val descripcion = parts[0]
                    val origen = parts[1]
                    val destino = parts[2]
                    val horaVuelo = parts[3]
                    val reserva = ReservaVuelo.creaInstancia(descripcion, origen, destino, horaVuelo)
                    lista.add(reserva)
                }
            }
        }
        return lista
    }

    override fun obtenerPorId(id: Int): ReservaVuelo? {
        val lista = obtenerTodas()
        for (reserva in lista) {
            if (reserva.id == id) {
                return reserva
            }
        }
        return null
    }

    override fun actualizar(reserva: ReservaVuelo): Boolean {
        val lista = obtenerTodas().toMutableList()
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
            file.appendText("${r.descripcion}|${r.origen}|${r.destino}|${r.horaVuelo}\n")
        }

        return true
    }

    override fun eliminar(id: Int): Boolean {

        val lista = obtenerTodas()
        val nuevaLista = mutableListOf<ReservaVuelo>()
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
            file.appendText("${r.descripcion}|${r.origen}|${r.destino}|${r.horaVuelo}\n")
        }

        return true
    }
}