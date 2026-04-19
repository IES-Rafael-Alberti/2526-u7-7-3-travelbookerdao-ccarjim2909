[CE 7.c] ¿Que librería/clases has utilizado para realizar la práctica.? Comenta los métodos mas interesantes

Para la realización de la práctica se ha utilizado principalmente la librería estándar de Kotlin y Java. Específicamente:

- **java.io.File**: Clase utilizada para manejar archivos en el sistema de archivos. Métodos interesantes:
  - `appendText(text: String)`: Permite añadir texto al final del archivo de manera eficiente, ideal para operaciones de creación de reservas sin sobrescribir el contenido existente.
  - `readLines()`: Lee todas las líneas del archivo y las devuelve como una lista de strings, facilitando el procesamiento línea por línea.
  - `writeText(text: String)`: Sobrescribe completamente el contenido del archivo con el texto proporcionado, útil para operaciones de actualización y eliminación donde se reescribe el archivo entero.
  - `exists()`: Verifica si el archivo existe en el sistema de archivos, permitiendo manejar casos donde el archivo aún no ha sido creado.


```kotlin
import java.io.File

class ReservaHotelDAOFichero(private val ruta: String) : IReservaHotelDAO {
    private val file = File(ruta)

    override fun crear(reserva: ReservaHotel): Boolean {
        file.appendText("${reserva.descripcion}|${reserva.ubicacion}|${reserva.numeroNoches}\n")
        return true
    }

    override fun obtenerTodas(): List<ReservaHotel> {
        val lista = mutableListOf<ReservaHotel>()
        if (!file.exists()) return lista
        val lineas = file.readLines()
        for (linea in lineas) {
            if (linea.isNotBlank()) {
                val parts = linea.split("|")
                if (parts.size >= 3) {
                    val descripcion = parts[0]
                    val ubicacion = parts[1]
                    val numeroNoches = parts[2].toInt()
                    val reserva = ReservaHotel.creaInstancia(descripcion, ubicacion, numeroNoches)
                    lista.add(reserva)
                }
            }
        }
        return lista
    }

    override fun actualizar(reserva: ReservaHotel): Boolean {
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
            file.appendText("${r.descripcion}|${r.ubicacion}|${r.numeroNoches}\n")
        }

        return true
    }
}
```

[CE 7.d] 2.a ¿Que formato le has dado a la información del fichero para guardar y recuperar la información?

El formato utilizado es un CSV simple separado por el carácter "|". Cada línea del archivo representa una reserva, con los campos separados por "|". Por ejemplo:
- Para reservas de hotel: `descripcion|ubicacion|numeroNoches`
- Para reservas de vuelo: `descripcion|origen|destino|horaVuelo`

Este formato permite una serialización y deserialización sencilla, ya que los campos se pueden dividir fácilmente usando `split("|")` y reconstruir concatenándolos.

```kotlin
// Ejemplo de escritura en ReservaHotelDAOFichero.kt
file.appendText("${reserva.descripcion}|${reserva.ubicacion}|${reserva.numeroNoches}\n")

// Ejemplo de lectura en ReservaHotelDAOFichero.kt
val parts = linea.split("|")
val descripcion = parts[0]
val ubicacion = parts[1]
val numeroNoches = parts[2].toInt()
```

2.b ¿Que estrategia has usado para trabajar con los ficheros? (Carpeta en donde se guardan los ficheros, cuando crear los archivos, ....)

Los archivos se guardan en el directorio raíz del proyecto (junto al ejecutable), con nombres fijos: "vuelos.txt" para reservas de vuelo y "hoteles.txt" para reservas de hotel. La estrategia es:
- Los archivos se crean automáticamente la primera vez que se escribe en ellos si no existen (mediante `appendText` o `writeText`).
- Para operaciones de creación, se añade una nueva línea al final del archivo usando `appendText`.
- Para operaciones de actualización y eliminación, se lee todo el contenido del archivo, se modifica la lista en memoria, y se reescribe el archivo completo usando `writeText("")` seguido de `appendText` para cada reserva restante.
- No se utiliza una carpeta específica; los archivos están en el directorio de trabajo actual.

```kotlin
// En TravelBooker.kt
val vueloDAO = ReservaVueloDAOFichero("vuelos.txt")
val hotelDAO = ReservaHotelDAOFichero("hoteles.txt")

// Creación automática en ReservaHotelDAOFichero.kt
override fun crear(reserva: ReservaHotel): Boolean {
    file.appendText("${reserva.descripcion}|${reserva.ubicacion}|${reserva.numeroNoches}\n")
    return true
}

// Reescritura completa en actualizar
file.writeText("")
for (r in lista) {
    file.appendText("${r.descripcion}|${r.ubicacion}|${r.numeroNoches}\n")
}
```

