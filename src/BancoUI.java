// Archivo: BancoUI.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class BancoUI extends JFrame {

    // --- SECCIÓN 1: LÓGICA DEL SISTEMA (BACKEND) ---
    private HashTable clientesTable;
    private Cliente clienteSesion;
    // private Stack<String> pilaHistorial; // ELIMINADO: Ya no hay un historial global
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

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

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
        // pilaHistorial = new Stack<>(); // ELIMINADO
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


    // =========================================================================
    // PANELES DE LA INTERFAZ (DISEÑO MEJORADO)
    // =========================================================================

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

    // =========================================================================
    // LÓGICA DE ACCIONES Y EVENTOS
    // =========================================================================


    private void accionVerHistorial() {
        // MODIFICADO: Usa el historial del cliente con sesión activa
        if (clienteSesion.getPilaHistorial().isEmpty()) {
            showMessage("Historial Vacío", "No hay movimientos en el historial.");
            return;
        }

        StringBuilder historialTexto = new StringBuilder("Últimos movimientos (Más recientes primero):\n---------------------------------------------\n");
        Stack<String> tempStack = new Stack<>();
        try {
            // MODIFICADO: Usa el historial del cliente con sesión activa
            Node<String> actual = clienteSesion.getPilaHistorial().historial.firstNode;
            while (actual != null) {
                tempStack.push(actual.getData());
                actual = actual.next;
            }

            while (!tempStack.isEmpty()) {
                historialTexto.append(tempStack.peek()).append("\n");
                tempStack.pop();
            }
        } catch (Exception e) {
            historialTexto.append("Error al leer el historial.");
        }

        JTextArea textArea = new JTextArea(historialTexto.toString());
        textArea.setFont(FONT_BODY);
        textArea.setEditable(false);
        textArea.setBackground(COLOR_BACKGROUND);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Historial de Movimientos", JOptionPane.PLAIN_MESSAGE);
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
        // pilaHistorial.clear(); // ELIMINADO: Ya no se limpia el historial
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
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a depositar:", "Depósito", JOptionPane.PLAIN_MESSAGE);
        try {
            int monto = Integer.parseInt(montoStr);
            if (monto <= 0) { showError("El monto debe ser positivo."); return; }
            clienteSesion.Depositar(monto);
            // MODIFICADO: Usa el historial del cliente
            clienteSesion.getPilaHistorial().push("Deposito: +$" + monto);
            actualizarInfoCliente();
            showMessage("Éxito", "Depósito de $" + monto + " realizado con éxito.");
        } catch (Exception e) { showError("Por favor, ingrese un número válido."); }
    }

    private void accionRetirar() {
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a retirar:", "Retiro", JOptionPane.PLAIN_MESSAGE);
        try {
            int monto = Integer.parseInt(montoStr);
            if (monto <= 0) { showError("El monto debe ser positivo."); return; }
            if (monto > clienteSesion.Monto) { showError("Fondos insuficientes."); return; }
            clienteSesion.Monto -= monto;
            // MODIFICADO: Usa el historial del cliente
            clienteSesion.getPilaHistorial().push("Retiro: -$" + monto);
            actualizarInfoCliente();
            showMessage("Éxito", "Retiro de $" + monto + " realizado con éxito.");
        } catch (Exception e) { showError("Por favor, ingrese un número válido."); }
    }

    private void accionTransferir() {
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

                clienteSesion.Monto -= monto;
                destinatario.Depositar(monto);
                colaTransferencias.enqueue(monto);

                // --- MODIFICACIÓN CLAVE ---
                // 1. Añade la transferencia ENVIADA al historial del cliente actual
                clienteSesion.getPilaHistorial().push("Transferencia a " + destinatario.Nombre + ": -$" + monto);

                // 2. Añade la transferencia RECIBIDA al historial del cliente destinatario
                destinatario.getPilaHistorial().push("Transferencia de " + clienteSesion.Nombre + ": +$" + monto);
                // -------------------------

                actualizarInfoCliente();
                showMessage("Éxito", "Transferencia realizada con éxito.");
            } catch (Exception e) { showError("Datos inválidos. Verifique la información."); }
        }
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
                    // MODIFICADO: Usa el historial del cliente
                    clienteSesion.getPilaHistorial().push(String.format("Depósito Ahorros: -$%d", monto));
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
                    // MODIFICADO: Usa el historial del cliente
                    clienteSesion.getPilaHistorial().push(String.format("Retiro Ahorros: +$%d", monto));
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

    private void showError(String message) { JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE); }
    private void showError(String message, Component parent) { JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE); }
    private void showMessage(String title, String message) { JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE); }
    private void showMessage(String title, String message, Component parent) { JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE); }

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