package es.iesra.datos

import es.iesra.dominio.Reserva

/**
 * Interfaz que define las operaciones básicas para almacenar y recuperar reservas.
 */
interface IReservaRepository {

    fun agregar(reserva: Reserva): Boolean

    fun obtenerTodas(): List<Reserva>

    fun obtenerPorId(id: Int): Reserva?

    fun actualizar(reserva: Reserva): Boolean

    fun eliminar(id: Int): Boolean
}
