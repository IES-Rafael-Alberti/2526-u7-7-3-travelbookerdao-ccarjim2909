package es.iesra.datos

import es.iesra.dominio.*


/**
 * Implementación en memoria del repositorio de reservas.
 */
class ReservaRepository(
    private val vueloDAO: IReservaVueloDAO,
    private val hotelDAO: IReservaHotelDAO
) : IReservaRepository {

    override fun agregar(reserva: Reserva): Boolean {
        return when (reserva) {
            is ReservaVuelo -> vueloDAO.crear(reserva)
            is ReservaHotel -> hotelDAO.crear(reserva)
            else -> false
        }
    }

    override fun obtenerTodas(): List<Reserva> {
        return vueloDAO.obtenerTodas() + hotelDAO.obtenerTodas()
    }

    override fun obtenerPorId(id: Int): Reserva? {
        return vueloDAO.obtenerPorId(id)
            ?: hotelDAO.obtenerPorId(id)
    }

    override fun actualizar(reserva: Reserva): Boolean {
        return when (reserva) {
            is ReservaVuelo -> vueloDAO.actualizar(reserva)
            is ReservaHotel -> hotelDAO.actualizar(reserva)
            else -> false
        }
    }

    override fun eliminar(id: Int): Boolean {
        return vueloDAO.eliminar(id) || hotelDAO.eliminar(id)
    }
}