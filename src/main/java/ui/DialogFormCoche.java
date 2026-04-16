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
        setSize(460, 580);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        setContentPane(root);

        // Cabecera
        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(VentanaPrincipal.COLOR_PRIMARIO);
        head.setBorder(new EmptyBorder(14, 20, 14, 20));
        head.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel titulo = new JLabel(esNuevo ? "🚗  Nuevo Vehículo" : "✏  Editar Vehículo");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 17));
        titulo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        head.add(titulo);
        root.add(head);

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        form.setBorder(new EmptyBorder(20, 25, 10, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMarca      = tf(); txtModelo = tf(); txtAnio = tf();
        txtColor      = tf(); txtPotencia = tf(); txtPrecio = tf();
        cbCombustible = cb("Gasolina", "Diesel", "Hibrido", "Electrico", "GLP");
        cbTransmision = cb("Manual", "Automatico");
        cbEstado      = cb("Disponible", "Reservado", "Vendido");

        // Panel de color con rueda
        txtColor.setEditable(false);
        txtColor.setText("Blanco");
        txtColor.setBackground(Color.WHITE);
        txtColor.setForeground(Color.BLACK);

        JButton btnRueda = new JButton("🎨");
        btnRueda.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnRueda.setBackground(VentanaPrincipal.COLOR_ACENTO);
        btnRueda.setForeground(Color.WHITE);
        btnRueda.setBorderPainted(false);
        btnRueda.setFocusPainted(false);
        btnRueda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRueda.setPreferredSize(new Dimension(42, 36));
        btnRueda.addActionListener(e -> {
            Color sel = JColorChooser.showDialog(this, "Elige el color del vehiculo", txtColor.getBackground());
            if (sel != null) {
                txtColor.setBackground(sel);
                String hex = String.format("#%02X%02X%02X", sel.getRed(), sel.getGreen(), sel.getBlue());
                txtColor.setText(hex);
                int brillo = (sel.getRed() * 299 + sel.getGreen() * 587 + sel.getBlue() * 114) / 1000;
                txtColor.setForeground(brillo > 128 ? Color.BLACK : Color.WHITE);
            }
        });

        JPanel panelColor = new JPanel(new BorderLayout(6, 0));
        panelColor.setOpaque(false);
        panelColor.add(txtColor,  BorderLayout.CENTER);
        panelColor.add(btnRueda,  BorderLayout.EAST);

        String[] labels = {"Marca:", "Modelo:", "Anno:", "Color:", "Combustible:", "Transmision:", "Potencia (CV):", "Precio (EUR):", "Estado:"};
        Component[] fields = {txtMarca, txtModelo, txtAnio, panelColor, cbCombustible, cbTransmision, txtPotencia, txtPrecio, cbEstado};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel l = new JLabel(labels[i]);
            l.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
            l.setFont(new Font("SansSerif", Font.PLAIN, 13));
            form.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(fields[i], gbc);
        }
        root.add(form);

        // Botones
        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        bots.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        bots.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JButton btnC = btn("Cancelar", VentanaPrincipal.COLOR_SUPERFICIE, VentanaPrincipal.COLOR_TEXTO);
        JButton btnG = btn(esNuevo ? "+ Anadir" : "Guardar", VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        bots.add(btnC);
        bots.add(btnG);
        root.add(bots);

        btnC.addActionListener(e -> { coche = null; dispose(); });
        btnG.addActionListener(e -> guardar());
    }

    private void rellenarCampos() {
        txtMarca.setText(coche.getMarca());
        txtModelo.setText(coche.getModelo());
        txtAnio.setText(String.valueOf(coche.getAnio()));
        txtColor.setText(coche.getColor());
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
            if (marca.isEmpty() || modelo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Marca y modelo son obligatorios.");
                return;
            }
            int    anio   = Integer.parseInt(txtAnio.getText().trim());
            String color  = txtColor.getText().trim();
            String comb   = (String) cbCombustible.getSelectedItem();
            String trans  = (String) cbTransmision.getSelectedItem();
            int    pot    = Integer.parseInt(txtPotencia.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().replace(",", ".").trim());
            String estado = (String) cbEstado.getSelectedItem();

            if (esNuevo) {
                coche = new Coche(0, marca, modelo, anio, color, comb, trans, pot, precio);
            } else {
                coche.setMarca(marca); coche.setModelo(modelo); coche.setAnio(anio);
                coche.setColor(color); coche.setTipoCombustible(comb); coche.setTransmision(trans);
                coche.setPotenciaCV(pot); coche.setPrecio(precio); coche.setEstado(estado);
            }
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Anno, potencia y precio deben ser numeros validos.");
        }
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
            new EmptyBorder(6, 10, 6, 10)));
        return t;
    }

    private JComboBox<String> cb(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        c.setForeground(VentanaPrincipal.COLOR_TEXTO);
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        c.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? VentanaPrincipal.COLOR_ACENTO : VentanaPrincipal.COLOR_SUPERFICIE);
                setForeground(VentanaPrincipal.COLOR_TEXTO);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
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
