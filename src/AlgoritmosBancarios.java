public class AlgoritmosBancarios {
    
    // ===== ALGORITMOS RECURSIVOS =====
    
    // Calcular interés compuesto recursivamente
    public static double calcularInteresCompuesto(double capital, double tasa, int tiempo) {
        // Caso base
        if (tiempo == 0) {
            return capital;
        }
        
        // Llamada recursiva
        return calcularInteresCompuesto(capital * (1 + tasa), tasa, tiempo - 1);
    }
    
    // Calcular suma total de saldos recursivamente
    public static int sumarSaldosRecursivo(Cliente[] clientes, int indice) {
        // Caso base
        if (indice >= clientes.length || clientes[indice] == null) {
            return 0;
        }
        
        // Llamada recursiva
        return clientes[indice].Monto + sumarSaldosRecursivo(clientes, indice + 1);
    }
    
    // Contar clientes con saldo mayor a X recursivamente
    public static int contarClientesSaldoMayor(Cliente[] clientes, int indice, int saldoMinimo) {
        // Caso base
        if (indice >= clientes.length || clientes[indice] == null) {
            return 0;
        }
        
        // Contar si cumple la condición + llamada recursiva
        int cuenta = (clientes[indice].Monto > saldoMinimo) ? 1 : 0;
        return cuenta + contarClientesSaldoMayor(clientes, indice + 1, saldoMinimo);
    }
    
    // Calcular factorial para cálculos de combinaciones (préstamos)
    public static long factorial(int n) {
        // Caso base
        if (n <= 1) {
            return 1;
        }
        
        // Llamada recursiva
        return n * factorial(n - 1);
    }
    
    // Calcular Fibonacci para proyecciones de crecimiento
    public static long fibonacci(int n) {
        // Casos base
        if (n <= 1) {
            return n;
        }
        
        // Llamada recursiva
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    // ===== ALGORITMOS DIVIDE Y VENCERÁS =====
    
    // Procesar transacciones en lotes usando divide y vencerás
    public static void procesarTransaccionesEnLotes(String[] transacciones, int inicio, int fin) {
        // Caso base: si hay una sola transacción o menos
        if (inicio >= fin) {
            if (inicio == fin) {
                System.out.println("   ║ Procesando: " + transacciones[inicio]);
            }
            return;
        }
        
        // Dividir
        int medio = inicio + (fin - inicio) / 2;
        
        System.out.println("   ║ Dividiendo lote: [" + inicio + "-" + medio + "] y [" + (medio + 1) + "-" + fin + "]");
        
        // Conquistar (procesar recursivamente)
        procesarTransaccionesEnLotes(transacciones, inicio, medio);
        procesarTransaccionesEnLotes(transacciones, medio + 1, fin);
        
        // Combinar (en este caso, solo mostrar que se completó el lote)
        System.out.println("   ║ Lote completado: [" + inicio + "-" + fin + "]");
    }
    
    // Encontrar cliente con mayor saldo usando divide y vencerás
    public static Cliente encontrarClienteMayorSaldo(Cliente[] clientes, int inicio, int fin) {
        // Caso base: un solo cliente
        if (inicio == fin) {
            return clientes[inicio];
        }
        
        // Caso base: dos clientes
        if (fin - inicio == 1) {
            return (clientes[inicio].Monto > clientes[fin].Monto) ? clientes[inicio] : clientes[fin];
        }
        
        // Dividir
        int medio = inicio + (fin - inicio) / 2;
        
        // Conquistar
        Cliente maxIzquierda = encontrarClienteMayorSaldo(clientes, inicio, medio);
        Cliente maxDerecha = encontrarClienteMayorSaldo(clientes, medio + 1, fin);
        
        // Combinar
        return (maxIzquierda.Monto > maxDerecha.Monto) ? maxIzquierda : maxDerecha;
    }
    
    // Calcular suma de saldos usando divide y vencerás
    public static int sumarSaldosDivideVenceras(Cliente[] clientes, int inicio, int fin) {
        // Caso base: un solo cliente
        if (inicio == fin) {
            return clientes[inicio].Monto;
        }
        
        // Caso base: array vacío
        if (inicio > fin) {
            return 0;
        }
        
        // Dividir
        int medio = inicio + (fin - inicio) / 2;
        
        // Conquistar y combinar
        int sumaIzquierda = sumarSaldosDivideVenceras(clientes, inicio, medio);
        int sumaDerecha = sumarSaldosDivideVenceras(clientes, medio + 1, fin);
        
        return sumaIzquierda + sumaDerecha;
    }
    
    // ===== MÉTODOS DE UTILIDAD PARA ESTADÍSTICAS BANCARIAS =====
    
    // Mostrar estadísticas completas usando recursividad
    public static void mostrarEstadisticasCompletas(Cliente[] clientes) {
        if (clientes == null || clientes.length == 0) {
            System.out.println("   ║ No hay clientes para analizar");
            return;
        }
        
        System.out.println("   ║ ESTADÍSTICAS BANCARIAS COMPLETAS");
        System.out.println("   ║ ═══════════════════════════════════");
        
        // Filtrar clientes no nulos
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);
        
        if (clientesValidos.length == 0) {
            System.out.println("   ║ No hay clientes válidos");
            return;
        }
        
        // Usar recursividad para calcular estadísticas
        int sumaTotal = sumarSaldosRecursivo(clientesValidos, 0);
        int clientesRicos = contarClientesSaldoMayor(clientesValidos, 0, 10000);
        Cliente clienteMax = encontrarClienteMayorSaldo(clientesValidos, 0, clientesValidos.length - 1);
        
        System.out.println("   ║ Total de clientes: " + clientesValidos.length);
        System.out.println("   ║ Suma total de saldos: $" + sumaTotal);
        System.out.println("   ║ Promedio de saldos: $" + (sumaTotal / clientesValidos.length));
        System.out.println("   ║ Clientes con saldo > $10,000: " + clientesRicos);
        System.out.println("   ║ Cliente con mayor saldo: " + clienteMax.Nombre + " ($" + clienteMax.Monto + ")");
    }
    
    // Calcular proyección de crecimiento usando Fibonacci
    public static void calcularProyeccionCrecimiento(int meses) {
        System.out.println("   ║ PROYECCIÓN DE CRECIMIENTO (Fibonacci)");
        System.out.println("   ║ ═══════════════════════════════════════");
        
        for (int i = 1; i <= meses && i <= 12; i++) {
            long factor = fibonacci(i);
            System.out.println("   ║ Mes " + i + ": Factor de crecimiento " + factor);
        }
    }
    
    // Calcular interés compuesto para múltiples clientes
    public static void calcularInteresesClientes(Cliente[] clientes, double tasaAnual, int años) {
        System.out.println("   ║ CÁLCULO DE INTERESES COMPUESTOS");
        System.out.println("   ║ Tasa anual: " + (tasaAnual * 100) + "% - Años: " + años);
        System.out.println("   ║ ═══════════════════════════════════════");
        
        Cliente[] clientesValidos = filtrarClientesValidos(clientes);
        
        for (Cliente cliente : clientesValidos) {
            double montoFinal = calcularInteresCompuesto(cliente.Monto, tasaAnual, años);
            double ganancia = montoFinal - cliente.Monto;
            
            System.out.println("   ║ " + cliente.Nombre + ":");
            System.out.println("   ║   Capital inicial: $" + cliente.Monto);
            System.out.println("   ║   Capital final: $" + String.format("%.2f", montoFinal));
            System.out.println("   ║   Ganancia: $" + String.format("%.2f", ganancia));
            System.out.println("   ║");
        }
    }
    
    // Procesar transacciones masivas usando divide y vencerás
    public static void procesarTransaccionesMasivas(String[] tiposTransacciones, int[] montos, String[] clientes) {
        if (tiposTransacciones.length != montos.length || montos.length != clientes.length) {
            System.out.println("   ║ Error: Arrays de diferentes tamaños");
            return;
        }
        
        System.out.println("   ║ PROCESAMIENTO MASIVO DE TRANSACCIONES");
        System.out.println("   ║ ═══════════════════════════════════════");
        
        // Crear array de transacciones formateadas
        String[] transacciones = new String[tiposTransacciones.length];
        for (int i = 0; i < transacciones.length; i++) {
            transacciones[i] = tiposTransacciones[i] + ": $" + montos[i] + " - " + clientes[i];
        }
        
        // Procesar usando divide y vencerás
        procesarTransaccionesEnLotes(transacciones, 0, transacciones.length - 1);
    }
    
    // Método auxiliar para filtrar clientes válidos
    private static Cliente[] filtrarClientesValidos(Cliente[] clientes) {
        // Contar clientes válidos
        int count = 0;
        for (Cliente cliente : clientes) {
            if (cliente != null) {
                count++;
            }
        }
        
        // Crear array de clientes válidos
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
