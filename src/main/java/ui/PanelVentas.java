package ui;

import dao.CocheDAO;
import dao.ClienteDAO;
import dao.VentaDAO;
import model.Coche;
import model.Cliente;
import model.Venta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanelVentas extends JPanel {

    private VentaDAO ventaDAO;
    private CocheDAO cocheDAO;
    private ClienteDAO clienteDAO;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblNumVentas;
    private JLabel lblTotalFacturado;

    public PanelVentas(VentaDAO ventaDAO, CocheDAO cocheDAO, ClienteDAO clienteDAO) {
        this.ventaDAO   = ventaDAO;
        this.cocheDAO   = cocheDAO;
        this.clienteDAO = clienteDAO;
        inicializarUI();
        cargarTabla(ventaDAO.listarTodas());
    }

    private void inicializarUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(VentanaPrincipal.COLOR_FONDO);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Cabecera
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);
        JLabel titulo    = new JLabel("REGISTRO DE VENTAS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        JLabel subtitulo = new JLabel("Historial de transacciones del concesionario");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(4));
        tituloPanel.add(subtitulo);
        cabecera.add(tituloPanel, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);
        JButton btnNueva    = crearBoton("+ Nueva Venta",  VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        JButton btnAnular   = crearBoton("✖ Anular Venta", new Color(40,40,50), VentanaPrincipal.COLOR_TEXTO_SUAVE);
        panelBotones.add(btnNueva); panelBotones.add(btnAnular);
        cabecera.add(panelBotones, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        // Stats cards
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        stats.setOpaque(false);
        stats.setBorder(new EmptyBorder(0, 0, 15, 0));

        lblNumVentas      = crearStatCard("🧾 Ventas totales", "0",       new Color(59, 130, 246));
        lblTotalFacturado = crearStatCard("💶 Facturado",       "0,00 €",  new Color(34, 197, 94));

        stats.add(wrapStat("🧾 Ventas", lblNumVentas,      new Color(59,130,246)));
        stats.add(wrapStat("💶 Facturado", lblTotalFacturado, new Color(34,197,94)));

        // Tabla
        String[] cols = {"ID", "Cliente", "Vehículo", "Fecha", "Precio (€)", "Forma Pago", "Observaciones"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? VentanaPrincipal.COLOR_SECUNDARIO : VentanaPrincipal.COLOR_PRIMARIO);
                    c.setForeground(VentanaPrincipal.COLOR_TEXTO);
                } else {
                    c.setBackground(new Color(220, 38, 38, 80));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE));
        scroll.getViewport().setBackground(VentanaPrincipal.COLOR_SECUNDARIO);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setOpaque(false);
        sur.add(stats,  BorderLayout.NORTH);
        sur.add(scroll, BorderLayout.CENTER);
        add(sur, BorderLayout.CENTER);

        btnNueva.addActionListener(e  -> abrirFormVenta());
        btnAnular.addActionListener(e -> anularVenta());
    }

    private JPanel wrapStat(String titulo, JLabel valorLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(VentanaPrincipal.COLOR_SUPERFICIE);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g.setColor(color);
                g.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 18, 12, 18));
        card.setPreferredSize(new Dimension(200, 65));

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lTitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);

        valorLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        valorLabel.setForeground(color);

        card.add(lTitulo,    BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);
        return card;
    }

    private JLabel crearStatCard(String titulo, String valor, Color color) {
        return new JLabel(valor);
    }

    private void cargarTabla(List<Venta> ventas) {
        modeloTabla.setRowCount(0);
        for (Venta v : ventas)
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getCliente().getNombre() + " " + v.getCliente().getApellidos(),
                v.getCoche().getMarca() + " " + v.getCoche().getModelo(),
                v.getFechaVenta().toString(),
                String.format("%.2f", v.getPrecioFinal()),
                v.getFormaPago(),
                v.getObservaciones()
            });
        lblNumVentas.setText(String.valueOf(ventaDAO.numeroVentas()));
        lblTotalFacturado.setText(String.format("%.2f €", ventaDAO.totalFacturado()));
    }

    private void abrirFormVenta() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        List<Coche>   disponibles = cocheDAO.listarDisponibles();
        if (clientes.isEmpty())    { JOptionPane.showMessageDialog(this, "No hay clientes registrados."); return; }
        if (disponibles.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay vehículos disponibles."); return; }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Venta", true);
        dialog.setSize(480, 400);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        dialog.setLayout(new BorderLayout());

        // Cabecera del diálogo
        JPanel dHead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dHead.setBackground(VentanaPrincipal.COLOR_PRIMARIO);
        dHead.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel dTitulo = new JLabel("💰  Registrar Nueva Venta");
        dTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        dTitulo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        dHead.add(dTitulo);
        dialog.add(dHead, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Cliente> cbCliente = new JComboBox<>(clientes.toArray(new Cliente[0]));
        JComboBox<Coche>   cbCoche   = new JComboBox<>(disponibles.toArray(new Coche[0]));
        JTextField txtPrecio         = estilizarTextField(new JTextField());
        JComboBox<String>  cbPago    = new JComboBox<>(new String[]{"Contado", "Financiado", "Leasing"});
        JTextField txtObs            = estilizarTextField(new JTextField());

        estilizarCombo(cbCliente); estilizarCombo(cbCoche); estilizarCombo(cbPago);

        cbCoche.addActionListener(e -> {
            Coche c = (Coche) cbCoche.getSelectedItem();
            if (c != null) txtPrecio.setText(String.format("%.2f", c.getPrecio()));
        });
        if (!disponibles.isEmpty()) txtPrecio.setText(String.format("%.2f", disponibles.get(0).getPrecio()));

        String[] labels = {"Cliente:", "Vehículo:", "Precio Final (€):", "Forma de pago:", "Observaciones:"};
        Component[] fields = {cbCliente, cbCoche, txtPrecio, cbPago, txtObs};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel l = new JLabel(labels[i]);
            l.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
            l.setFont(new Font("SansSerif", Font.PLAIN, 13));
            form.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(fields[i], gbc);
        }
        dialog.add(form, BorderLayout.CENTER);

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bots.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        JButton btnGuardar  = crearBoton("✅ Registrar", VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        JButton btnCancelar = crearBoton("Cancelar",    VentanaPrincipal.COLOR_SUPERFICIE, VentanaPrincipal.COLOR_TEXTO);
        bots.add(btnCancelar); bots.add(btnGuardar);
        dialog.add(bots, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            try {
                double precio = Double.parseDouble(txtPrecio.getText().replace(",", "."));
                Venta v = new Venta(0, (Cliente) cbCliente.getSelectedItem(),
                        (Coche) cbCoche.getSelectedItem(), LocalDate.now(),
                        precio, (String) cbPago.getSelectedItem(), txtObs.getText());
                ventaDAO.agregar(v);
                cargarTabla(ventaDAO.listarTodas());
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "✅ Venta registrada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "El precio no es válido.");
            }
        });
        dialog.setVisible(true);
    }

    private void anularVenta() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una venta para anular."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Anular esta venta? El vehículo volverá a estar disponible.", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            ventaDAO.eliminar((int) modeloTabla.getValueAt(fila, 0));
            cargarTabla(ventaDAO.listarTodas());
        }
    }

    public void refrescar() { cargarTabla(ventaDAO.listarTodas()); }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg); btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 16, 9, 16));
        return btn;
    }

    private JTextField estilizarTextField(JTextField tf) {
        tf.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        tf.setForeground(VentanaPrincipal.COLOR_TEXTO);
        tf.setCaretColor(VentanaPrincipal.COLOR_TEXTO);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE),
            new EmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    private void estilizarCombo(JComboBox<?> cb) {
        cb.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        cb.setForeground(VentanaPrincipal.COLOR_TEXTO);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // index == -1 es el campo cerrado (valor seleccionado visible)
                if (index == -1) {
                    setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
                    setForeground(VentanaPrincipal.COLOR_TEXTO);
                } else {
                    setBackground(isSelected ? VentanaPrincipal.COLOR_ACENTO : VentanaPrincipal.COLOR_SUPERFICIE);
                    setForeground(VentanaPrincipal.COLOR_TEXTO);
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    private void estilizarTabla(JTable t) {
        t.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        t.setForeground(VentanaPrincipal.COLOR_TEXTO);
        t.setRowHeight(36);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setGridColor(VentanaPrincipal.COLOR_BORDE);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(new Color(220, 38, 38, 80));
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(VentanaPrincipal.COLOR_PRIMARIO);
        t.getTableHeader().setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, VentanaPrincipal.COLOR_ACENTO));
        t.setIntercellSpacing(new Dimension(0, 1));
    }
}
