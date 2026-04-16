package es.iesra.datos

import es.iesra.dominio.Reserva


interface IReservaDAO {

    // C
    fun crear(reserva: Reserva): Boolean

    // R
    fun obtenerTodas(): List<Reserva>
    fun obtenerPorId(id: Int): Reserva?

    // U
    fun actualizar(reserva: Reserva): Boolean

    // D
    fun eliminar(id: Int): Boolean
}
