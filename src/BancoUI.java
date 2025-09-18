// Archivo: BancoUI.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;


public class BancoUI extends JFrame {

    // --- SECCIÓN 1: LÓGICA DEL SISTEMA (BACKEND) ---
    private HashTable clientesTable;
    private Cliente clienteSesion;
    private Stack<String> pilaHistorial;
    private Queue<String> colaTransferencias;

    // --- SECCIÓN 2: COMPONENTES DE LA INTERFAZ (FRONTEND) ---
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel mainMenuPanel;

    // Componentes del LoginPanel
    private JTextField loginTarjetaField;
    private JTextField loginIdField;
    private JTextField loginNombreField;
    private JTextField loginApellidoField;

    // Componentes del MainMenuPanel
    private JLabel clienteLabel;
    private JLabel saldoLabel;
    private JLabel ahorrosLabel; // <-- NUEVO LABEL PARA AHORROS

    public BancoUI() {
        // --- INICIALIZACIÓN DEL BACKEND ---
        inicializarSistemaBancario();

        // --- CONFIGURACIÓN DE LA VENTANA PRINCIPAL ---
        setTitle("Banco Financiero GUI");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla

        // --- PANEL PRINCIPAL CON CARDLAYOUT ---
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- CREAR PANELES ---
        crearLoginPanel();
        crearMainMenuPanel();

        // --- AGREGAR PANELES AL CARDLAYOUT ---
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(mainMenuPanel, "MainMenu");

        // --- MOSTRAR EL PANEL INICIAL Y HACER VISIBLE LA VENTANA ---
        add(mainPanel);
        cardLayout.show(mainPanel, "Login"); // Empezar en la pantalla de login
    }

    private void inicializarSistemaBancario() {
        clientesTable = new HashTable(10);
        pilaHistorial = new Stack<>();
        colaTransferencias = new Queue<>();

        // Agregar 5 clientes predefinidos (lógica de Main.java)
        agregarCliente(1, "Juan Perez", 7500, "5201169781530257");
        agregarCliente(2, "Maria Garcia", 12000, "4509297861614535");
        agregarCliente(3, "Carlos Lopez", 3500, "4555061037596247");
        agregarCliente(4, "Ana Rodriguez", 9800, "4915762317479773");
        agregarCliente(5, "Pedro Martinez", 15000, "5161034964107141");
    }

    private void agregarCliente(int id, String nombre, int monto, String numeroTarjeta) {
        Cliente cliente = new Cliente(id, nombre, monto, numeroTarjeta);
        clientesTable.put(numeroTarjeta, cliente);
    }

    // =========================================================================
    // CREACIÓN DEL PANEL DE INICIO DE SESIÓN
    // =========================================================================
    private void crearLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        loginPanel.setBackground(new Color(240, 240, 240));

        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        JLabel titleLabel = new JLabel("Bienvenido al Banco");
        titleLabel.setFont(titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginTarjetaField = new JTextField(20);
        loginIdField = new JTextField(20);
        loginNombreField = new JTextField(20);
        loginApellidoField = new JTextField(20);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(buttonFont);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerButton = new JButton("Registrar Nuevo Cliente");
        registerButton.setFont(buttonFont);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- LAYOUT Y AÑADIR COMPONENTES ---
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        addFormField(loginPanel, "Nº Tarjeta (16 dígitos):", loginTarjetaField, labelFont);
        addFormField(loginPanel, "ID de Cliente:", loginIdField, labelFont);
        addFormField(loginPanel, "Nombre:", loginNombreField, labelFont);
        addFormField(loginPanel, "Apellido:", loginApellidoField, labelFont);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(registerButton);

        // --- ACCIONES DE LOS BOTONES ---
        loginButton.addActionListener(e -> intentarLogin());
        registerButton.addActionListener(e -> mostrarDialogoRegistro());
    }

