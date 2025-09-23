public class BusquedaBancaria {

    // ===== BÚSQUEDA LINEAL =====

    // Buscar cliente por ID usando búsqueda lineal
    public static Cliente busquedaLinealPorID(Cliente[] clientes, int id) {
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] != null && clientes[i].ID == id) {
                return clientes[i];
            }
        }
        return null;
    }

    // Buscar cliente por nombre usando búsqueda lineal
    public static Cliente busquedaLinealPorNombre(Cliente[] clientes, String nombre) {
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] != null && clientes[i].Nombre.equalsIgnoreCase(nombre)) {
                return clientes[i];
            }
        }
        return null;
    }



    // ===== BÚSQUEDA BINARIA =====

    // Buscar cliente por ID usando búsqueda binaria (requiere array ordenado por ID)
    public static Cliente busquedaBinariaPorID(Cliente[] clientes, int id) {
        // Primero filtrar clientes válidos
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        // Ordenar por ID antes de la búsqueda binaria
        OrdenamientoBancario.quickSortPorID(clientesValidos, 0, clientesValidos.length - 1);

        return busquedaBinariaRecursiva(clientesValidos, id, 0, clientesValidos.length - 1);
    }

    private static Cliente busquedaBinariaRecursiva(Cliente[] clientes, int id, int inicio, int fin) {
        // Caso base: elemento no encontrado
        if (inicio > fin) {
            return null;
        }

        int medio = inicio + (fin - inicio) / 2;

        // Elemento encontrado
        if (clientes[medio].ID == id) {
            return clientes[medio];
        }

        // Buscar en la mitad izquierda
        if (id < clientes[medio].ID) {
            return busquedaBinariaRecursiva(clientes, id, inicio, medio - 1);
        }

        // Buscar en la mitad derecha
        return busquedaBinariaRecursiva(clientes, id, medio + 1, fin);
    }

    // ===== BÚSQUEDAS ESPECIALIZADAS PARA EL SISTEMA BANCARIO =====

    // Buscar clientes por número de tarjeta (usando HashTable existente)
    public static Cliente buscarPorTarjeta(HashTable tabla, String numeroTarjeta) {
        return tabla.get(numeroTarjeta);
    }

    // Buscar clientes con saldo mayor a un monto específico
    public static Cliente[] buscarClientesSaldoMayor(Cliente[] clientes, int montoMinimo) {
        int count = 0;
        for (Cliente cliente : clientes) {
            if (cliente != null && cliente.Monto > montoMinimo) {
                count++;
            }
        }

        Cliente[] resultado = new Cliente[count];
        int index = 0;
        for (Cliente cliente : clientes) {
            if (cliente != null && cliente.Monto > montoMinimo) {
                resultado[index++] = cliente;
            }
        }

        return resultado;
    }

    // Buscar el cliente más cercano a un saldo específico
    public static Cliente buscarClienteSaldoMasCercano(Cliente[] clientes, int saldoObjetivo) {
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        if (clientesValidos.length == 0) {
            return null;
        }

        Cliente clienteMasCercano = clientesValidos[0];
        float diferenciaMenor = Math.abs(clientesValidos[0].Monto - saldoObjetivo);

        for (int i = 1; i < clientesValidos.length; i++) {
            float diferencia = Math.abs(clientesValidos[i].Monto - saldoObjetivo);
            if (diferencia < diferenciaMenor) {
                diferenciaMenor = diferencia;
                clienteMasCercano = clientesValidos[i];
            }
        }

        return clienteMasCercano;
    }

    // ===== MÉTODOS PÚBLICOS PARA USAR EN EL SISTEMA BANCARIO =====

    // Demostrar búsqueda lineal vs binaria
    public static void compararMetodosBusqueda(Cliente[] clientes, int idBuscado) {
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        if (clientesValidos.length == 0) {
            System.out.println("   ║ No hay clientes para buscar");
            return;
        }

        System.out.println("   ║ COMPARACIÓN DE MÉTODOS DE BÚSQUEDA");
        System.out.println("   ║ ═══════════════════════════════════");
        System.out.println("   ║ Buscando cliente con ID: " + idBuscado);
        System.out.println("   ║ Número de elementos: " + clientesValidos.length);

        // Búsqueda lineal
        long inicioLineal = System.nanoTime();
        Cliente resultadoLineal = busquedaLinealPorID(clientesValidos, idBuscado);
        long finLineal = System.nanoTime();

        // Búsqueda binaria
        long inicioBinaria = System.nanoTime();
        Cliente resultadoBinaria = busquedaBinariaPorID(clientesValidos, idBuscado);
        long finBinaria = System.nanoTime();

        System.out.println("   ║ Búsqueda Lineal: " + (finLineal - inicioLineal) + " nanosegundos");
        System.out.println("   ║ Búsqueda Binaria: " + (finBinaria - inicioBinaria) + " nanosegundos");

        if (resultadoLineal != null) {
            System.out.println("   ║ Cliente encontrado: " + resultadoLineal.Nombre);
        } else {
            System.out.println("   ║ Cliente no encontrado");
        }

        if (finLineal - inicioLineal < finBinaria - inicioBinaria) {
            System.out.println("   ║ Búsqueda Lineal fue más rápida");
        } else {
            System.out.println("   ║ Búsqueda Binaria fue más rápida");
        }
    }

    // Buscar y mostrar clientes por criterios múltiples
    public static void busquedaAvanzada(Cliente[] clientes, String criterio, String valor) {
        System.out.println("   ║ BÚSQUEDA AVANZADA DE CLIENTES");
        System.out.println("   ║ ═══════════════════════════════");
        System.out.println("   ║ Criterio: " + criterio + " | Valor: " + valor);
        System.out.println("   ║");

        switch (criterio.toLowerCase()) {
            case "id":
                try {
                    int id = Integer.parseInt(valor);
                    Cliente cliente = busquedaLinealPorID(clientes, id);
                    if (cliente != null) {
                        System.out.println("   ║ Cliente encontrado:");
                        System.out.println("   ║ ID: " + cliente.ID + " | Nombre: " + cliente.Nombre +
                                " | Saldo: $" + cliente.Monto);
                    } else {
                        System.out.println("   ║ Cliente no encontrado");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("   ║ Error: ID debe ser un número");
                }
                break;

            case "nombre":
                Cliente clienteNombre = busquedaLinealPorNombre(clientes, valor);
                if (clienteNombre != null) {
                    System.out.println("   ║ Cliente encontrado:");
                    System.out.println("   ║ ID: " + clienteNombre.ID + " | Nombre: " + clienteNombre.Nombre +
                            " | Saldo: $" + clienteNombre.Monto);
                } else {
                    System.out.println("   ║ Cliente no encontrado");
                }
                break;

            case "saldo":
                try {
                    int saldo = Integer.parseInt(valor);
                    Cliente clienteSaldo = buscarClienteSaldoMasCercano(clientes, saldo);
                    if (clienteSaldo != null) {
                        System.out.println("   ║ Cliente con saldo más cercano a $" + saldo + ":");
                        System.out.println("   ║ ID: " + clienteSaldo.ID + " | Nombre: " + clienteSaldo.Nombre +
                                " | Saldo: $" + clienteSaldo.Monto);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("   ║ Error: Saldo debe ser un número");
                }
                break;

            default:
                System.out.println("   ║ Criterio no válido. Use: id, nombre, o saldo");
        }
    }

    // Mostrar estadísticas de búsqueda
    public static void mostrarEstadisticasBusqueda(Cliente[] clientes) {
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        if (clientesValidos.length == 0) {
            System.out.println("   ║ No hay clientes para analizar");
            return;
        }

        System.out.println("   ║ ESTADÍSTICAS DE BÚSQUEDA");
        System.out.println("   ║ ═══════════════════════════");
        System.out.println("   ║ Total de clientes: " + clientesValidos.length);

        // Encontrar rangos de ID y saldo
        int minID = clientesValidos[0].ID;
        int maxID = clientesValidos[0].ID;
        float minSaldo = clientesValidos[0].Monto;
        float maxSaldo = clientesValidos[0].Monto;

        for (Cliente cliente : clientesValidos) {
            if (cliente.ID < minID) minID = cliente.ID;
            if (cliente.ID > maxID) maxID = cliente.ID;
            if (cliente.Monto < minSaldo) minSaldo = cliente.Monto;
            if (cliente.Monto > maxSaldo) maxSaldo = cliente.Monto;
        }

        System.out.println("   ║ Rango de IDs: " + minID + " - " + maxID);
        System.out.println("   ║ Rango de saldos: $" + minSaldo + " - $" + maxSaldo);

        // Calcular eficiencia teórica
        int comparacionesLineales = clientesValidos.length;
        int comparacionesBinarias = (int) Math.ceil(Math.log(clientesValidos.length) / Math.log(2));

        System.out.println("   ║ Comparaciones máximas (lineal): " + comparacionesLineales);
        System.out.println("   ║ Comparaciones máximas (binaria): " + comparacionesBinarias);
    }

    // Método auxiliar para filtrar clientes válidos
    private static Cliente[] filtrarClientesValidos(Cliente[] clientes) {
        int count = 0;
        for (Cliente cliente : clientes) {
            if (cliente != null) {
                count++;
            }
        }

        Cliente[] clientesValidos = new Cliente[count];
        int index = 0;
        for (Cliente cliente : clientes) {
            if (cliente != null) {
                clientesValidos[index++] = cliente;
            }
        }

        return clientesValidos;
    }
}
