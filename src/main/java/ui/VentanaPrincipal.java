package ui;

import dao.CocheDAO;
import dao.ClienteDAO;
import dao.VentaDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
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

    // Paleta clara estilo Mercedes
    public static final Color COLOR_PRIMARIO     = new Color(20, 20, 20);
    public static final Color COLOR_SECUNDARIO   = new Color(245, 245, 245);
    public static final Color COLOR_SUPERFICIE   = new Color(255, 255, 255);
    public static final Color COLOR_ACENTO       = new Color(0, 100, 180);
    public static final Color COLOR_ACENTO_HOVER = new Color(0, 80, 150);
    public static final Color COLOR_TEXTO        = new Color(20, 20, 20);
    public static final Color COLOR_TEXTO_SUAVE  = new Color(100, 100, 100);
    public static final Color COLOR_FONDO        = new Color(248, 248, 248);
    public static final Color COLOR_BORDE        = new Color(220, 220, 220);

    public VentanaPrincipal() {
        cocheDAO   = new CocheDAO();
        clienteDAO = new ClienteDAO();
        ventaDAO   = new VentaDAO();
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Bayarry's Car — Concesionario Premium");
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
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, COLOR_BORDE),
            new EmptyBorder(0, 30, 0, 30)
        ));
        topBar.setPreferredSize(new Dimension(0, 65));

        // Logo
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelLogo.setOpaque(false);
        JLabel ico   = new JLabel("◈ ");
        ico.setFont(new Font("SansSerif", Font.BOLD, 22));
        ico.setForeground(new Color(180, 0, 0));
        JLabel nombre = new JLabel("BAYARRY'S ");
        nombre.setFont(new Font("SansSerif", Font.BOLD, 20));
        nombre.setForeground(COLOR_PRIMARIO);
        JLabel car = new JLabel("CAR");
        car.setFont(new Font("SansSerif", Font.BOLD, 20));
        car.setForeground(new Color(180, 0, 0));
        panelLogo.add(ico); panelLogo.add(nombre); panelLogo.add(car);

        // Nav central
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nav.setOpaque(false);

        JButton btnCoches   = crearBotonNav("🚗  Vehículos");
        JButton btnClientes = crearBotonNav("👤  Clientes");
        JButton btnVentas   = crearBotonNav("💰  Ventas");
        activarBtn(btnCoches);

        nav.add(btnCoches); nav.add(btnClientes); nav.add(btnVentas);

        // Der
        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        der.setOpaque(false);
        JLabel ver = new JLabel("v1.0");
        ver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ver.setForeground(COLOR_TEXTO_SUAVE);
        der.add(ver);

        topBar.add(panelLogo, BorderLayout.WEST);
        topBar.add(nav,       BorderLayout.CENTER);
        topBar.add(der,       BorderLayout.EAST);

        btnCoches.addActionListener(e   -> { cardLayout.show(panelContenido, "COCHES");   cambiarActivo(btnCoches, btnClientes, btnVentas); });
        btnClientes.addActionListener(e -> { cardLayout.show(panelContenido, "CLIENTES"); panelClientes.refrescar(); cambiarActivo(btnClientes, btnCoches, btnVentas); });
        btnVentas.addActionListener(e   -> { cardLayout.show(panelContenido, "VENTAS");   panelVentas.refrescar();   cambiarActivo(btnVentas, btnCoches, btnClientes); });

        return topBar;
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(COLOR_TEXTO_SUAVE);
        btn.setBackground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(20, 22, 20, 22));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!btn.getForeground().equals(COLOR_PRIMARIO))
                    btn.setForeground(new Color(60, 60, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getForeground().equals(COLOR_PRIMARIO))
                    btn.setForeground(COLOR_TEXTO_SUAVE);
            }
        });
        return btn;
    }

    private void activarBtn(JButton btn) {
        btn.setForeground(COLOR_PRIMARIO);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, COLOR_PRIMARIO),
            new EmptyBorder(20, 22, 18, 22)
        ));
    }

    private void desactivarBtn(JButton btn) {
        btn.setForeground(COLOR_TEXTO_SUAVE);
        btn.setBorder(new EmptyBorder(20, 22, 20, 22));
    }

    private void cambiarActivo(JButton activo, JButton... resto) {
        activarBtn(activo);
        for (JButton b : resto) desactivarBtn(b);
    }

    public CocheDAO getCocheDAO()     { return cocheDAO; }
    public ClienteDAO getClienteDAO() { return clienteDAO; }
    public VentaDAO getVentaDAO()     { return ventaDAO; }
}
