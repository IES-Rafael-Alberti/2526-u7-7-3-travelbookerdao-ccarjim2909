package es.iesra.datos

import es.iesra.dominio.Reserva


interface IReservaDAO {
    fun guardar(reserva: Reserva): Boolean
    fun obtenerTodas(): List<Reserva>
}