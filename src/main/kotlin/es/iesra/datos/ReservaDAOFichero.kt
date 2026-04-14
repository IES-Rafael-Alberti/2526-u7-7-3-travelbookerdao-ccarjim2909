package es.iesra.datos
import es.iesra.dominio.Reserva
import java.io.File


class ReservaDAOFichero(private val ruta: String) : IReservaDAO {

    private val file = File(ruta)

    override fun guardar(reserva: Reserva): Boolean {
        file.appendText(reserva.toString() + "\n")
        return true
    }

    override fun obtenerTodas(): List<Reserva> {
        return file.readLines().mapIndexed { index, linea ->
            object : Reserva(index, java.time.LocalDateTime.now(), linea) {}
        }
    }
}