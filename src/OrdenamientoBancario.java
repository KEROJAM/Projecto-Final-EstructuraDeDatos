public class OrdenamientoBancario {

    // ===== QUICKSORT PARA CLIENTES =====

    // Ordenar clientes por saldo usando QuickSort
    public static void quickSortPorSaldo(Cliente[] clientes, int inicio, int fin) {
        if (inicio < fin) {
            int indicePivote = particionPorSaldo(clientes, inicio, fin);

            // Ordenar recursivamente las dos mitades
            quickSortPorSaldo(clientes, inicio, indicePivote - 1);
            quickSortPorSaldo(clientes, indicePivote + 1, fin);
        }
    }

    private static int particionPorSaldo(Cliente[] clientes, int inicio, int fin) {
        int pivote = clientes[fin].Monto; // Usar el último elemento como pivote
        int i = inicio - 1; // Índice del elemento más pequeño

        for (int j = inicio; j < fin; j++) {
            // Si el elemento actual es menor o igual al pivote
            if (clientes[j].Monto <= pivote) {
                i++;
                intercambiar(clientes, i, j);
            }
        }

        intercambiar(clientes, i + 1, fin);
        return i + 1;
    }

    // Ordenar clientes por ID usando QuickSort
    public static void quickSortPorID(Cliente[] clientes, int inicio, int fin) {
        if (inicio < fin) {
            int indicePivote = particionPorID(clientes, inicio, fin);

            quickSortPorID(clientes, inicio, indicePivote - 1);
            quickSortPorID(clientes, indicePivote + 1, fin);
        }
    }

    private static int particionPorID(Cliente[] clientes, int inicio, int fin) {
        int pivote = clientes[fin].ID;
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (clientes[j].ID <= pivote) {
                i++;
                intercambiar(clientes, i, j);
            }
        }

        intercambiar(clientes, i + 1, fin);
        return i + 1;
    }

    // ===== MERGESORT PARA CLIENTES =====

    // Ordenar clientes por nombre usando MergeSort
    public static void mergeSortPorNombre(Cliente[] clientes, int inicio, int fin) {
        if (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;

            // Dividir
            mergeSortPorNombre(clientes, inicio, medio);
            mergeSortPorNombre(clientes, medio + 1, fin);

            // Conquistar (combinar)
            combinarPorNombre(clientes, inicio, medio, fin);
        }
    }

    private static void combinarPorNombre(Cliente[] clientes, int inicio, int medio, int fin) {
        // Crear arrays temporales
        int n1 = medio - inicio + 1;
        int n2 = fin - medio;

        Cliente[] izquierda = new Cliente[n1];
        Cliente[] derecha = new Cliente[n2];

        // Copiar datos a arrays temporales
        for (int i = 0; i < n1; i++) {
            izquierda[i] = clientes[inicio + i];
        }
        for (int j = 0; j < n2; j++) {
            derecha[j] = clientes[medio + 1 + j];
        }

        // Combinar los arrays temporales
        int i = 0, j = 0, k = inicio;

        while (i < n1 && j < n2) {
            if (izquierda[i].Nombre.compareToIgnoreCase(derecha[j].Nombre) <= 0) {
                clientes[k] = izquierda[i];
                i++;
            } else {
                clientes[k] = derecha[j];
                j++;
            }
            k++;
        }

        // Copiar elementos restantes
        while (i < n1) {
            clientes[k] = izquierda[i];
            i++;
            k++;
        }

        while (j < n2) {
            clientes[k] = derecha[j];
            j++;
            k++;
        }
    }

    // Ordenar clientes por saldo usando MergeSort
    public static void mergeSortPorSaldo(Cliente[] clientes, int inicio, int fin) {
        if (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;

            mergeSortPorSaldo(clientes, inicio, medio);
            mergeSortPorSaldo(clientes, medio + 1, fin);

            combinarPorSaldo(clientes, inicio, medio, fin);
        }
    }

    private static void combinarPorSaldo(Cliente[] clientes, int inicio, int medio, int fin) {
        int n1 = medio - inicio + 1;
        int n2 = fin - medio;

        Cliente[] izquierda = new Cliente[n1];
        Cliente[] derecha = new Cliente[n2];

        for (int i = 0; i < n1; i++) {
            izquierda[i] = clientes[inicio + i];
        }
        for (int j = 0; j < n2; j++) {
            derecha[j] = clientes[medio + 1 + j];
        }

        int i = 0, j = 0, k = inicio;

        while (i < n1 && j < n2) {
            if (izquierda[i].Monto <= derecha[j].Monto) {
                clientes[k] = izquierda[i];
                i++;
            } else {
                clientes[k] = derecha[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            clientes[k] = izquierda[i];
            i++;
            k++;
        }

        while (j < n2) {
            clientes[k] = derecha[j];
            j++;
            k++;
        }
    }

    // ===== ORDENAMIENTO DE TRANSACCIONES =====

    // Ordenar array de strings (transacciones) usando QuickSort
    public static void quickSortTransacciones(String[] transacciones, int inicio, int fin) {
        if (inicio < fin) {
            int indicePivote = particionTransacciones(transacciones, inicio, fin);

            quickSortTransacciones(transacciones, inicio, indicePivote - 1);
            quickSortTransacciones(transacciones, indicePivote + 1, fin);
        }
    }

    private static int particionTransacciones(String[] transacciones, int inicio, int fin) {
        String pivote = transacciones[fin];
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (transacciones[j].compareToIgnoreCase(pivote) <= 0) {
                i++;
                intercambiarStrings(transacciones, i, j);
            }
        }

        intercambiarStrings(transacciones, i + 1, fin);
        return i + 1;
    }

    // ===== MÉTODOS DE UTILIDAD =====

    private static void intercambiar(Cliente[] clientes, int i, int j) {
        Cliente temp = clientes[i];
        clientes[i] = clientes[j];
        clientes[j] = temp;
    }

    private static void intercambiarStrings(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // ===== MÉTODOS PÚBLICOS PARA USAR EN EL SISTEMA BANCARIO =====

    // Mostrar clientes ordenados por saldo (menor a mayor)
    public static void mostrarClientesOrdenadosPorSaldo(Cliente[] clientes) {
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        if (clientesValidos.length == 0) {
            System.out.println("   ║ No hay clientes para ordenar");
            return;
        }

        System.out.println("   ║ ORDENANDO CLIENTES POR SALDO (QuickSort)...");
        quickSortPorSaldo(clientesValidos, 0, clientesValidos.length - 1);

        System.out.println("   ║ CLIENTES ORDENADOS POR SALDO:");
        System.out.println("   ║ ═══════════════════════════════════");

        for (int i = 0; i < clientesValidos.length; i++) {
            System.out.println("   ║ " + (i + 1) + ". " + clientesValidos[i].Nombre +
                    " (ID:" + clientesValidos[i].ID + ") - Saldo: $" + clientesValidos[i].Monto);
        }
    }

    // Mostrar clientes ordenados por nombre (A-Z)
    public static void mostrarClientesOrdenadosPorNombre(Cliente[] clientes) {
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        if (clientesValidos.length == 0) {
            System.out.println("   ║ No hay clientes para ordenar");
            return;
        }

        System.out.println("   ║ ORDENANDO CLIENTES POR NOMBRE (MergeSort)...");
        mergeSortPorNombre(clientesValidos, 0, clientesValidos.length - 1);

        System.out.println("   ║ CLIENTES ORDENADOS POR NOMBRE:");
        System.out.println("   ║ ═══════════════════════════════════");

        for (int i = 0; i < clientesValidos.length; i++) {
            System.out.println("   ║ " + (i + 1) + ". " + clientesValidos[i].Nombre +
                    " (ID:" + clientesValidos[i].ID + ") - Saldo: $" + clientesValidos[i].Monto);
        }
    }

    // Mostrar clientes ordenados por ID
    public static void mostrarClientesOrdenadosPorID(Cliente[] clientes) {
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        if (clientesValidos.length == 0) {
            System.out.println("   ║ No hay clientes para ordenar");
            return;
        }

        System.out.println("   ║ ORDENANDO CLIENTES POR ID (QuickSort)...");
        quickSortPorID(clientesValidos, 0, clientesValidos.length - 1);

        System.out.println("   ║ CLIENTES ORDENADOS POR ID:");
        System.out.println("   ║ ═══════════════════════════════════");

        for (int i = 0; i < clientesValidos.length; i++) {
            System.out.println("   ║ " + (i + 1) + ". ID:" + clientesValidos[i].ID +
                    " - " + clientesValidos[i].Nombre + " - Saldo: $" + clientesValidos[i].Monto);
        }
    }

    // Comparar rendimiento de algoritmos de ordenamiento
    public static void compararAlgoritmos(Cliente[] clientes) {
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);

        if (clientesValidos.length < 2) {
            System.out.println("   ║ Se necesitan al menos 2 clientes para comparar");
            return;
        }

        System.out.println("   ║ COMPARACIÓN DE ALGORITMOS DE ORDENAMIENTO");
        System.out.println("   ║ ═══════════════════════════════════════════");
        System.out.println("   ║ Número de elementos: " + clientesValidos.length);

        // Crear copias para cada algoritmo
        Cliente[] copiaQuick = new Cliente[clientesValidos.length];
        Cliente[] copiaMerge = new Cliente[clientesValidos.length];

        System.arraycopy(clientesValidos, 0, copiaQuick, 0, clientesValidos.length);
        System.arraycopy(clientesValidos, 0, copiaMerge, 0, clientesValidos.length);

        // Medir QuickSort
        long inicioQuick = System.nanoTime();
        quickSortPorSaldo(copiaQuick, 0, copiaQuick.length - 1);
        long finQuick = System.nanoTime();

        // Medir MergeSort
        long inicioMerge = System.nanoTime();
        mergeSortPorSaldo(copiaMerge, 0, copiaMerge.length - 1);
        long finMerge = System.nanoTime();

        System.out.println("   ║ QuickSort: " + (finQuick - inicioQuick) + " nanosegundos");
        System.out.println("   ║ MergeSort: " + (finMerge - inicioMerge) + " nanosegundos");

        if (finQuick - inicioQuick < finMerge - inicioMerge) {
            System.out.println("   ║ QuickSort fue más rápido");
        } else {
            System.out.println("   ║ MergeSort fue más rápido");
        }
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