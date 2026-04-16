package ui;

import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogFormCliente extends JDialog {

    private Cliente cliente;
    private boolean esNuevo;
    private JTextField txtNombre, txtApellidos, txtDni, txtTelefono, txtEmail, txtDireccion;
    public DialogFormCliente(Frame parent, Cliente clienteEditar) {
        super(parent, clienteEditar == null ? "Nuevo Cliente" : "Editar Cliente", true);
        this.esNuevo = (clienteEditar == null);
        this.cliente = clienteEditar;
        inicializarUI();
        if (!esNuevo) rellenarCampos();
    }

    private void inicializarUI() {
        setSize(440, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(VentanaPrincipal.COLOR_SECUNDARIO);

        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(VentanaPrincipal.COLOR_PRIMARIO);
        head.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel(esNuevo ? "👤  Nuevo Cliente" : "✏  Editar Cliente");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 17));
        titulo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        head.add(titulo);
        add(head, BorderLayout.NORTH);

        txtNombre = tf(); txtApellidos = tf(); txtDni = tf();
        txtTelefono = tf(); txtEmail = tf(); txtDireccion = tf();

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Nombre:", "Apellidos:", "DNI:", "Teléfono:", "Email:", "Dirección:"};
        JTextField[] fields = {txtNombre, txtApellidos, txtDni, txtTelefono, txtEmail, txtDireccion};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel l = new JLabel(labels[i]);
            l.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
            l.setFont(new Font("SansSerif", Font.PLAIN, 13));
            form.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(fields[i], gbc);
        }
        add(form, BorderLayout.CENTER);

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        bots.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        JButton btnG = btn(esNuevo ? "➕ Añadir" : "💾 Guardar", VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        JButton btnC = btn("Cancelar", VentanaPrincipal.COLOR_SUPERFICIE, VentanaPrincipal.COLOR_TEXTO);
        bots.add(btnC); bots.add(btnG);
        add(bots, BorderLayout.SOUTH);

        btnC.addActionListener(e -> { cliente = null; dispose(); });
        btnG.addActionListener(e -> guardar());
    }

    private void rellenarCampos() {
        txtNombre.setText(cliente.getNombre()); txtApellidos.setText(cliente.getApellidos());
        txtDni.setText(cliente.getDni()); txtTelefono.setText(cliente.getTelefono());
        txtEmail.setText(cliente.getEmail()); txtDireccion.setText(cliente.getDireccion());
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim(), apellidos = txtApellidos.getText().trim(), dni = txtDni.getText().trim();
        if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty()) { JOptionPane.showMessageDialog(this, "Nombre, apellidos y DNI son obligatorios."); return; }
        if (esNuevo) cliente = new Cliente(0, nombre, apellidos, dni, txtTelefono.getText().trim(), txtEmail.getText().trim(), txtDireccion.getText().trim());
        else {
            cliente.setNombre(nombre); cliente.setApellidos(apellidos); cliente.setDni(dni);
            cliente.setTelefono(txtTelefono.getText().trim()); cliente.setEmail(txtEmail.getText().trim()); cliente.setDireccion(txtDireccion.getText().trim());
        }
        dispose();
    }

    public Cliente getCliente() { return cliente; }

    private JTextField tf() {
        JTextField t = new JTextField();
        t.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        t.setForeground(VentanaPrincipal.COLOR_TEXTO);
        t.setCaretColor(VentanaPrincipal.COLOR_TEXTO);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE),
            new EmptyBorder(6,10,6,10)));
        return t;
    }

    private JButton btn(String texto, Color bg, Color fg) {
        JButton b = new JButton(texto);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }
}
