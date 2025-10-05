// Clase para representar los datos de cada trabajador
data class Trabajador(
    val nombre: String,
    val salarioBruto: Double,
    val inss: Double,
    val irMensual: Double,
    val totalDeduccion: Double,
    val salarioNeto: Double
)

// Función para "limpiar" la consola (simulación): imprime líneas en blanco.
fun limpiarConsola() {
    repeat(50) { println() }
}

// Pausa hasta que el usuario presione Enter (útil para ver resultados antes de volver al menú).
fun pausar() {
    println("\nPresiona Enter para volver al menú...")
    readLine()
}

fun main() {
    val listaTrabajadores = mutableListOf<Trabajador>() // almacenamiento de registros

    while (true) {
        // ---------- MENÚ PRINCIPAL ----------
        println("===========================================")
        println("     CALCULADORA DE SALARIOS - MENU")
        println("===========================================")
        println("[1] Ingresar trabajador y calcular")
        println("[2] Mostrar todos los trabajadores registrados")
        println("[3] Eliminar todos los datos (reiniciar)")
        println("[4] Salir")
        print("Seleccione una opción: ")

        when (readLine()?.trim()) {
            "1" -> {
                // ----- Ingreso de datos -----
                print("\nIngrese el nombre completo del trabajador: ")
                val nombre = readLine()?.trim().orEmpty()
                if (nombre.isEmpty()) {
                    println("El nombre no puede estar vacío.")
                    pausar()
                    continue
                }

                print("Ingrese el salario mensual (C$): ")
                val salarioInput = readLine()?.trim()
                val salario = salarioInput?.toDoubleOrNull()
                if (salario == null || salario <= 0) {
                    println("Debe ingresar un salario válido y mayor que 0.")
                    pausar()
                    continue
                }

                // ----- Cálculos -----
                val inss = salario * 0.07 // INSS 7%
                val salarioRestante = salario - inss
                val salarioAnual = salarioRestante * 12

                // IR anual según tramos progresivos (DGI Nicaragua)
                val irAnual = when {
                    salarioAnual <= 100_000 -> 0.0
                    salarioAnual <= 200_000 -> (salarioAnual - 100_000) * 0.15
                    salarioAnual <= 350_000 -> (salarioAnual - 200_000) * 0.20 + 15_000
                    salarioAnual <= 500_000 -> (salarioAnual - 350_000) * 0.25 + 45_000
                    else -> (salarioAnual - 500_000) * 0.30 + 82_500
                }

                val irMensual = irAnual / 12.0
                val totalDeduccion = inss + irMensual
                val salarioNeto = salario - totalDeduccion

                // Guardar registro
                listaTrabajadores.add(
                    Trabajador(
                        nombre = nombre,
                        salarioBruto = salario,
                        inss = inss,
                        irMensual = irMensual,
                        totalDeduccion = totalDeduccion,
                        salarioNeto = salarioNeto
                    )
                )

                // Mostrar resultados del registro actual
                println("\n---------- RESULTADO ----------")
                println("Empleado: $nombre")
                println("SaSS (7%): C$ ${"%.2f".format(inss)}")
                println("IR Mensual: C$ ${"%.2f".format(irMensual)}")
                println("Total Deducción: C$ ${"%.2f".format(totalDeduccion)}")
                println("Salario Neto: C$ ${"%.2f".format(salarioNeto)}")
                println("-------------------------------")
                pausar()
            }

            "2" -> {
                // Mostrar todos los registros en formato tabla
                if (listaTrabajadores.isEmpty()) {
                    println("\nNo hay trabajadores registrados todavía.")
                } else {
                    println("\n===== LISTA DE TRABAJADORES REGISTRADOS =====")
                    // Cabecera de la "tabla" con ancho fijo
                    println(String.format("%-3s %-25s %12s %10s %10s %12s",
                        "N", "Nombre", "Bruto", "INSS", "IR", "Neto"))
                    println("-".repeat(78))
                    for ((index, t) in listaTrabajadores.withIndex()) {
                        println(String.format("%-3d %-25s %12.2f %10.2f %10.2f %12.2f",
                            index + 1, t.nombre, t.salarioBruto, t.inss, t.irMensual, t.salarioNeto))
                    }
                    println("==============================================")
                }
                pausar()
            }

            "3" -> {
                // Eliminar todos los registros
                listaTrabajadores.clear()
                limpiarConsola()
                println("Todos los datos han sido eliminados.\n")
                pausar()
            }

            "4" -> {
                println("\nSaliendo del programa...")
                return
            }

            else -> {
                println("\nOpción no válida. Intente nuevamente.")
                pausar()
            }
        }
        // Al final de cada opción regresamos al menú (bucle principal)
        /* Debido a limitaciones de mi laptop, lo que es realizar la interfaz con Android Studio no es posible
          sufre de crasheos constantes y mucho lag, de tal manera que solo puedo entregar lo que es el código en sí*/
    }
}
