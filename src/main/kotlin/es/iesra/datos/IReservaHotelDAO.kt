package es.iesra.datos

import es.iesra.dominio.ReservaHotel


interface IReservaHotelDAO {
    fun crear(reserva: ReservaHotel): Boolean
    fun obtenerTodas(): List<ReservaHotel>
    fun obtenerPorId(id: Int): ReservaHotel?
    fun actualizar(reserva: ReservaHotel): Boolean
    fun eliminar(id: Int): Boolean
}