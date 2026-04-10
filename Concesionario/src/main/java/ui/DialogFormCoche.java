package ui;

import model.Coche;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogFormCoche extends JDialog {

    private Coche coche;
    private boolean esNuevo;

    private JTextField txtMarca, txtModelo, txtAnio, txtColor, txtPotencia, txtPrecio;
    private JComboBox<String> cbCombustible, cbTransmision, cbEstado;

    public DialogFormCoche(Frame parent, Coche cocheEditar) {
        super(parent, cocheEditar == null ? "Nuevo Vehículo" : "Editar Vehículo", true);
        this.esNuevo = (cocheEditar == null);
        this.coche   = cocheEditar;
        inicializarUI();
        if (!esNuevo) rellenarCampos();
    }

    private void inicializarUI() {
        setSize(450, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(VentanaPrincipal.COLOR_SECUNDARIO);

        // Cabecera
        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(VentanaPrincipal.COLOR_PRIMARIO);
        head.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel(esNuevo ? "🚗  Nuevo Vehículo" : "✏  Editar Vehículo");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 17));
        titulo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        head.add(titulo);
        add(head, BorderLayout.NORTH);

        // Línea roja
        JPanel linea = new JPanel();
        linea.setBackground(VentanaPrincipal.COLOR_ACENTO);
        linea.setPreferredSize(new Dimension(0, 3));
        add(linea, BorderLayout.AFTER_LAST_LINE);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMarca      = tf(); txtModelo = tf(); txtAnio = tf();
        txtColor      = tf(); txtPotencia = tf(); txtPrecio = tf();
        cbCombustible = cb("Gasolina", "Diésel", "Híbrido", "Eléctrico", "GLP");
        cbTransmision = cb("Manual", "Automático");
        cbEstado      = cb("Disponible", "Reservado", "Vendido");

        String[] labels = {"Marca:", "Modelo:", "Año:", "Color:", "Combustible:", "Transmisión:", "Potencia (CV):", "Precio (€):", "Estado:"};
        Component[] fields = {txtMarca, txtModelo, txtAnio, txtColor, cbCombustible, cbTransmision, txtPotencia, txtPrecio, cbEstado};

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

        // Botones
        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        bots.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        JButton btnG = btn(esNuevo ? "➕ Añadir" : "💾 Guardar", VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        JButton btnC = btn("Cancelar", VentanaPrincipal.COLOR_SUPERFICIE, VentanaPrincipal.COLOR_TEXTO);
        bots.add(btnC); bots.add(btnG);
        add(bots, BorderLayout.SOUTH);

        btnC.addActionListener(e -> { coche = null; dispose(); });
        btnG.addActionListener(e -> guardar());
    }

    private void rellenarCampos() {
        txtMarca.setText(coche.getMarca()); txtModelo.setText(coche.getModelo());
        txtAnio.setText(String.valueOf(coche.getAnio())); txtColor.setText(coche.getColor());
        cbCombustible.setSelectedItem(coche.getTipoCombustible());
        cbTransmision.setSelectedItem(coche.getTransmision());
        txtPotencia.setText(String.valueOf(coche.getPotenciaCV()));
        txtPrecio.setText(String.valueOf(coche.getPrecio()));
        cbEstado.setSelectedItem(coche.getEstado());
    }

    private void guardar() {
        try {
            String marca  = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            if (marca.isEmpty() || modelo.isEmpty()) { JOptionPane.showMessageDialog(this, "Marca y modelo son obligatorios."); return; }
            int    anio    = Integer.parseInt(txtAnio.getText().trim());
            String color   = txtColor.getText().trim();
            String comb    = (String) cbCombustible.getSelectedItem();
            String trans   = (String) cbTransmision.getSelectedItem();
            int    pot     = Integer.parseInt(txtPotencia.getText().trim());
            double precio  = Double.parseDouble(txtPrecio.getText().replace(",",".").trim());
            String estado  = (String) cbEstado.getSelectedItem();

            if (esNuevo) coche = new Coche(0, marca, modelo, anio, color, comb, trans, pot, precio);
            else {
                coche.setMarca(marca); coche.setModelo(modelo); coche.setAnio(anio);
                coche.setColor(color); coche.setTipoCombustible(comb); coche.setTransmision(trans);
                coche.setPotenciaCV(pot); coche.setPrecio(precio); coche.setEstado(estado);
            }
            dispose();
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Año, potencia y precio deben ser números válidos."); }
    }

    public Coche getCoche() { return coche; }

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

    private JComboBox<String> cb(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        c.setForeground(VentanaPrincipal.COLOR_TEXTO);
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return c;
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
