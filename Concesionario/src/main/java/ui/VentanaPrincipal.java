package ui;

import dao.CocheDAO;
import dao.ClienteDAO;
import dao.VentaDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private CocheDAO cocheDAO;
    private ClienteDAO clienteDAO;
    private VentaDAO ventaDAO;

    private JPanel panelContenido;
    private CardLayout cardLayout;

    private PanelCoches panelCoches;
    private PanelClientes panelClientes;
    private PanelVentas panelVentas;

    public static final Color COLOR_PRIMARIO     = new Color(18, 18, 22);
    public static final Color COLOR_SECUNDARIO   = new Color(28, 28, 35);
    public static final Color COLOR_SUPERFICIE   = new Color(38, 38, 48);
    public static final Color COLOR_ACENTO       = new Color(220, 38, 38);
    public static final Color COLOR_ACENTO_HOVER = new Color(185, 28, 28);
    public static final Color COLOR_TEXTO        = new Color(240, 240, 245);
    public static final Color COLOR_TEXTO_SUAVE  = new Color(140, 140, 160);
    public static final Color COLOR_FONDO        = new Color(22, 22, 28);
    public static final Color COLOR_BORDE        = new Color(50, 50, 65);

    public VentanaPrincipal() {
        cocheDAO   = new CocheDAO();
        clienteDAO = new ClienteDAO();
        ventaDAO   = new VentaDAO();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Bayarri`s Car — Concesionario Premium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COLOR_FONDO);

        add(crearTopBar(), BorderLayout.NORTH);

        cardLayout     = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(COLOR_FONDO);

        panelCoches   = new PanelCoches(cocheDAO, clienteDAO, ventaDAO);
        panelClientes = new PanelClientes(clienteDAO);
        panelVentas   = new PanelVentas(ventaDAO, cocheDAO, clienteDAO);

        panelContenido.add(panelCoches,   "COCHES");
        panelContenido.add(panelClientes, "CLIENTES");
        panelContenido.add(panelVentas,   "VENTAS");

        add(panelContenido, BorderLayout.CENTER);
        cardLayout.show(panelContenido, "COCHES");
    }

    private JPanel crearTopBar() {
        JPanel topBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(COLOR_PRIMARIO);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(COLOR_ACENTO);
                g.fillRect(0, getHeight() - 3, getWidth(), 3);
            }
        };
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBorder(new EmptyBorder(0, 30, 0, 30));
        topBar.setOpaque(false);

        // Logo
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelLogo.setOpaque(false);
        JLabel ico  = new JLabel("◈ ");
        ico.setFont(new Font("SansSerif", Font.BOLD, 26));
        ico.setForeground(COLOR_ACENTO);
        JLabel auto = new JLabel("AUTO");
        auto.setFont(new Font("SansSerif", Font.BOLD, 22));
        auto.setForeground(COLOR_TEXTO);
        JLabel elite = new JLabel("ELITE");
        elite.setFont(new Font("SansSerif", Font.BOLD, 22));
        elite.setForeground(COLOR_ACENTO);
        panelLogo.add(ico); panelLogo.add(auto); panelLogo.add(elite);

        // Nav
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        nav.setOpaque(false);
        JButton btnCoches   = crearBotonNav("🚘  VEHÍCULOS");
        JButton btnClientes = crearBotonNav("👤  CLIENTES");
        JButton btnVentas   = crearBotonNav("💰  VENTAS");
        activarBtn(btnCoches);
        nav.add(btnCoches); nav.add(btnClientes); nav.add(btnVentas);

        // Der
        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        der.setOpaque(false);
        JLabel ver = new JLabel("v1.0  ");
        ver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ver.setForeground(COLOR_TEXTO_SUAVE);
        der.add(ver);

        topBar.add(panelLogo, BorderLayout.WEST);
        topBar.add(nav,       BorderLayout.CENTER);
        topBar.add(der,       BorderLayout.EAST);

        btnCoches.addActionListener(e -> { cardLayout.show(panelContenido, "COCHES");   cambiarActivo(btnCoches, btnClientes, btnVentas); });
        btnClientes.addActionListener(e -> { cardLayout.show(panelContenido, "CLIENTES"); panelClientes.refrescar(); cambiarActivo(btnClientes, btnCoches, btnVentas); });
        btnVentas.addActionListener(e -> { cardLayout.show(panelContenido, "VENTAS");   panelVentas.refrescar();   cambiarActivo(btnVentas, btnCoches, btnClientes); });

        return topBar;
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(COLOR_TEXTO_SUAVE);
        btn.setBackground(new Color(0,0,0,0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!btn.getForeground().equals(Color.WHITE)) btn.setForeground(COLOR_TEXTO);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getForeground().equals(Color.WHITE)) btn.setForeground(COLOR_TEXTO_SUAVE);
            }
        });
        return btn;
    }

    private void activarBtn(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_ACENTO),
            new EmptyBorder(10, 18, 8, 18)
        ));
    }

    private void desactivarBtn(JButton btn) {
        btn.setForeground(COLOR_TEXTO_SUAVE);
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));
    }

    private void cambiarActivo(JButton activo, JButton... resto) {
        activarBtn(activo);
        for (JButton b : resto) desactivarBtn(b);
    }

    public CocheDAO getCocheDAO()     { return cocheDAO; }
    public ClienteDAO getClienteDAO() { return clienteDAO; }
    public VentaDAO getVentaDAO()     { return ventaDAO; }
}
