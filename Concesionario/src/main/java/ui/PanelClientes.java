package ui;

import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class PanelClientes extends JPanel {

    private ClienteDAO clienteDAO;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    public PanelClientes(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
        inicializarUI();
        cargarTabla(clienteDAO.listarTodos());
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
        JLabel titulo    = new JLabel("GESTIÓN DE CLIENTES");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        JLabel subtitulo = new JLabel("Base de datos de clientes registrados");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
        tituloPanel.add(titulo);
        tituloPanel.add(Box.createVerticalStrut(4));
        tituloPanel.add(subtitulo);
        cabecera.add(tituloPanel, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setOpaque(false);
        JButton btnNuevo    = crearBoton("+ Nuevo Cliente", VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        JButton btnEditar   = crearBoton("✏ Editar",        VentanaPrincipal.COLOR_SUPERFICIE, VentanaPrincipal.COLOR_TEXTO);
        JButton btnEliminar = crearBoton("🗑 Eliminar",      new Color(40,40,50), VentanaPrincipal.COLOR_TEXTO_SUAVE);
        panelBotones.add(btnNuevo); panelBotones.add(btnEditar); panelBotones.add(btnEliminar);
        cabecera.add(panelBotones, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        // Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFiltros.setOpaque(false);
        panelFiltros.setBorder(new EmptyBorder(0, 0, 15, 0));
        txtBuscar = estilizarTextField(new JTextField(25));
        JButton btnBuscar = crearBoton("Buscar", VentanaPrincipal.COLOR_SECUNDARIO, VentanaPrincipal.COLOR_TEXTO);
        panelFiltros.add(label("Buscar por nombre o DNI:"));
        panelFiltros.add(txtBuscar);
        panelFiltros.add(btnBuscar);

        // Tabla
        String[] cols = {"ID", "Nombre", "Apellidos", "DNI", "Teléfono", "Email", "Dirección"};
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
                setHorizontalAlignment(col < 4 ? SwingConstants.CENTER : SwingConstants.LEFT);
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
        sur.add(panelFiltros, BorderLayout.NORTH);
        sur.add(scroll,       BorderLayout.CENTER);
        add(sur, BorderLayout.CENTER);

        btnNuevo.addActionListener(e    -> abrirFormNuevo());
        btnEditar.addActionListener(e   -> abrirFormEditar());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnBuscar.addActionListener(e   -> buscar());
        txtBuscar.addActionListener(e   -> buscar());
    }

    private void cargarTabla(List<Cliente> clientes) {
        modeloTabla.setRowCount(0);
        for (Cliente c : clientes)
            modeloTabla.addRow(new Object[]{ c.getId(), c.getNombre(), c.getApellidos(), c.getDni(), c.getTelefono(), c.getEmail(), c.getDireccion() });
    }

    private void buscar() {
        String txt = txtBuscar.getText().trim();
        if (txt.isEmpty()) { cargarTabla(clienteDAO.listarTodos()); return; }
        Cliente porDni = clienteDAO.buscarPorDni(txt);
        if (porDni != null) cargarTabla(Arrays.asList(porDni));
        else                cargarTabla(clienteDAO.buscarPorNombre(txt));
    }

    private void abrirFormNuevo() {
        DialogFormCliente d = new DialogFormCliente((Frame) SwingUtilities.getWindowAncestor(this), null);
        d.setVisible(true);
        if (d.getCliente() != null) { clienteDAO.agregar(d.getCliente()); cargarTabla(clienteDAO.listarTodos()); }
    }

    private void abrirFormEditar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un cliente."); return; }
        Cliente c = clienteDAO.buscarPorId((int) modeloTabla.getValueAt(fila, 0));
        DialogFormCliente d = new DialogFormCliente((Frame) SwingUtilities.getWindowAncestor(this), c);
        d.setVisible(true);
        if (d.getCliente() != null) { clienteDAO.actualizar(d.getCliente()); cargarTabla(clienteDAO.listarTodos()); }
    }

    private void eliminarCliente() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un cliente."); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clienteDAO.eliminar((int) modeloTabla.getValueAt(fila, 0));
            cargarTabla(clienteDAO.listarTodos());
        }
    }

    public void refrescar() { cargarTabla(clienteDAO.listarTodos()); }

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
}