2.c ¿Cómo se gestionan los errores? Pon ejemplos de código (enlace permanente al código en GitHub).

La gestión de errores es básica y se limita a verificaciones simples sin uso de excepciones try-catch en los DAOs. Por ejemplo:
- En el método `obtenerTodas()` de `ReservaHotelDAOFichero.kt` (líneas 15-32), se verifica si el archivo existe con `if (!file.exists()) return lista`, devolviendo una lista vacía si no existe, evitando errores de lectura.
- En la creación de instancias en las clases de dominio, se usan `require()` para validar datos (ej. en `ReservaHotel.creaInstancia`, línea 33: `require(numeroNoches > 0)`), lanzando excepciones si no se cumplen las condiciones.
- No hay manejo de errores de E/S como IOException; se asume que las operaciones de archivo son exitosas.

Ejemplo de código: En `ReservaHotelDAOFichero.kt`, línea 17: `if (!file.exists()) return lista`

```kotlin
// En ReservaHotelDAOFichero.kt
override fun obtenerTodas(): List<ReservaHotel> {
    val lista = mutableListOf<ReservaHotel>()
    if (!file.exists()) return lista  // Verificación de existencia
    // ...existing code...
}

// En ReservaHotel.kt
fun creaInstancia(descripcion: String, ubicacion: String, numeroNoches: Int): ReservaHotel {
    require(numeroNoches > 0) { "El número de noches debe ser mayor a 0" }  // Validación con require
    // ...existing code...
}
```

[CE 7.e] 3.a Describe la forma de acceso para leer información

La lectura se realiza cargando todo el archivo en memoria. Se utiliza `file.readLines()` para obtener una lista de strings, cada una representando una línea (reserva). Luego, cada línea se procesa dividiendo por "|" y parseando los campos. Por ejemplo, en `ReservaHotelDAOFichero.kt`, líneas 18-31: se itera sobre las líneas, se divide cada una en partes, y se crea una instancia de ReservaHotel.

```kotlin
override fun obtenerTodas(): List<ReservaHotel> {
    val lista = mutableListOf<ReservaHotel>()
    if (!file.exists()) return lista
    val lineas = file.readLines()
    for (linea in lineas) {
        if (linea.isNotBlank()) {
            val parts = linea.split("|")
            if (parts.size >= 3) {
                val descripcion = parts[0]
                val ubicacion = parts[1]
                val numeroNoches = parts[2].toInt()
                val reserva = ReservaHotel.creaInstancia(descripcion, ubicacion, numeroNoches)
                lista.add(reserva)
            }
        }
    }
    return lista
}
```

3.b Describe la forma de acceso para escribir información

Para escribir, se utiliza `appendText()` para añadir al final del archivo en operaciones de creación. Para actualizaciones o eliminaciones, se reescribe el archivo completo: primero `writeText("")` para vaciarlo, luego `appendText()` para cada reserva en la lista modificada. Ejemplo en `ReservaHotelDAOFichero.kt`, líneas 56-60: se vacía el archivo y se reescribe con la lista actualizada.

```kotlin
override fun crear(reserva: ReservaHotel): Boolean {
    file.appendText("${reserva.descripcion}|${reserva.ubicacion}|${reserva.numeroNoches}\n")
    return true
}

override fun actualizar(reserva: ReservaHotel): Boolean {
    val lista = obtenerTodas().toMutableList()
    // ...existing code...
    file.writeText("")
    for (r in lista) {
        file.appendText("${r.descripcion}|${r.ubicacion}|${r.numeroNoches}\n")
    }
    return true
}
```

3.c Describe la forma de acceso para actualizar información. Pon ejemplos de código.

La actualización implica leer todas las reservas, modificar la correspondiente en memoria, y reescribir el archivo completo. En `ReservaHotelDAOFichero.kt`, líneas 44-63: se obtiene la lista, se busca y reemplaza la reserva por ID, se vacía el archivo y se reescribe. Esto asegura consistencia pero no es eficiente para archivos grandes.

```kotlin
override fun actualizar(reserva: ReservaHotel): Boolean {
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
        file.appendText("${r.descripcion}|${r.ubicacion}|${r.numeroNoches}\n")
    }

    return true
}
```
