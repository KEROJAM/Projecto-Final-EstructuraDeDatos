public class ValidadorTarjeta {

    /**
     * Valida un número de tarjeta de crédito usando el algoritmo de Luhn
     * @param numeroTarjeta número de tarjeta de 16 dígitos
     * @return true si la tarjeta es válida, false en caso contrario
     */
    public static boolean validarTarjeta(String numeroTarjeta) {
        // Eliminar espacios y guiones
        String numeroLimpio = numeroTarjeta.replaceAll("[\\s-]+", "");

        // Verificar que tenga exactamente 16 dígitos y sean todos números
        if (!numeroLimpio.matches("\\d{16}")) {
            return false;
        }

        // Aplicar algoritmo de Luhn
        int suma = 0;
        boolean doble = false;

        for (int i = numeroLimpio.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numeroLimpio.charAt(i));

            if (doble) {
                digito *= 2;
                if (digito > 9) {
                    digito = digito - 9;
                }
            }

            suma += digito;
            doble = !doble;
        }

        return (suma % 10 == 0);
    }

    /**
     * Determina el tipo de tarjeta basado en el primer dígito
     * @param numeroTarjeta número de tarjeta
     * @return tipo de tarjeta (Visa, MasterCard, etc.)
     */
    public static String obtenerTipoTarjeta(String numeroTarjeta) {
        String numeroLimpio = numeroTarjeta.replaceAll("[\\s-]+", "");

        if (numeroLimpio.startsWith("4")) {
            return "Visa";
        } else if (numeroLimpio.startsWith("5")) {
            return "MasterCard";
        } else if (numeroLimpio.startsWith("34") || numeroLimpio.startsWith("37")) {
            return "American Express";
        } else if (numeroLimpio.startsWith("6")) {
            return "Discover";
        } else {
            return "Desconocida";
        }
    }

    /**
     * Enmascara el número de tarjeta para mostrarlo (ej: **** **** **** 1234)
     * @param numeroTarjeta número de tarjeta completo
     * @return número enmascarado para visualización
     */
    public static String enmascararTarjeta(String numeroTarjeta) {
        String numeroLimpio = numeroTarjeta.replaceAll("[\\s-]+", "");
        if (numeroLimpio.length() != 16) {
            return "Número inválido";
        }
        return "**** **** **** " + numeroLimpio.substring(12);
    }
}