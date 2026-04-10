package ui;

import dao.CocheDAO;
import dao.ClienteDAO;
import dao.VentaDAO;
import model.Coche;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class PanelCoches extends JPanel {

    private CocheDAO cocheDAO;
    private ClienteDAO clienteDAO;
    private VentaDAO ventaDAO;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cbFiltroEstado;

    public PanelCoches(CocheDAO cocheDAO, ClienteDAO clienteDAO, VentaDAO ventaDAO) {
        this.cocheDAO   = cocheDAO;
        this.clienteDAO = clienteDAO;
        this.ventaDAO   = ventaDAO;
        inicializarUI();
        cargarTabla(cocheDAO.listarTodos());
    }

    private void inicializarUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(VentanaPrincipal.COLOR_FONDO);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // ── Cabecera ──────────────────────────────
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);

        JLabel titulo = new JLabel("CATÁLOGO DE VEHÍCULOS");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(VentanaPrincipal.COLOR_TEXTO);

        JLabel subtitulo = new JLabel("Gestiona el inventario del concesionario");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(4));
        tituloPanel.add(subtitulo);
        cabecera.add(tituloPanel, BorderLayout.WEST);

        // Botones acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);

        JButton btnNuevo        = crearBoton("+ Nuevo",      VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        JButton btnEditar       = crearBoton("✏ Editar",     VentanaPrincipal.COLOR_SUPERFICIE, VentanaPrincipal.COLOR_TEXTO);
        JButton btnPersonalizar = crearBoton("🎨 Personalizar", new Color(88, 28, 135), Color.WHITE);
        JButton btnEliminar     = crearBoton("🗑 Eliminar",   new Color(40, 40, 50), VentanaPrincipal.COLOR_TEXTO_SUAVE);

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnPersonalizar);
        panelBotones.add(btnEliminar);
        cabecera.add(panelBotones, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        // ── Filtros ───────────────────────────────
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(new EmptyBorder(0, 0, 15, 0));

        txtBuscar = estilizarTextField(new JTextField(22));
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar marca o modelo...");

        cbFiltroEstado = new JComboBox<>(new String[]{"Todos", "Disponible", "Reservado", "Vendido"});
        estilizarCombo(cbFiltroEstado);

        JButton btnFiltrar = crearBoton("Filtrar", VentanaPrincipal.COLOR_SECUNDARIO, VentanaPrincipal.COLOR_TEXTO);

        JLabel lbBuscar = label("Buscar:");
        JLabel lbEstado = label("Estado:");

        panelFiltros.add(lbBuscar);
        panelFiltros.add(txtBuscar);
        panelFiltros.add(lbEstado);
        panelFiltros.add(cbFiltroEstado);
        panelFiltros.add(btnFiltrar);

        // ── Tabla ─────────────────────────────────
        String[] columnas = {"ID", "Marca", "Modelo", "Año", "Color", "Combustible", "Transmisión", "CV", "Precio (€)", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        // Renderer con colores por estado
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                String estado = (String) modeloTabla.getValueAt(row, 9);
                if (!sel) {
                    if (row % 2 == 0) c.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
                    else              c.setBackground(VentanaPrincipal.COLOR_PRIMARIO);
                    c.setForeground(VentanaPrincipal.COLOR_TEXTO);

                    if (col == 9) {
                        if ("Vendido".equals(estado))    { c.setBackground(new Color(60, 20, 20)); c.setForeground(new Color(252, 129, 129)); }
                        else if ("Reservado".equals(estado)) { c.setBackground(new Color(60, 50, 10)); c.setForeground(new Color(250, 204, 21)); }
                        else                             { c.setBackground(new Color(20, 50, 30)); c.setForeground(new Color(74, 222, 128)); }
                    }
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
        scroll.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);

        // ── Panel sur (filtros + tabla juntos) ────
        JPanel sur = new JPanel(new BorderLayout());
        sur.setOpaque(false);
        sur.add(panelFiltros, BorderLayout.NORTH);
        sur.add(scroll,       BorderLayout.CENTER);
        add(sur, BorderLayout.CENTER);

        // Acciones
        btnNuevo.addActionListener(e        -> abrirFormNuevo());
        btnEditar.addActionListener(e       -> abrirFormEditar());
        btnPersonalizar.addActionListener(e -> abrirPersonalizar());
        btnEliminar.addActionListener(e     -> eliminarCoche());
        btnFiltrar.addActionListener(e      -> filtrar());
    }

    private void cargarTabla(List<Coche> coches) {
        modeloTabla.setRowCount(0);
        for (Coche c : coches) {
            modeloTabla.addRow(new Object[]{
                c.getId(), c.getMarca(), c.getModelo(), c.getAnio(),
                c.getColor(), c.getTipoCombustible(), c.getTransmision(),
                c.getPotenciaCV(), String.format("%.2f", c.getPrecio()), c.getEstado()
            });
        }
    }

    private void filtrar() {
        String texto  = txtBuscar.getText().toLowerCase();
        String estado = (String) cbFiltroEstado.getSelectedItem();
        List<Coche> todos = cocheDAO.listarTodos();
        todos.removeIf(c -> {
            boolean ok   = c.getMarca().toLowerCase().contains(texto) || c.getModelo().toLowerCase().contains(texto);
            boolean estOk = "Todos".equals(estado) || c.getEstado().equals(estado);
            return !ok || !estOk;
        });
        cargarTabla(todos);
    }

    private void abrirFormNuevo() {
        DialogFormCoche d = new DialogFormCoche((Frame) SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (d.getCoche() != null) { cocheDAO.agregar(d.getCoche()); cargarTabla(cocheDAO.listarTodos()); }
    }

    private void abrirFormEditar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { msg("Selecciona un coche para editar."); return; }
        Coche c = cocheDAO.buscarPorId((int) modeloTabla.getValueAt(fila, 0));
        DialogFormCoche d = new DialogFormCoche((Frame) SwingUtilities.getWindowAncestor(this), c);
        d.setVisible(true);
        if (d.getCoche() != null) { cocheDAO.actualizar(d.getCoche()); cargarTabla(cocheDAO.listarTodos()); }
    }

    private void abrirPersonalizar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { msg("Selecciona un coche para personalizar."); return; }
        Coche c = cocheDAO.buscarPorId((int) modeloTabla.getValueAt(fila, 0));
        DialogPersonalizarCoche d = new DialogPersonalizarCoche((Frame) SwingUtilities.getWindowAncestor(this), c);
        d.setVisible(true);
        cocheDAO.actualizar(c);
        cargarTabla(cocheDAO.listarTodos());
    }

    private void eliminarCoche() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { msg("Selecciona un coche para eliminar."); return; }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar este vehículo?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            cocheDAO.eliminar(id);
            cargarTabla(cocheDAO.listarTodos());
        }
    }

    public void refrescar() { cargarTabla(cocheDAO.listarTodos()); }

    // ── Helpers de estilo ─────────────────────────
    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 16, 9, 16));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = bg;
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(original.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(original); }
        });
        return btn;
    }

    private JTextField estilizarTextField(JTextField tf) {
        tf.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        tf.setForeground(VentanaPrincipal.COLOR_TEXTO);
        tf.setCaretColor(VentanaPrincipal.COLOR_TEXTO);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE),
            new EmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    private void estilizarCombo(JComboBox<String> cb) {
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

    private JLabel label(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return l;
    }

    private void msg(String m) { JOptionPane.showMessageDialog(this, m); }
}