    // =========================================================================
    // CREACIÓN DEL PANEL DEL MENÚ PRINCIPAL
    // =========================================================================
    private void crearMainMenuPanel() {
        mainMenuPanel = new JPanel(new BorderLayout(10, 10));
        mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- PANEL DE INFORMACIÓN DEL CLIENTE (NORTE) ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Cliente"));

        clienteLabel = new JLabel("Cliente: ");
        saldoLabel = new JLabel("Saldo Principal: $");
        ahorrosLabel = new JLabel("Saldo Ahorrado: $"); // <-- INICIALIZACIÓN DEL NUEVO LABEL

        clienteLabel.setFont(new Font("Arial", Font.BOLD, 16));
        saldoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ahorrosLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(clienteLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(saldoLabel);
        infoPanel.add(ahorrosLabel); // <-- AÑADIDO AL PANEL

        // --- PANEL DE BOTONES DE ACCIONES (CENTRO) ---
        JPanel actionsPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JButton depositarBtn = new JButton("Realizar Depósito");
        JButton retirarBtn = new JButton("Retirar Monto");
        JButton transferirBtn = new JButton("Transferir");
        JButton historialBtn = new JButton("Historial de Movimientos");
        JButton ahorrosBtn = new JButton("Caja de Inversión");
        JButton avanzadasBtn = new JButton("Funciones Avanzadas");
        JButton logoutBtn = new JButton("Cerrar Sesión");
        logoutBtn.setBackground(new Color(220, 50, 50));
        logoutBtn.setForeground(Color.WHITE);

        actionsPanel.add(depositarBtn);
        actionsPanel.add(retirarBtn);
        actionsPanel.add(transferirBtn);
        actionsPanel.add(historialBtn);
        actionsPanel.add(ahorrosBtn);
        actionsPanel.add(avanzadasBtn);

        mainMenuPanel.add(infoPanel, BorderLayout.NORTH);
        mainMenuPanel.add(actionsPanel, BorderLayout.CENTER);
        mainMenuPanel.add(logoutBtn, BorderLayout.SOUTH);

        // --- ACCIONES DE LOS BOTONES DEL MENÚ ---
        depositarBtn.addActionListener(e -> accionDepositar());
        retirarBtn.addActionListener(e -> accionRetirar());
        historialBtn.addActionListener(e -> accionVerHistorial());
        transferirBtn.addActionListener(e -> accionTransferir());
        logoutBtn.addActionListener(e -> cerrarSesion());
        ahorrosBtn.addActionListener(e -> mostrarDialogoCajaAhorros()); // <-- LLAMA AL NUEVO SUBMENÚ
    }

    // =========================================================================
    // LÓGICA DE ACCIONES Y EVENTOS
    // =========================================================================

    // --- Lógica de Login/Registro (sin cambios) ---
    private void intentarLogin() {
        String tarjeta = loginTarjetaField.getText();
        String idStr = loginIdField.getText();
        String nombre = loginNombreField.getText();
        String apellido = loginApellidoField.getText();

        if (tarjeta.isEmpty() || idStr.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidadorTarjeta.validarTarjeta(tarjeta)) {
            JOptionPane.showMessageDialog(this, "Formato de tarjeta no válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente clienteEncontrado = clientesTable.get(tarjeta);

        if (clienteEncontrado == null) {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idUsuario = Integer.parseInt(idStr);
            String nombreCompleto = nombre + " " + apellido;

            if (clienteEncontrado.ID == idUsuario && clienteEncontrado.Nombre.equalsIgnoreCase(nombreCompleto)) {
                clienteSesion = clienteEncontrado;
                pilaHistorial.clear(); // Limpiar historial para la nueva sesión
                actualizarInfoCliente();
                cardLayout.show(mainPanel, "MainMenu");
                JOptionPane.showMessageDialog(this, "¡Inicio de sesión exitoso! Bienvenido, " + clienteSesion.Nombre, "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistro() {
        // Usamos un JPanel personalizado para un diálogo más complejo
        JTextField nombreField = new JTextField(20);
        JTextField apellidoField = new JTextField(20);
        JTextField tarjetaField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Apellido:"));
        panel.add(apellidoField);
        panel.add(new JLabel("Nº de Tarjeta (16 dígitos):"));
        panel.add(tarjetaField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registro de Nuevo Cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String tarjeta = tarjetaField.getText();

            if (nombre.isEmpty() || apellido.isEmpty() || tarjeta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (clientesTable.get(tarjeta) != null) {
                JOptionPane.showMessageDialog(this, "Esta tarjeta ya está registrada.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (ValidadorTarjeta.validarTarjeta(tarjeta)) {
                String nombreCompleto = nombre + " " + apellido;
                int nuevoID = clientesTable.size() + 1; // ID secuencial
                agregarCliente(nuevoID, nombreCompleto, 0, tarjeta); // Saldo inicial 0
                JOptionPane.showMessageDialog(this, "¡Cliente '" + nombreCompleto + "' registrado con ID: " + nuevoID + "!\nAhora puede iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Número de tarjeta no válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- Lógica de acciones del menú (sin cambios) ---
    private void accionDepositar() {
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a depositar:", "Depósito", JOptionPane.PLAIN_MESSAGE);
        try {
            int monto = Integer.parseInt(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            clienteSesion.Depositar(monto);
            pilaHistorial.push("Deposito: +$" + monto);
            actualizarInfoCliente();
            JOptionPane.showMessageDialog(this, "Depósito de $" + monto + " realizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e){} // Para el push de la pila que puede tener IO Exception
    }

    private void accionRetirar() {
        String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a retirar:", "Retiro", JOptionPane.PLAIN_MESSAGE);
        try {
            int monto = Integer.parseInt(montoStr);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (monto > clienteSesion.Monto) {
                JOptionPane.showMessageDialog(this, "Fondos insuficientes.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            clienteSesion.Monto -= monto;
            pilaHistorial.push("Retiro: -$" + monto);
            actualizarInfoCliente();
            JOptionPane.showMessageDialog(this, "Retiro de $" + monto + " realizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e){}
    }

    private void accionVerHistorial() {
        if (pilaHistorial.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay movimientos en el historial.", "Historial Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder historialTexto = new StringBuilder("Últimos movimientos:\n---------------------\n");
        Stack<String> tempStack = new Stack<>();
        try {
            while(!pilaHistorial.isEmpty()) {
                String movimiento = pilaHistorial.peek();
                pilaHistorial.pop();
                tempStack.push(movimiento);
            }

            while(!tempStack.isEmpty()){
                String movimiento = tempStack.peek();
                tempStack.pop();
                historialTexto.append(movimiento).append("\n");
                pilaHistorial.push(movimiento);
            }

        } catch (Exception e) {}

        JTextArea textArea = new JTextArea(historialTexto.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Historial de Movimientos", JOptionPane.PLAIN_MESSAGE);
    }

    private void accionTransferir() {
        JTextField tarjetaField = new JTextField(20);
        JTextField idField = new JTextField(20);
        JTextField montoField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nº Tarjeta del Destinatario:"));
        panel.add(tarjetaField);
        panel.add(new JLabel("ID del Destinatario:"));
        panel.add(idField);
        panel.add(new JLabel("Monto a Transferir:"));
        panel.add(montoField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Realizar Transferencia",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String tarjetaDest = tarjetaField.getText();
                int idDest = Integer.parseInt(idField.getText());
                int monto = Integer.parseInt(montoField.getText());

                Cliente destinatario = clientesTable.get(tarjetaDest);

                if (destinatario == null) {
                    JOptionPane.showMessageDialog(this, "Nº de Tarjeta no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (destinatario.ID != idDest) {
                    JOptionPane.showMessageDialog(this, "El ID no corresponde al titular de la tarjeta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (monto <= 0 || monto > clienteSesion.Monto) {
                    JOptionPane.showMessageDialog(this, "Monto no válido o fondos insuficientes.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                clienteSesion.Monto -= monto;
                destinatario.Depositar(monto);
                colaTransferencias.enqueue(monto);
                pilaHistorial.push("Transferencia a " + destinatario.Nombre + ": -$" + monto);

                actualizarInfoCliente();
                JOptionPane.showMessageDialog(this, "Transferencia realizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Datos inválidos. Verifique la información.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================================================================
    // NUEVA LÓGICA MEJORADA PARA LA CAJA DE AHORROS
    // =========================================================================
    private void mostrarDialogoCajaAhorros() {
        final int LIMITE_AHORROS = 500000;

        // Crear un JDialog para el submenú
        JDialog ahorrosDialog = new JDialog(this, "Caja de Inversión / Ahorros", true);
        ahorrosDialog.setSize(400, 300);
        ahorrosDialog.setLocationRelativeTo(this);
        ahorrosDialog.setLayout(new BorderLayout(10, 10));

        // Panel de información de saldos
        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.Y_AXIS));
        balancePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel ahorrosActualLabel = new JLabel(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros));
        ahorrosActualLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel principalActualLabel = new JLabel(String.format("Saldo Principal: $%,d", clienteSesion.Monto));
        balancePanel.add(ahorrosActualLabel);
        balancePanel.add(principalActualLabel);

        // Panel de botones de acciones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton depositarAhorrosBtn = new JButton("Depositar en Ahorros");
        JButton retirarAhorrosBtn = new JButton("Retirar de Ahorros");
        JButton proyeccionBtn = new JButton("Ver Proyección");

        botonesPanel.add(depositarAhorrosBtn);
        botonesPanel.add(retirarAhorrosBtn);
        botonesPanel.add(proyeccionBtn);

        ahorrosDialog.add(balancePanel, BorderLayout.NORTH);
        ahorrosDialog.add(botonesPanel, BorderLayout.CENTER);

        // --- ACCIONES DE LOS BOTONES DEL SUBMENÚ ---

        depositarAhorrosBtn.addActionListener(e -> {
            String montoStr = JOptionPane.showInputDialog(ahorrosDialog, "Monto a depositar en ahorros (mín. $100):", "Depositar", JOptionPane.PLAIN_MESSAGE);
            try {
                int monto = Integer.parseInt(montoStr);
                if (monto < 100) {
                    JOptionPane.showMessageDialog(ahorrosDialog, "Error: El depósito mínimo es de $100.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (monto > clienteSesion.Monto) {
                    JOptionPane.showMessageDialog(ahorrosDialog, "Error: Fondos insuficientes en cuenta principal.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (clienteSesion.montoAhorros + monto > LIMITE_AHORROS) {
                    JOptionPane.showMessageDialog(ahorrosDialog, "Error: Esta operación superaría el límite de $" + LIMITE_AHORROS, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    clienteSesion.Monto -= monto;
                    clienteSesion.montoAhorros += monto;
                    pilaHistorial.push(String.format("Depósito Ahorros: -$%d", monto));
                    JOptionPane.showMessageDialog(ahorrosDialog, "Depósito a ahorros exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Actualizar todo
                    actualizarInfoCliente();
                    ahorrosActualLabel.setText(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros));
                    principalActualLabel.setText(String.format("Saldo Principal: $%,d", clienteSesion.Monto));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ahorrosDialog, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        retirarAhorrosBtn.addActionListener(e -> {
            String montoStr = JOptionPane.showInputDialog(ahorrosDialog, "Monto a retirar de ahorros:", "Retirar", JOptionPane.PLAIN_MESSAGE);
            try {
                int monto = Integer.parseInt(montoStr);
                if (monto > 0 && monto <= clienteSesion.montoAhorros) {
                    clienteSesion.montoAhorros -= monto;
                    clienteSesion.Monto += monto;
                    pilaHistorial.push(String.format("Retiro Ahorros: +$%d", monto));
                    JOptionPane.showMessageDialog(ahorrosDialog, "Retiro de ahorros exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Actualizar todo
                    actualizarInfoCliente();
                    ahorrosActualLabel.setText(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros));
                    principalActualLabel.setText(String.format("Saldo Principal: $%,d", clienteSesion.Monto));
                } else {
                    JOptionPane.showMessageDialog(ahorrosDialog, "Error: Monto no válido o fondos insuficientes en ahorros.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ahorrosDialog, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        proyeccionBtn.addActionListener(e -> {
            if (clienteSesion.montoAhorros > 0) {
                final double TASA_ANUAL = 0.07;
                final double TASA_MENSUAL = TASA_ANUAL / 12.0;

                double en1Mes = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 1);
                double en6Meses = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 6);
                double en1Anio = calcularCrecimientoAhorros(clienteSesion.montoAhorros, TASA_MENSUAL, 12);

                String proyeccionTexto = String.format(
                        "Proyección para un saldo de $%,d (Tasa Anual del 7%%):\n\n" +
                                "   - En 1 mes:   $%,d\n" +
                                "   - En 6 meses: $%,d\n" +
                                "   - En 1 año:   $%,d\n",
                        clienteSesion.montoAhorros, (long)en1Mes, (long)en6Meses, (long)en1Anio
                );

                JOptionPane.showMessageDialog(ahorrosDialog, proyeccionTexto, "Proyección de Crecimiento", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(ahorrosDialog, "No hay fondos en la caja de ahorros para realizar una proyección.", "Ahorros Vacíos", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ahorrosDialog.setVisible(true);
    }

    /**
     * MÉTODO RECURSIVO para calcular el crecimiento de una inversión.
     * Copiado de la lógica original de Main.java.
     */
    private static double calcularCrecimientoAhorros(double capital, double tasaMensual, int meses) {
        if (meses == 0) return capital;
        return calcularCrecimientoAhorros(capital * (1 + tasaMensual), tasaMensual, meses - 1);
    }

    // --- Métodos de utilidad y cierre de sesión (sin cambios) ---
    private void cerrarSesion() {
        clienteSesion = null;
        loginTarjetaField.setText("");
        loginIdField.setText("");
        loginNombreField.setText("");
        loginApellidoField.setText("");
        cardLayout.show(mainPanel, "Login");
    }

    private void actualizarInfoCliente() {
        if (clienteSesion != null) {
            clienteLabel.setText("Cliente: " + clienteSesion.Nombre + " (ID: " + clienteSesion.ID + ")");
            saldoLabel.setText(String.format("Saldo Principal: $%,d", clienteSesion.Monto));
            ahorrosLabel.setText(String.format("Saldo Ahorrado: $%,d", clienteSesion.montoAhorros));
        }
    }

    private void addFormField(JPanel panel, String labelText, JTextField textField, Font font) {
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(textField, BorderLayout.CENTER);
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height + 10));
        fieldPanel.setBackground(panel.getBackground());
        panel.add(fieldPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }


    // =========================================================================
    // PUNTO DE ENTRADA DE LA APLICACIÓN
    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BancoUI frame = new BancoUI();
            frame.setVisible(true);
        });
    }
}