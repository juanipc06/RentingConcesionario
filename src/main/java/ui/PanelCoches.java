package ui;

import dao.CocheDAO;
import dao.ClienteDAO;
import dao.VentaDAO;
import model.Coche;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class PanelCoches extends JPanel {

    private CocheDAO cocheDAO;
    private ClienteDAO clienteDAO;
    private VentaDAO ventaDAO;

    private JPanel panelTarjetas;
    private JTextField txtBuscar;
    private JComboBox<String> cbFiltroEstado;

    // Colores estilo Mercedes (claro/premium)
    private static final Color BG           = new Color(248, 248, 248);
    private static final Color CARD_BG      = Color.WHITE;
    private static final Color ACENTO       = new Color(0, 100, 180);
    private static final Color ACENTO_RED   = new Color(180, 0, 0);
    private static final Color TEXTO_DARK   = new Color(20, 20, 20);
    private static final Color TEXTO_GRAY   = new Color(100, 100, 100);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);

    public PanelCoches(CocheDAO cocheDAO, ClienteDAO clienteDAO, VentaDAO ventaDAO) {
        this.cocheDAO   = cocheDAO;
        this.clienteDAO = clienteDAO;
        this.ventaDAO   = ventaDAO;
        inicializarUI();
        cargarTarjetas(cocheDAO.listarTodos());
    }

    private void inicializarUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // ── Top bar ──────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(18, 30, 18, 30)
        ));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);
        JLabel titulo    = new JLabel("Catálogo de Vehículos");
        titulo.setFont(new Font("SansSerif", Font.PLAIN, 26));
        titulo.setForeground(TEXTO_DARK);
        JLabel subtitulo = new JLabel("Gestiona y configura el inventario");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_GRAY);
        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(3));
        tituloPanel.add(subtitulo);
        topBar.add(tituloPanel, BorderLayout.WEST);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnPrimario("+ Nuevo vehículo"));
        topBar.add(acciones, BorderLayout.EAST);

        ((JButton) acciones.getComponent(0)).addActionListener(e -> abrirFormNuevo());
        add(topBar, BorderLayout.NORTH);

        // ── Filtros ───────────────────────────────
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        filtros.setBackground(BG);
        filtros.setBorder(new EmptyBorder(0, 20, 0, 20));

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(7, 12, 7, 12)));
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar marca, modelo...");

        cbFiltroEstado = new JComboBox<>(new String[]{"Todos los estados", "Disponible", "Reservado", "Vendido"});
        cbFiltroEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbFiltroEstado.setBackground(Color.WHITE);
        cbFiltroEstado.setPreferredSize(new Dimension(160, 36));

        JButton btnFiltrar = btnSecundario("Filtrar");
        btnFiltrar.addActionListener(e -> filtrar());
        txtBuscar.addActionListener(e -> filtrar());

        filtros.add(new JLabel("Buscar:"));
        filtros.add(txtBuscar);
        filtros.add(cbFiltroEstado);
        filtros.add(btnFiltrar);

        // ── Panel de tarjetas con scroll ──────────
        panelTarjetas = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        panelTarjetas.setBackground(BG);
        panelTarjetas.setBorder(new EmptyBorder(10, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(panelTarjetas);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(BG);
        centro.add(filtros,  BorderLayout.NORTH);
        centro.add(scroll,   BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private void cargarTarjetas(List<Coche> coches) {
        panelTarjetas.removeAll();
        for (Coche c : coches) {
            panelTarjetas.add(crearTarjeta(c));
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearTarjeta(Coche coche) {
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
        card.setPreferredSize(new Dimension(280, 360));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(12, BORDER_COLOR),
            new EmptyBorder(0, 0, 0, 0)
        ));

        // Badge estado
        JPanel imagenPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Fondo gris claro para la imagen
                g2.setColor(new Color(240, 240, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                // Icono de coche grande
                g2.setColor(new Color(180, 180, 180));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 64));
                FontMetrics fm = g2.getFontMetrics();
                String ico = "🚗";
                g2.drawString(ico, (getWidth() - fm.stringWidth(ico)) / 2, getHeight() / 2 + 20);
                g2.dispose();
            }
        };
        imagenPanel.setOpaque(false);
        imagenPanel.setPreferredSize(new Dimension(280, 170));

        // Badge de estado
        JLabel badge = new JLabel(" " + coche.getEstado() + " ");
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        if ("Disponible".equals(coche.getEstado()))      badge.setBackground(new Color(34, 139, 34));
        else if ("Reservado".equals(coche.getEstado()))  badge.setBackground(new Color(200, 140, 0));
        else                                             badge.setBackground(new Color(160, 0, 0));
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));

        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        badgePanel.setOpaque(false);
        badgePanel.add(badge);
        imagenPanel.add(badgePanel, BorderLayout.NORTH);

        // Info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel lblMarca = new JLabel(coche.getMarca().toUpperCase());
        lblMarca.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblMarca.setForeground(TEXTO_GRAY);

        JLabel lblModelo = new JLabel(coche.getModelo());
        lblModelo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblModelo.setForeground(TEXTO_DARK);

        JLabel lblAnio = new JLabel(coche.getAnio() + "  ·  " + coche.getPotenciaCV() + " CV  ·  " + coche.getTipoCombustible());
        lblAnio.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblAnio.setForeground(TEXTO_GRAY);

        JLabel lblPrecio = new JLabel(String.format("A partir de %.2f €", coche.getPrecio()));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblPrecio.setForeground(TEXTO_DARK);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        info.add(lblMarca);
        info.add(Box.createVerticalStrut(4));
        info.add(lblModelo);
        info.add(Box.createVerticalStrut(4));
        info.add(lblAnio);
        info.add(Box.createVerticalStrut(10));
        info.add(sep);
        info.add(Box.createVerticalStrut(10));
        info.add(lblPrecio);

        // Botones
        JPanel bots = new JPanel(new GridLayout(1, 3, 6, 0));
        bots.setOpaque(false);
        bots.setBorder(new EmptyBorder(0, 16, 14, 16));

        JButton btnEditar  = btnIcono("✏", "Editar");
        JButton btnPerson  = btnIcono("🎨", "Personalizar");
        JButton btnElim    = btnIconoRojo("🗑", "Eliminar");

        bots.add(btnEditar);
        bots.add(btnPerson);
        bots.add(btnElim);

        btnEditar.addActionListener(e -> abrirFormEditar(coche));
        btnPerson.addActionListener(e -> abrirPersonalizar(coche));
        btnElim.addActionListener(e   -> eliminarCoche(coche));

        card.add(imagenPanel, BorderLayout.NORTH);
        card.add(info,        BorderLayout.CENTER);
        card.add(bots,        BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(12, ACENTO),
                    new EmptyBorder(0, 0, 0, 0)));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(12, BORDER_COLOR),
                    new EmptyBorder(0, 0, 0, 0)));
            }
        });

        return card;
    }

    private void filtrar() {
        String txt    = txtBuscar.getText().toLowerCase();
        String estado = (String) cbFiltroEstado.getSelectedItem();
        List<Coche> todos = cocheDAO.listarTodos();
        todos.removeIf(c -> {
            boolean txtOk = c.getMarca().toLowerCase().contains(txt) || c.getModelo().toLowerCase().contains(txt);
            boolean estOk = "Todos los estados".equals(estado) || c.getEstado().equals(estado);
            return !txtOk || !estOk;
        });
        cargarTarjetas(todos);
    }

    private void abrirFormNuevo() {
        DialogFormCoche d = new DialogFormCoche((Frame) SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (d.getCoche() != null) { cocheDAO.agregar(d.getCoche()); cargarTarjetas(cocheDAO.listarTodos()); }
    }

    private void abrirFormEditar(Coche c) {
        DialogFormCoche d = new DialogFormCoche((Frame) SwingUtilities.getWindowAncestor(this), c);
        d.setVisible(true);
        if (d.getCoche() != null) { cocheDAO.actualizar(d.getCoche()); cargarTarjetas(cocheDAO.listarTodos()); }
    }

    private void abrirPersonalizar(Coche c) {
        DialogPersonalizarCoche d = new DialogPersonalizarCoche((Frame) SwingUtilities.getWindowAncestor(this), c);
        d.setVisible(true);
        cocheDAO.actualizar(c);
        cargarTarjetas(cocheDAO.listarTodos());
    }

    private void eliminarCoche(Coche c) {
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar " + c.getMarca() + " " + c.getModelo() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            cocheDAO.eliminar(c.getId());
            cargarTarjetas(cocheDAO.listarTodos());
        }
    }

    public void refrescar() { cargarTarjetas(cocheDAO.listarTodos()); }

    // ── Helpers de botones ────────────────────────
    private JButton btnPrimario(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(TEXTO_DARK);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        return b;
    }

    private JButton btnSecundario(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(Color.WHITE);
        b.setForeground(TEXTO_DARK);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(7, 16, 7, 16)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton btnIcono(String ico, String tooltip) {
        JButton b = new JButton(ico + " " + tooltip);
        b.setFont(new Font("SansSerif", Font.PLAIN, 11));
        b.setBackground(Color.WHITE);
        b.setForeground(TEXTO_DARK);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(5, 6, 5, 6)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton btnIconoRojo(String ico, String tooltip) {
        JButton b = btnIcono(ico, tooltip);
        b.setForeground(new Color(180, 0, 0));
        return b;
    }

    // ── WrapLayout para las tarjetas ─────────────
    static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }
        @Override public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }
        @Override public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }
        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - insets.left - insets.right - hgap * 2;
                int x = 0, y = insets.top + vgap, rowH = 0;
                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component c = target.getComponent(i);
                    if (!c.isVisible()) continue;
                    Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
                    if (x + d.width > maxWidth && x > 0) { y += rowH + vgap; x = 0; rowH = 0; }
                    x += d.width + hgap;
                    rowH = Math.max(rowH, d.height);
                }
                y += rowH + vgap + insets.bottom;
                return new Dimension(targetWidth, y);
            }
        }
    }

    // ── Borde redondeado ──────────────────────────
    static class RoundedBorder extends AbstractBorder {
        private int radius; private Color color;
        public RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w-1, h-1, radius, radius);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(radius/2, radius/2, radius/2, radius/2); }
    }
}
