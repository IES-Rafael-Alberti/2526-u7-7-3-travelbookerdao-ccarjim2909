package es.iesra.datos

import es.iesra.dominio.ReservaVuelo

interface IReservaVueloDAO {
    fun crear(reserva: ReservaVuelo): Boolean
    fun obtenerTodas(): List<ReservaVuelo>
    fun obtenerPorId(id: Int): ReservaVuelo?
    fun actualizar(reserva: ReservaVuelo): Boolean
    fun eliminar(id: Int): Boolean
}