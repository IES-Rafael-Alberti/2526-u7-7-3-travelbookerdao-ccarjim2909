import es.iesra.datos.ReservaRepository
import es.iesra.presentacion.ConsolaUI
import es.iesra.presentacion.IUserInterface
import es.iesra.servicio.IReservaService
import es.iesra.servicio.ReservaService
import es.iesra.datos.*

/**
 * Función principal que inicia la aplicación.
 * Se realiza la inyección de dependencias de manera manual.
 */
fun main() {
    val vueloDAO = ReservaVueloDAOFichero("vuelos.txt")
    val hotelDAO = ReservaHotelDAOFichero("hoteles.txt")

    // Crear la instancia del repositorio (capa de datos).
    val repositorio = ReservaRepository(vueloDAO, hotelDAO)

    // Inyectar la dependencia en el servicio a través de la interfaz.
    val reservaService: IReservaService = ReservaService(repositorio)

    // Inyectar el servicio en la capa de presentación a través de su interfaz.
    val ui: IUserInterface = ConsolaUI(reservaService)

    // Iniciar la aplicación.
    ui.iniciar()
}
/*
Explicación General
En este ejemplo, se ha implementado un sistema de reservas de vuelos y hoteles utilizando el patrón de arquitectura de capas. Cada capa tiene una responsabilidad específica y se comunica con las demás capas a través de interfaces. Además, se ha aplicado el principio de inversión de dependencias para reducir el acoplamiento entre las capas y facilitar la extensibilidad y mantenibilidad del sistema.

Dominio (dominio): Contiene las clases que representan el modelo de dominio del sistema. Es decir, las entidades y reglas de negocio del sistema. No sabe nada de cómo se almacenan los datos ni cómo se presentan al usuario. Sabe cómo se comportan las entidades y cómo interactúan entre sí. Entidades como Reserva, ReservaVuelo y ReservaHotel.
Se define la clase abstracta Reserva que contiene los atributos comunes: id, fechaCreacion y descripcion, y la propiedad detalle que concatena el id y la descripción.
ReservaVuelo y ReservaHotel heredan de Reserva, agregando atributos propios. Ambos tienen constructores privados y usan un companion object para crear instancias a través del método creaInstancia.
En ReservaVuelo, se utiliza una expresión regular para validar el formato de la hora.

Datos (datos): Contiene la lógica de acceso a los datos. Es decir, la persistencia de las reservas, en este caso, en memoria. Por lo tanto, todo lo que tiene que ver con la persistencia de datos. No sabe nada de reservas, solo de cómo almacenarlas y recuperarlas.
Se define la interfaz IReservaRepository para abstraer el almacenamiento.
ReservaRepository implementa esta interfaz utilizando una lista mutable en memoria.

Servicios (servicios): Contiene la lógica de negocio. Es decir, las operaciones que se realizan sobre las reservas.  Se encarga de gestionar las reservas, aplicando reglas de negocio y delegando el acceso a los datos a la capa de datos. Conoce todo lo que tiene que ver con las reservas, pero no cómo se almacenan ni cómo se presentan, ni se recuperan de la base de datos.
Se crea la interfaz IReservaService que define las operaciones de negocio para gestionar reservas.
ReservaService implementa IReservaService y utiliza la abstracción IReservaRepository, aplicando el principio de inversión de dependencias.

Presentación (presentacion): Contiene la lógica de interacción con el usuario. Es decir, la interfaz de usuario y cómo se presentan los datos al usuario. No sabe nada de cómo se almacenan los datos ni cómo se gestionan, solo de cómo se presentan al usuario y como se interactúa con él.
Se define la interfaz IUserInterface y su implementación ConsolaUI, que se encarga de interactuar con el usuario mediante la consola.
Importante: Se inyecta una instancia de IReservaService en ConsolaUI, aplicando así la inversión de dependencias también en la capa de presentación.

Main (main):
Se realiza la inyección de dependencias de forma manual, creando las instancias de repositorio, servicio y la interfaz de usuario, y finalmente se inicia la aplicación.

 */