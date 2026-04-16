package ui;

import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.List;

public class PanelClientes extends JPanel {

    private ClienteDAO clienteDAO;
    private JPanel panelTarjetas;
    private JTextField txtBuscar;

    private static final Color BG           = new Color(248, 248, 248);
    private static final Color CARD_BG      = Color.WHITE;
    private static final Color TEXTO_DARK   = new Color(20, 20, 20);
    private static final Color TEXTO_GRAY   = new Color(100, 100, 100);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color ACENTO       = new Color(0, 100, 180);

    public PanelClientes(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
        inicializarUI();
        cargarTarjetas(clienteDAO.listarTodos());
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
        JLabel titulo    = new JLabel("Clientes");
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 26));
        titulo.setForeground(TEXTO_DARK);
        JLabel subtitulo = new JLabel("Base de datos de clientes registrados");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_GRAY);
        tituloPanel.add(titulo); tituloPanel.add(Box.createVerticalStrut(3)); tituloPanel.add(subtitulo);
        topBar.add(tituloPanel, BorderLayout.WEST);

        JButton btnNuevo = btnPrimario("+ Nuevo cliente");
        btnNuevo.addActionListener(e -> abrirFormNuevo());
        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acc.setOpaque(false); acc.add(btnNuevo);
        topBar.add(acc, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Filtros
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        filtros.setBackground(BG);
        filtros.setBorder(new EmptyBorder(0, 20, 0, 20));
        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), new EmptyBorder(7, 12, 7, 12)));
        JButton btnBuscar = btnSecundario("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        txtBuscar.addActionListener(e -> buscar());
        filtros.add(new JLabel("Buscar por nombre o DNI:")); filtros.add(txtBuscar); filtros.add(btnBuscar);

        // Panel tarjetas
        panelTarjetas = new JPanel(new PanelCoches.WrapLayout(FlowLayout.LEFT, 20, 20));
        panelTarjetas.setBackground(BG);
        panelTarjetas.setBorder(new EmptyBorder(10, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(panelTarjetas);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(BG);
        centro.add(filtros, BorderLayout.NORTH);
        centro.add(scroll,  BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private void cargarTarjetas(List<Cliente> clientes) {
        panelTarjetas.removeAll();
        for (Cliente c : clientes) panelTarjetas.add(crearTarjeta(c));
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearTarjeta(Cliente cliente) {
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
        card.setPreferredSize(new Dimension(280, 300));
        card.setBorder(new PanelCoches.RoundedBorder(12, BORDER_COLOR));

        // Avatar
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 235, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // Inicial
                String ini = String.valueOf(cliente.getNombre().charAt(0)).toUpperCase();
                g2.setColor(ACENTO);
                g2.setFont(new Font("SansSerif", Font.BOLD, 52));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(ini, (getWidth() - fm.stringWidth(ini)) / 2, getHeight() / 2 + fm.getAscent() / 2 - 5);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(280, 120));

        // Info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(14, 16, 10, 16));

        JLabel lblNombre = new JLabel(cliente.getNombre() + " " + cliente.getApellidos());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblNombre.setForeground(TEXTO_DARK);

        JLabel lblDni = new JLabel("DNI: " + cliente.getDni());
        lblDni.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDni.setForeground(TEXTO_GRAY);

        JLabel lblTel = new JLabel("📞 " + cliente.getTelefono());
        lblTel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTel.setForeground(TEXTO_GRAY);

        JLabel lblEmail = new JLabel("✉ " + cliente.getEmail());
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEmail.setForeground(TEXTO_GRAY);

        info.add(lblNombre); info.add(Box.createVerticalStrut(6));
        info.add(lblDni);    info.add(Box.createVerticalStrut(3));
        info.add(lblTel);    info.add(Box.createVerticalStrut(3));
        info.add(lblEmail);

        // Botones
        JPanel bots = new JPanel(new GridLayout(1, 2, 6, 0));
        bots.setOpaque(false);
        bots.setBorder(new EmptyBorder(0, 16, 14, 16));

        JButton btnEditar  = btnIcono("✏ Editar");
        JButton btnEliminar= btnIconoRojo("🗑 Eliminar");
        bots.add(btnEditar); bots.add(btnEliminar);

        btnEditar.addActionListener(e   -> abrirFormEditar(cliente));
        btnEliminar.addActionListener(e -> eliminarCliente(cliente));

        card.add(avatar, BorderLayout.NORTH);
        card.add(info,   BorderLayout.CENTER);
        card.add(bots,   BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { card.setBorder(new PanelCoches.RoundedBorder(12, ACENTO)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { card.setBorder(new PanelCoches.RoundedBorder(12, BORDER_COLOR)); }
        });
        return card;
    }

    private void buscar() {
        String txt = txtBuscar.getText().trim();
        if (txt.isEmpty()) { cargarTarjetas(clienteDAO.listarTodos()); return; }
        Cliente porDni = clienteDAO.buscarPorDni(txt);
        if (porDni != null) cargarTarjetas(Arrays.asList(porDni));
        else                cargarTarjetas(clienteDAO.buscarPorNombre(txt));
    }

    private void abrirFormNuevo() {
        DialogFormCliente d = new DialogFormCliente((Frame) SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (d.getCliente() != null) { clienteDAO.agregar(d.getCliente()); cargarTarjetas(clienteDAO.listarTodos()); }
    }

    private void abrirFormEditar(Cliente c) {
        DialogFormCliente d = new DialogFormCliente((Frame) SwingUtilities.getWindowAncestor(this), c);
        d.setVisible(true);
        if (d.getCliente() != null) { clienteDAO.actualizar(d.getCliente()); cargarTarjetas(clienteDAO.listarTodos()); }
    }

    private void eliminarCliente(Cliente c) {
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar a " + c.getNombre() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clienteDAO.eliminar(c.getId());
            cargarTarjetas(clienteDAO.listarTodos());
        }
    }

    public void refrescar() { cargarTarjetas(clienteDAO.listarTodos()); }

    private JButton btnPrimario(String t) {
        JButton b = new JButton(t);
        b.setBackground(TEXTO_DARK); b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }
    private JButton btnSecundario(String t) {
        JButton b = new JButton(t);
        b.setBackground(Color.WHITE); b.setForeground(TEXTO_DARK);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR), new EmptyBorder(7,16,7,16)));
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private JButton btnIcono(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("SansSerif", Font.PLAIN, 11));
        b.setBackground(Color.WHITE); b.setForeground(TEXTO_DARK);
        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR), new EmptyBorder(5,6,5,6)));
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
    private JButton btnIconoRojo(String t) {
        JButton b = btnIcono(t);
        b.setForeground(new Color(180, 0, 0));
        return b;
    }
}
