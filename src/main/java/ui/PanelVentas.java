package ui;

import dao.CocheDAO;
import dao.ClienteDAO;
import dao.VentaDAO;
import model.Coche;
import model.Cliente;
import model.Venta;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.List;

public class PanelVentas extends JPanel {

    private VentaDAO ventaDAO;
    private CocheDAO cocheDAO;
    private ClienteDAO clienteDAO;

    private JPanel panelTarjetas;
    private JLabel lblNumVentas;
    private JLabel lblTotal;

    private static final Color BG           = new Color(248, 248, 248);
    private static final Color CARD_BG      = Color.WHITE;
    private static final Color TEXTO_DARK   = new Color(20, 20, 20);
    private static final Color TEXTO_GRAY   = new Color(100, 100, 100);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color VERDE        = new Color(34, 139, 34);
    private static final Color ACENTO       = new Color(0, 100, 180);

    public PanelVentas(VentaDAO ventaDAO, CocheDAO cocheDAO, ClienteDAO clienteDAO) {
        this.ventaDAO   = ventaDAO;
        this.cocheDAO   = cocheDAO;
        this.clienteDAO = clienteDAO;
        inicializarUI();
        cargarTarjetas(ventaDAO.listarTodas());
    }

    private void inicializarUI() {
        setLayout(new BorderLayout());
        setBackground(BG);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(18, 30, 18, 30)));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);
        JLabel titulo    = new JLabel("Registro de Ventas");
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 26));
        titulo.setForeground(TEXTO_DARK);
        JLabel subtitulo = new JLabel("Historial de transacciones del concesionario");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_GRAY);
        tituloPanel.add(titulo); tituloPanel.add(Box.createVerticalStrut(3)); tituloPanel.add(subtitulo);
        topBar.add(tituloPanel, BorderLayout.WEST);

        JButton btnNueva = btnPrimario("+ Nueva venta");
        btnNueva.addActionListener(e -> abrirFormVenta());
        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acc.setOpaque(false); acc.add(btnNueva);
        topBar.add(acc, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Stats
        lblNumVentas = new JLabel("0");
        lblTotal     = new JLabel("0,00 €");

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        stats.setBackground(BG);
        stats.setBorder(new EmptyBorder(0, 20, 0, 20));
        stats.add(crearStatCard("Ventas realizadas", lblNumVentas, ACENTO));
        stats.add(crearStatCard("Total facturado",   lblTotal,     VERDE));

        // Tarjetas
        panelTarjetas = new JPanel(new PanelCoches.WrapLayout(FlowLayout.LEFT, 20, 20));
        panelTarjetas.setBackground(BG);
        panelTarjetas.setBorder(new EmptyBorder(5, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(panelTarjetas);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(BG);
        centro.add(stats,  BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearStatCard(String titulo, JLabel valorLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(color);
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(200, 70));
        card.setBorder(BorderFactory.createCompoundBorder(
            new PanelCoches.RoundedBorder(10, BORDER_COLOR),
            new EmptyBorder(12, 18, 12, 18)));

        JLabel lTit = new JLabel(titulo);
        lTit.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lTit.setForeground(TEXTO_GRAY);

        valorLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valorLabel.setForeground(color);

        card.add(lTit,       BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);
        return card;
    }

    private void cargarTarjetas(List<Venta> ventas) {
        panelTarjetas.removeAll();
        if (ventas.isEmpty()) {
            JLabel vacio = new JLabel("No hay ventas registradas aún.");
            vacio.setFont(new Font("SansSerif", Font.PLAIN, 14));
            vacio.setForeground(TEXTO_GRAY);
            vacio.setBorder(new EmptyBorder(40, 0, 0, 0));
            panelTarjetas.add(vacio);
        } else {
            for (Venta v : ventas) panelTarjetas.add(crearTarjeta(v));
        }
        lblNumVentas.setText(String.valueOf(ventaDAO.numeroVentas()));
        lblTotal.setText(String.format("%.2f €", ventaDAO.totalFacturado()));
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearTarjeta(Venta venta) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(300, 250));
        card.setBorder(new PanelCoches.RoundedBorder(12, BORDER_COLOR));

        // Header de la tarjeta
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(245, 245, 245));
                g.fillRoundRect(0, 0, getWidth(), getHeight() + 12, 12, 12);
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 14, 16));
        header.setPreferredSize(new Dimension(300, 70));

        JLabel lblVehiculo = new JLabel("🚗  " + venta.getCoche().getMarca() + " " + venta.getCoche().getModelo());
        lblVehiculo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblVehiculo.setForeground(TEXTO_DARK);

        JLabel lblId = new JLabel("Venta #" + venta.getId());
        lblId.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblId.setForeground(TEXTO_GRAY);

        header.add(lblVehiculo, BorderLayout.CENTER);
        header.add(lblId,       BorderLayout.EAST);

        // Detalles
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(12, 16, 10, 16));

        info.add(fila("👤 Cliente",   venta.getCliente().getNombre() + " " + venta.getCliente().getApellidos()));
        info.add(Box.createVerticalStrut(6));
        info.add(fila("📅 Fecha",     venta.getFechaVenta().toString()));
        info.add(Box.createVerticalStrut(6));
        info.add(fila("💳 Pago",      venta.getFormaPago()));
        info.add(Box.createVerticalStrut(10));

        JLabel lblPrecio = new JLabel(String.format("%.2f €", venta.getPrecioFinal()));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblPrecio.setForeground(VERDE);
        info.add(lblPrecio);

        // Botón anular
        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        bots.setOpaque(false);
        JButton btnAnular = new JButton("✖ Anular venta");
        btnAnular.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnAnular.setBackground(Color.WHITE);
        btnAnular.setForeground(new Color(180, 0, 0));
        btnAnular.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 180, 180)),
            new EmptyBorder(5, 10, 5, 10)));
        btnAnular.setFocusPainted(false);
        btnAnular.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAnular.addActionListener(e -> anularVenta(venta));
        bots.add(btnAnular);

        card.add(header, BorderLayout.NORTH);
        card.add(info,   BorderLayout.CENTER);
        card.add(bots,   BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { card.setBorder(new PanelCoches.RoundedBorder(12, ACENTO)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { card.setBorder(new PanelCoches.RoundedBorder(12, BORDER_COLOR)); }
        });

        return card;
    }

    private JPanel fila(String label, String valor) {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(TEXTO_GRAY);
        l.setPreferredSize(new Dimension(80, 16));
        JLabel v = new JLabel(valor);
        v.setFont(new Font("SansSerif", Font.PLAIN, 12));
        v.setForeground(TEXTO_DARK);
        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.CENTER);
        return p;
    }

    private void abrirFormVenta() {
        List<Cliente> clientes    = clienteDAO.listarTodos();
        List<Coche>   disponibles = cocheDAO.listarDisponibles();
        if (clientes.isEmpty())    { JOptionPane.showMessageDialog(this, "No hay clientes registrados."); return; }
        if (disponibles.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay vehiculos disponibles."); return; }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Venta", true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(Color.WHITE);
        dialog.setLayout(new BorderLayout());

        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(TEXTO_DARK);
        head.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel dTitulo = new JLabel("💰  Registrar Nueva Venta");
        dTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        dTitulo.setForeground(Color.WHITE);
        head.add(dTitulo);
        dialog.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Cliente> cbCliente = new JComboBox<>(clientes.toArray(new Cliente[0]));
        JComboBox<Coche>   cbCoche   = new JComboBox<>(disponibles.toArray(new Coche[0]));
        JTextField txtPrecio         = new JTextField();
        JComboBox<String>  cbPago    = new JComboBox<>(new String[]{"Contado", "Financiado", "Leasing"});
        JTextField txtObs            = new JTextField();

        cbCoche.addActionListener(e -> {
            Coche c = (Coche) cbCoche.getSelectedItem();
            if (c != null) txtPrecio.setText(String.format("%.2f", c.getPrecio()));
        });
        if (!disponibles.isEmpty()) txtPrecio.setText(String.format("%.2f", disponibles.get(0).getPrecio()));

        String[] labels = {"Cliente:", "Vehiculo:", "Precio Final (EUR):", "Forma de pago:", "Observaciones:"};
        Component[] fields = {cbCliente, cbCoche, txtPrecio, cbPago, txtObs};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel l = new JLabel(labels[i]);
            l.setFont(new Font("SansSerif", Font.PLAIN, 13));
            l.setForeground(TEXTO_GRAY);
            form.add(l, gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(fields[i], gbc);
        }
        dialog.add(form, BorderLayout.CENTER);

        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        bots.setBackground(Color.WHITE);
        JButton btnG = btnPrimario("Registrar venta");
        JButton btnC = new JButton("Cancelar");
        btnC.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnC.setFocusPainted(false);
        bots.add(btnC); bots.add(btnG);
        dialog.add(bots, BorderLayout.SOUTH);

        btnC.addActionListener(e -> dialog.dispose());
        btnG.addActionListener(e -> {
            try {
                double precio = Double.parseDouble(txtPrecio.getText().replace(",", "."));
                Venta v = new Venta(0, (Cliente) cbCliente.getSelectedItem(),
                        (Coche) cbCoche.getSelectedItem(), LocalDate.now(),
                        precio, (String) cbPago.getSelectedItem(), txtObs.getText());
                ventaDAO.agregar(v);
                cargarTarjetas(ventaDAO.listarTodas());
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "El precio no es valido.");
            }
        });
        dialog.setVisible(true);
    }

    private void anularVenta(Venta v) {
        if (JOptionPane.showConfirmDialog(this,
                "¿Anular la venta del " + v.getCoche().getMarca() + " " + v.getCoche().getModelo() + "?\nEl vehiculo volvera a estar disponible.",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            ventaDAO.eliminar(v.getId());
            cargarTarjetas(ventaDAO.listarTodas());
        }
    }

    public void refrescar() { cargarTarjetas(ventaDAO.listarTodas()); }

    private JButton btnPrimario(String t) {
        JButton b = new JButton(t);
        b.setBackground(TEXTO_DARK); b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }
}
