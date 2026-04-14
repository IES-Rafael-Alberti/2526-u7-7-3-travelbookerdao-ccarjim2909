package es.iesra.datos

import es.iesra.dominio.Reserva

/**
 * Implementación en memoria del repositorio de reservas.
 */
class ReservaRepository(private val dao: IReservaDAO) : IReservaRepository {

    override fun agregar(reserva: Reserva): Boolean {
        return dao.guardar(reserva)
    }

    override fun obtenerTodas(): List<Reserva> {
        return dao.obtenerTodas()
    }
}