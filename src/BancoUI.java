import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BancoUI extends JFrame {

    // --- SECCIÓN 1: LÓGICA DEL SISTEMA (BACKEND) ---
    private HashTable clientesTable;
    private Cliente clienteSesion;
    private Queue<String> colaTransferencias;
    private static final String RUTA_CSV = "src/clientes.csv";


    // =========================================================================
    // SECCIÓN DE DISEÑO (UI THEME)
    // =========================================================================
    private static final Color COLOR_PRIMARY = new Color(0, 90, 156);
    private static final Color COLOR_BACKGROUND = new Color(245, 247, 250);
    private static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private static final Color COLOR_TEXT_LIGHT = Color.WHITE;
    private static final Color COLOR_ACCENT = new Color(0, 123, 255);
    private static final Color COLOR_LOGOUT = new Color(220, 53, 69);

    private static final Font FONT_TITLE = new Font("Noto Sans", Font.BOLD, 32);
    private static final Font FONT_HEADER = new Font("Noto Sans", Font.BOLD, 18);
    private static final Font FONT_BODY = new Font("Noto Sans", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Noto Sans", Font.BOLD, 14);
    // =========================================================================
    // COMPONENTES DE LA INTERFAZ (FRONTEND)
    // =========================================================================
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel initialPanel;
    private JPanel loginPanel;
    private JPanel mainMenuPanel;

    private JTextField loginTarjetaField;
    private JPasswordField loginPasswordField;


    private JLabel clienteLabel;
    private JLabel saldoLabel;
    private JLabel ahorrosLabel;

    // =========================================================================
    // CLASES AUXILIARES PARA EL HISTORIAL ORDENADO POR MONTO (ÁRBOL BINARIO)
    // =========================================================================

    /**
     * Nodo para el árbol binario de historial. Almacena el monto para la
     * comparación y la descripción completa de la transacción.
     */
    private static class TransactionNode {
        int amount;
        String description;
        TransactionNode left, right;

        TransactionNode(int amount, String description) {
            this.amount = amount;
            this.description = description;
        }
    }

    /**
     * Árbol Binario de Búsqueda para ordenar las transacciones por monto.
     * Los montos negativos (retiros) se colocarán a la izquierda y los
     * positivos (depósitos) a la derecha.
     */
    private static class TransactionHistoryTree {
        private TransactionNode root;

        public void insert(int amount, String description) {
            root = insertRec(root, amount, description);
        }

        private TransactionNode insertRec(TransactionNode current, int amount, String description) {
            if (current == null) {
                return new TransactionNode(amount, description);
            }
            if (amount < current.amount) {
                current.left = insertRec(current.left, amount, description);
            } else { // Si los montos son iguales, se inserta a la derecha.
                current.right = insertRec(current.right, amount, description);
            }
            return current;
        }

        public String getInOrderTraversal() {
            StringBuilder sb = new StringBuilder("Movimientos ordenados de menor a mayor monto:\n---------------------------------------------\n");
            if (root == null) {
                sb.append("No hay movimientos para ordenar.");
            } else {
                inOrderRec(root, sb);
            }
            return sb.toString();
        }

        private void inOrderRec(TransactionNode node, StringBuilder sb) {
            if (node != null) {
                inOrderRec(node.left, sb);
                sb.append(node.description).append("\n");
                inOrderRec(node.right, sb);
            }
        }
    }


    public BancoUI() {
        inicializarSistemaBancario();
        applyGlobalUIStyles();
        setTitle("Banco Financiero GUI");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        crearInitialPanel();
        crearLoginPanel();
        crearMainMenuPanel();
        mainPanel.add(initialPanel, "Initial");
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(mainMenuPanel, "MainMenu");
        add(mainPanel);
        cardLayout.show(mainPanel, "Initial");
    }

    private void inicializarSistemaBancario() {
        clientesTable = new HashTable(100);
        colaTransferencias = new Queue<>();

        File csvFile = new File(RUTA_CSV);
        if (csvFile.exists()) {
            int loaded = CSVClientLoader.cargarClientes(RUTA_CSV, clientesTable);
            System.out.println("Cargados " + loaded + " clientes desde " + RUTA_CSV);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el archivo 'clientes.csv' en la carpeta 'src'.\n" +
                            "Puede crear un archivo de ejemplo usando 'Opciones Avanzadas'.",
                    "Archivo no encontrado",
                    JOptionPane.WARNING_MESSAGE);
            agregarCliente(1, "Usuario Ejemplo", 1000, "1111222233334444", "pass123", false);
        }
    }

    private void agregarCliente(int id, String nombre, int monto, String numeroTarjeta, String contrasena, boolean guardarEnCSV) {
        Cliente cliente = new Cliente(id, nombre, monto, numeroTarjeta, contrasena);
        clientesTable.put(numeroTarjeta, cliente);
        if (guardarEnCSV) {
            CSVClientLoader.guardarCliente(cliente, RUTA_CSV);
        }
    }

    private void crearInitialPanel() {
        initialPanel = new JPanel(new GridBagLayout());
        initialPanel.setBackground(COLOR_BACKGROUND);
        initialPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Bienvenido al Banco Financiero");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton loginButton = createStyledButton("Iniciar Sesión", COLOR_PRIMARY, COLOR_TEXT_LIGHT);
        JButton registerButton = createStyledButton("Registrarse", COLOR_ACCENT, COLOR_TEXT_LIGHT);
        JButton advancedOptionsButton = createStyledButton("Opciones Avanzadas", new Color(108, 117, 125), COLOR_TEXT_LIGHT);

        gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 30, 0); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridx = 0; gbc.gridy = 0;
        initialPanel.add(titleLabel, gbc);

        gbc.insets = new Insets(20, 5, 10, 5); gbc.gridy = 1;
        initialPanel.add(loginButton, gbc);

        gbc.insets = new Insets(10, 5, 10, 5); gbc.gridy = 2;
        initialPanel.add(registerButton, gbc);

        gbc.insets = new Insets(10, 5, 10, 5); gbc.gridy = 3;
        initialPanel.add(advancedOptionsButton, gbc);

        loginButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));
        registerButton.addActionListener(e -> mostrarDialogoRegistro());
        advancedOptionsButton.addActionListener(e -> gestionarCSV());
    }

    private void crearLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(COLOR_BACKGROUND);
        loginPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Iniciar Sesión");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_TEXT_DARK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loginTarjetaField = createStyledTextField();
        loginPasswordField = createStyledPasswordField();

        JButton loginButton = createStyledButton("Iniciar Sesión", COLOR_PRIMARY, COLOR_TEXT_LIGHT);
        JButton backButton = createStyledButton("Volver", COLOR_LOGOUT, COLOR_TEXT_LIGHT);


        gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 30, 0); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);
        gbc.gridwidth = 1; gbc.insets = new Insets(5, 5, 5, 5);
        addFormField(loginPanel, gbc, "Nº Tarjeta:", 1, loginTarjetaField);
        addFormField(loginPanel, gbc, "Contraseña:", 2, loginPasswordField);
        gbc.gridwidth = 2; gbc.insets = new Insets(20, 5, 10, 5); gbc.gridy = 5;
        loginPanel.add(loginButton, gbc);
        gbc.insets = new Insets(0, 5, 10, 5); gbc.gridy = 6;
        loginPanel.add(backButton, gbc);


        loginButton.addActionListener(e -> intentarLogin());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Initial"));
    }

    private void crearMainMenuPanel() {
        mainMenuPanel = new JPanel(new BorderLayout(20, 20));
        mainMenuPanel.setBackground(COLOR_BACKGROUND);
        mainMenuPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(COLOR_PRIMARY);
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        clienteLabel = new JLabel("Cliente: ");
        clienteLabel.setFont(FONT_HEADER);
        clienteLabel.setForeground(COLOR_TEXT_LIGHT);
        saldoLabel = new JLabel("Saldo Principal: $");
        saldoLabel.setFont(FONT_BODY);
        saldoLabel.setForeground(COLOR_TEXT_LIGHT);
        ahorrosLabel = new JLabel("Saldo Ahorrado: $");
        ahorrosLabel.setFont(FONT_BODY);
        ahorrosLabel.setForeground(COLOR_TEXT_LIGHT);
        infoPanel.add(clienteLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(saldoLabel);
        infoPanel.add(ahorrosLabel);

        JPanel actionsPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        actionsPanel.setBackground(COLOR_BACKGROUND);
        actionsPanel.add(createStyledButton("Realizar Depósito", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> accionDepositar()));
        actionsPanel.add(createStyledButton("Retirar Monto", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> accionRetirar()));
        actionsPanel.add(createStyledButton("Transferir", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> accionTransferir()));
        actionsPanel.add(createStyledButton("Historial", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> accionVerHistorial()));
        actionsPanel.add(createStyledButton("Caja de Inversión", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> mostrarDialogoCajaAhorros()));

        JButton logoutBtn = createStyledButton("Cerrar Sesión", COLOR_LOGOUT, COLOR_TEXT_LIGHT, e -> cerrarSesion());

        mainMenuPanel.add(infoPanel, BorderLayout.NORTH);
        mainMenuPanel.add(actionsPanel, BorderLayout.CENTER);
        mainMenuPanel.add(logoutBtn, BorderLayout.SOUTH);
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss"));
    }

    /**
     * Clona el contenido de la pila de historial a una nueva pila.
     * Este método es no destructivo; la pila original del cliente se restaura a su estado inicial.
     * @return Un nuevo objeto Stack que es una copia exacta del historial del cliente.
     */
    private Stack<String> getHistoryElements() {
        Stack<String> copyStack = new Stack<>();
        Stack<String> originalStack = clienteSesion.getPilaHistorial();
        Stack<String> tempStack = new Stack<>(); // Pila auxiliar para la operación

        try {
            // 1. Mover todos los elementos de la pila original a la pila temporal.
            //    Esto invierte el orden.
            while (!originalStack.isEmpty()) {
                tempStack.push(originalStack.peek());
                originalStack.pop();
            }

            // 2. Mover los elementos de la pila temporal de vuelta a la original Y a la nueva pila de copia.
            //    Esto restaura la pila original y crea la copia en el orden correcto.
            while (!tempStack.isEmpty()) {
                String item = tempStack.peek();
                originalStack.push(item);
                copyStack.push(item);
                tempStack.pop();
            }
        } catch (Exception e) {
            System.err.println("Error al clonar la pila de historial: " + e.getMessage());
        }
        return copyStack;
    }


    /**
     * Muestra un diálogo para que el usuario elija cómo ver el historial.
     */
    private void accionVerHistorial() {
        if (clienteSesion.getPilaHistorial().isEmpty()) {
            showMessage("Historial Vacío", "No hay movimientos en el historial.");
            return;
        }

        Object[] options = {"Orden Cronológico", "Ordenar por Monto"};
        int choice = JOptionPane.showOptionDialog(this,
                "¿Cómo deseas ver el historial de movimientos?",
                "Ver Historial",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            mostrarHistorialCronologico();
        } else if (choice == JOptionPane.NO_OPTION) {
            mostrarHistorialPorMonto();
        }
    }

    /**
     * Muestra el historial en orden cronológico (más reciente primero), consumiendo una copia de la pila.
     */
    private void mostrarHistorialCronologico() {
        StringBuilder historialTexto = new StringBuilder("Últimos movimientos (Más recientes primero):\n---------------------------------------------\n");
        Stack<String> historialCopy = getHistoryElements(); // Obtiene una copia segura para consumir

        int contador = 1;
        try {
            while (!historialCopy.isEmpty()) {
                historialTexto.append(contador).append(". ").append(historialCopy.peek()).append("\n");
                historialCopy.pop();
                contador++;
            }
        } catch (Exception e) {
            historialTexto.append("\nError al procesar el historial.");
            System.err.println("Error en mostrarHistorialCronologico: " + e.getMessage());
        }

        if (contador == 1) { // Si el bucle nunca se ejecutó
            historialTexto.append("No hay historial de transacciones.");
        }

        JTextArea textArea = new JTextArea(historialTexto.toString());
        textArea.setFont(FONT_BODY);
        textArea.setEditable(false);
        textArea.setBackground(COLOR_BACKGROUND);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 350));
        JOptionPane.showMessageDialog(this, scrollPane, "Historial de Movimientos (Cronológico)", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Construye un árbol binario con el historial para mostrarlo ordenado por monto.
     */
    private void mostrarHistorialPorMonto() {
        TransactionHistoryTree sortedHistoryTree = new TransactionHistoryTree();
        Stack<String> historialCopy = getHistoryElements(); // Obtiene una copia segura para consumir

        // Expresión regular para encontrar montos con signo, ej: +$100, -$50
        Pattern pattern = Pattern.compile("([+-])\\$(\\d+)");

        try {
            while (!historialCopy.isEmpty()) {
                String item = historialCopy.peek();
                Matcher matcher = pattern.matcher(item);
                if (matcher.find()) {
                    String sign = matcher.group(1);
                    int amount = Integer.parseInt(matcher.group(2));
                    if ("-".equals(sign)) {
                        amount *= -1; // Convertir retiros a números negativos para la ordenación
                    }
                    sortedHistoryTree.insert(amount, item);
                }
                historialCopy.pop();
            }
        } catch (Exception e) {
            showError("Ocurrió un error al construir el árbol de historial.");
            System.err.println("Error en mostrarHistorialPorMonto: " + e.getMessage());
            return;
        }

        String sortedHistorial = sortedHistoryTree.getInOrderTraversal();

        JTextArea textArea = new JTextArea(sortedHistorial);
        textArea.setFont(FONT_BODY);
        textArea.setEditable(false);
        textArea.setBackground(COLOR_BACKGROUND);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 350));
        JOptionPane.showMessageDialog(this, scrollPane, "Historial de Movimientos (Ordenado por Monto)", JOptionPane.PLAIN_MESSAGE);
    }

    private void intentarLogin() {
        String tarjeta = loginTarjetaField.getText();
        String password = new String(loginPasswordField.getPassword());

        if (tarjeta.isEmpty() || password.isEmpty()) {
            showError("El número de tarjeta y la contraseña son obligatorios.");
            return;
        }
        if (!ValidadorTarjeta.validarTarjeta(tarjeta)) {
            showError("Formato de tarjeta no válido.");
            return;
        }
        Cliente clienteEncontrado = clientesTable.get(tarjeta);
        if (clienteEncontrado == null || !clienteEncontrado.validarContraseña(password)) {
            showError("Credenciales incorrectas.");
            return;
        }
        clienteSesion = clienteEncontrado;
        clienteSesion.setUI(this);
        actualizarInfoCliente();
        cardLayout.show(mainPanel, "MainMenu");
        showMessage("Inicio de sesión exitoso", "Bienvenido, " + clienteSesion.Nombre);
    }

    private void mostrarDialogoRegistro() {
        JTextField nombreField = createStyledTextField();
        JTextField apellidoField = createStyledTextField();
        JTextField tarjetaField = createStyledTextField();
        JPasswordField passwordField = createStyledPasswordField();
        JPasswordField confirmPasswordField = createStyledPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(COLOR_BACKGROUND);
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Apellido:"));
        panel.add(apellidoField);
        panel.add(new JLabel("Nº de Tarjeta (16 dígitos):"));
        panel.add(tarjetaField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);
        panel.add(new JLabel("Confirmar Contraseña:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registro de Nuevo Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String tarjeta = tarjetaField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (nombre.isEmpty() || apellido.isEmpty() || tarjeta.isEmpty() || password.isEmpty()) {
                showError("Todos los campos son obligatorios.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showError("Las contraseñas no coinciden.");
                return;
            }
            if (clientesTable.contains(tarjeta)) {
                showError("Esta tarjeta ya está registrada.");
            } else if (ValidadorTarjeta.validarTarjeta(tarjeta)) {
                String nombreCompleto = nombre + " " + apellido;
                int nuevoID = clientesTable.size() + 1;
                agregarCliente(nuevoID, nombreCompleto, 0, tarjeta, password, true);
                showMessage("Registro Exitoso", "Cliente '" + nombreCompleto + "' registrado con éxito.");
            } else {
                showError("Número de tarjeta no válido.");
            }
        }
    }

    private void accionDepositar() {
        if (clienteSesion.isTarjetaBloqueada()) {
            showError("Operación no permitida. Su tarjeta está bloqueada por seguridad.\nContacte al banco para desbloquearla.");
            return;
        }
        
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a depositar:", "Depósito", JOptionPane.PLAIN_MESSAGE);
        try {
            int monto = Integer.parseInt(montoStr);
            if (monto <= 0) { showError("El monto debe ser positivo."); return; }
            // MODIFICADO: Usa el historial del cliente
            clienteSesion.Depositar(monto);
            // MODIFICADO: Añade registro con fecha y hora
            clienteSesion.getPilaHistorial().push("Deposito: +$" + monto + " [" + getTimestamp() + "]");
            actualizarInfoCliente();
            showMessage("Éxito", "Depósito de $" + monto + " realizado con éxito.");
        } catch (Exception e) { showError("Por favor, ingrese un número válido."); }
    }

    private void accionRetirar() {
        if (clienteSesion.isTarjetaBloqueada()) {
            showError("Operación no permitida. Su tarjeta está bloqueada por seguridad.\nContacte al banco para desbloquearla.");
            return;
        }
        
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a retirar:", "Retiro", JOptionPane.PLAIN_MESSAGE);
        try {
            int monto = Integer.parseInt(montoStr);
            if (monto <= 0) { showError("El monto debe ser positivo."); return; }
            if (monto > clienteSesion.Monto) { showError("Fondos insuficientes."); return; }
            clienteSesion.Monto -= monto;
            // MODIFICADO: Añade registro con fecha y hora
            clienteSesion.getPilaHistorial().push("Retiro: -$" + monto + " [" + getTimestamp() + "]");
            actualizarInfoCliente();
            showMessage("Éxito", "Retiro de $" + monto + " realizado con éxito.");
        } catch (Exception e) { showError("Por favor, ingrese un número válido."); }
    }

    private void accionTransferir() {
        if (clienteSesion.isTarjetaBloqueada()) {
            showError("Operación no permitida. Su tarjeta está bloqueada por seguridad.\nContacte al banco para desbloquearla.");
            return;
        }
        
        JTextField tarjetaField = createStyledTextField(), NameField = createStyledTextField(), montoField = createStyledTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5)); panel.setBackground(COLOR_BACKGROUND);
        panel.add(new JLabel("Nº Tarjeta del Destinatario:")); panel.add(tarjetaField);
        panel.add(new JLabel("Nombre del Destinatario:")); panel.add(NameField);
        panel.add(new JLabel("Monto a Transferir:")); panel.add(montoField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Realizar Transferencia", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String tarjetaDest = tarjetaField.getText(); String NameDest = NameField.getText(); int monto = Integer.parseInt(montoField.getText());
                Cliente destinatario = clientesTable.get(tarjetaDest);
                if (destinatario == null) { showError("Nº de Tarjeta no encontrado."); return; }
                if (!Objects.equals(destinatario.Nombre, NameDest)) { showError("El Nombre no corresponde al titular de la tarjeta."); return; }
                if (monto <= 0 || monto > clienteSesion.Monto) { showError("Monto no válido o fondos insuficientes."); return; }

                // Usar el método Transferir() del cliente para detectar actividad inusual
                boolean esTransferenciaInusual = clienteSesion.Transferir(monto);
                destinatario.Depositar(monto);
                colaTransferencias.enqueue(monto);

                // MODIFICADO: Añade registro con fecha y hora a ambos clientes
                String timestamp = getTimestamp();
                clienteSesion.getPilaHistorial().push("Transferencia a " + destinatario.Nombre + ": -$" + monto + " [" + timestamp + "]");
                destinatario.getPilaHistorial().push("Transferencia de " + clienteSesion.Nombre + ": +$" + monto + " [" + timestamp + "]");

                actualizarInfoCliente();
                if (!esTransferenciaInusual) {
                    showMessage("Éxito", "Transferencia realizada con éxito.");
                } else {
                    mostrarDialogoTransferenciaSospechosa();
                }
            } catch (Exception e) { showError("Datos inválidos. Verifique la información."); }
        }
    }

    private void mostrarDialogoTransferenciaSospechosa() {
        JDialog dialog = new JDialog(this, "", true);
        dialog.setSize(450, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(COLOR_BACKGROUND);

        // Panel del mensaje
        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
        messagePanel.setBackground(COLOR_BACKGROUND);
        messagePanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Transferencia Sospechosa");
        titleLabel.setFont(new Font("Noto Sans", Font.BOLD, 16));
        titleLabel.setForeground(new Color(200, 0, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea messageArea = new JTextArea("Se ha detectado una transferencia sospechosa.\n\n" +
                "Esta actividad podría indicar un posible fraude o uso no autorizado de su cuenta.\n" +
                "¿Qué acción desea realizar?");
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBackground(COLOR_BACKGROUND);
        messageArea.setFont(FONT_BODY);

        messagePanel.add(titleLabel, BorderLayout.NORTH);
        messagePanel.add(messageArea, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setBackground(COLOR_BACKGROUND);

        JButton bloquearButton = createStyledButton("Bloquear Tarjeta", COLOR_LOGOUT, COLOR_TEXT_LIGHT);
        JButton ignorarButton = createStyledButton("Ignorar Mensaje", COLOR_ACCENT, COLOR_TEXT_LIGHT);

        bloquearButton.addActionListener(e -> {
            dialog.dispose(); // Cerrar el diálogo inmediatamente
            clienteSesion.bloquearTarjeta();
            showMessage("Tarjeta Bloqueada", 
                "Su tarjeta ha sido bloqueada por seguridad debido a actividad sospechosa.\n" +
                "Contacte al banco para desbloquearla.");
            cerrarSesion();
        });

        ignorarButton.addActionListener(e -> {
            dialog.dispose(); // Cerrar el diálogo inmediatamente
            showMessage("Transferencia Completada", 
                "La transferencia se ha completado exitosamente.\n" +
                "Recomendamos monitorear su cuenta regularmente.");
        });

        buttonsPanel.add(bloquearButton);
        buttonsPanel.add(ignorarButton);

        dialog.add(messagePanel, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void mostrarDialogoCajaAhorros() {
        JDialog dialog = new JDialog(this, "Caja de Inversión / Ahorros", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(COLOR_BACKGROUND);
        JLabel ahorrosActualLabel = new JLabel(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros), SwingConstants.CENTER);
        ahorrosActualLabel.setFont(FONT_HEADER);
        ahorrosActualLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel botonesPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        botonesPanel.setBackground(COLOR_BACKGROUND);
        botonesPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        botonesPanel.add(createStyledButton("Depositar en Ahorros", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> {
            String montoStr = JOptionPane.showInputDialog(dialog, "Monto a depositar (mín. $100):", "Depositar", JOptionPane.PLAIN_MESSAGE);
            try {
                int monto = Integer.parseInt(montoStr);
                if (monto < 100) {
                    showError("El depósito mínimo es de $100.", dialog);
                } else if (monto > clienteSesion.Monto) {
                    showError("Fondos insuficientes en cuenta principal.", dialog);
                } else {
                    clienteSesion.Monto -= monto;
                    clienteSesion.montoAhorros += monto;
                    // MODIFICADO: Añade registro con fecha y hora
                    clienteSesion.getPilaHistorial().push(String.format("Depósito Ahorros: -$%d [%s]", monto, getTimestamp()));
                    actualizarInfoCliente();
                    ahorrosActualLabel.setText(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros));
                    showMessage("Éxito", "Depósito a ahorros exitoso.", dialog);
                }
            } catch (Exception ex) {
                showError("Monto inválido.", dialog);
            }
        }));
        botonesPanel.add(createStyledButton("Retirar de Ahorros", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> {
            String montoStr = JOptionPane.showInputDialog(dialog, "Monto a retirar de ahorros:", "Retirar", JOptionPane.PLAIN_MESSAGE);
            try {
                int monto = Integer.parseInt(montoStr);
                if (monto > 0 && monto <= clienteSesion.montoAhorros) {
                    clienteSesion.montoAhorros -= monto;
                    clienteSesion.Monto += monto;
                    // MODIFICADO: Añade registro con fecha y hora
                    clienteSesion.getPilaHistorial().push(String.format("Retiro Ahorros: +$%d [%s]", monto, getTimestamp()));
                    actualizarInfoCliente();
                    ahorrosActualLabel.setText(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros));
                    showMessage("Éxito", "Retiro de ahorros exitoso.", dialog);
                } else {
                    showError("Monto no válido o fondos insuficientes.", dialog);
                }
            } catch (Exception ex) {
                showError("Monto inválido.", dialog);
            }
        }));
        botonesPanel.add(createStyledButton("Ver Proyección de Crecimiento", COLOR_ACCENT, COLOR_TEXT_LIGHT, e -> {
            if (clienteSesion.montoAhorros > 0) {
                final double TASA_ANUAL = 0.07, TASA_MENSUAL = TASA_ANUAL / 12.0;
                double en1 = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 1);
                double en6 = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 6);
                double en12 = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 12);
                String proy = String.format("Proyección para $%,d (7%% Anual):\n\n- En 1 mes:   $%,d\n- En 6 meses: $%,d\n- En 1 año:   $%,d\n", clienteSesion.montoAhorros, (long) en1, (long) en6, (long) en12);
                showMessage("Proyección de Crecimiento", proy, dialog);
            } else {
                showError("No hay fondos en ahorros para proyectar.", dialog);
            }
        }));
        dialog.add(ahorrosActualLabel, BorderLayout.NORTH);
        dialog.add(botonesPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private static double calcularCrecimientoAhorros(double capital, double tasaMensual, int meses) {
        if (meses == 0) return capital; return calcularCrecimientoAhorros(capital * (1 + tasaMensual), tasaMensual, meses - 1);
    }

    private void cerrarSesion() {
        clienteSesion = null;
        loginTarjetaField.setText("");
        loginPasswordField.setText("");
        cardLayout.show(mainPanel, "Initial");
    }

    private void actualizarInfoCliente() {
        if (clienteSesion != null) {
            clienteLabel.setText("Cliente: " + clienteSesion.Nombre);
            saldoLabel.setText(String.format("Saldo Principal: $%,d", clienteSesion.Monto));
            ahorrosLabel.setText(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros));
        }
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos, Component field) {
        JLabel label = new JLabel(labelText); label.setFont(FONT_BODY); label.setForeground(COLOR_TEXT_DARK);
        gbc.gridx = 0; gbc.gridy = yPos; gbc.anchor = GridBagConstraints.WEST; panel.add(label, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.CENTER; panel.add(field, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15); textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
        textField.setBackground(Color.WHITE); return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passField = new JPasswordField(15); passField.setFont(FONT_BODY);
        passField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
        passField.setBackground(Color.WHITE); return passField;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text); button.setFont(FONT_BUTTON); button.setBackground(bgColor); button.setForeground(fgColor);
        button.setFocusPainted(false); button.setCursor(new Cursor(Cursor.HAND_CURSOR)); button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor, java.awt.event.ActionListener listener) {
        JButton button = createStyledButton(text, bgColor, fgColor); button.addActionListener(listener); return button;
    }

    private void applyGlobalUIStyles() {
        UIManager.put("Panel.background", COLOR_BACKGROUND); UIManager.put("OptionPane.background", COLOR_BACKGROUND);
        UIManager.put("OptionPane.messageFont", FONT_BODY); UIManager.put("Button.background", COLOR_PRIMARY);
        UIManager.put("Button.foreground", COLOR_TEXT_LIGHT); UIManager.put("Button.font", FONT_BUTTON);
    }

    public void mostrarAlertaFraude(String titulo, String mensaje) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel iconLabel = new JLabel(new ImageIcon("warning.png"));
        // Panel para el mensaje
        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Noto Sans", Font.BOLD, 16));
        titleLabel.setForeground(new Color(200, 0, 0)); // Rojo oscuro
        titleLabel.setFont(new Font("Noto Sans", Font.BOLD, 16));
        titleLabel.setForeground(new Color(200, 0, 0));
        JTextArea messageArea = new JTextArea(mensaje);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBackground(panel.getBackground());
        messageArea.setFont(FONT_BODY);
        messagePanel.add(titleLabel, BorderLayout.NORTH);
        messagePanel.add(messageArea, BorderLayout.CENTER);

        // Configurar el panel principal
        messagePanel.add(titleLabel, BorderLayout.NORTH);
        messagePanel.add(messageArea, BorderLayout.CENTER);
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messagePanel, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(this, panel, "Alerta de Seguridad", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showError(String message, Component parent) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMessage(String title, String message, Component parent) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void gestionarCSV() {
        int userSelection = JOptionPane.showConfirmDialog(this,
                "¿Deseas crear un archivo 'clientes.csv' de ejemplo en la carpeta 'src'?",
                "Crear Archivo de Ejemplo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (userSelection == JOptionPane.YES_OPTION) {
            CSVClientLoader.crearEjemplo(RUTA_CSV);
            showMessage("Archivo Creado", "Archivo CSV de ejemplo creado en:\n" + new File(RUTA_CSV).getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Por favor, reinicia la aplicación para cargar los clientes del nuevo archivo.", "Reinicio Necesario", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BancoUI frame = new BancoUI();
            frame.setVisible(true);
        });
    }
}