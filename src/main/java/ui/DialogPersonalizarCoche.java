package ui;

import model.Coche;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class DialogPersonalizarCoche extends JDialog {

    private Coche coche;
    private double precioBase;

    private static final String[] COLORES = {"Blanco", "Negro", "Gris", "Plata", "Rojo", "Azul", "Verde", "Naranja", "Amarillo", "Marrón"};

    private JComboBox<String> cbColor;
    private JLabel lblPrecioFinal;
    private JCheckBox chkTechoSolar, chkCamara, chkBoston, chkAsientos, chkLED, chkLlantas, chkPiloto, chkCuero;

    public DialogPersonalizarCoche(Frame parent, Coche coche) {
        super(parent, "Personalizar: " + coche.getMarca() + " " + coche.getModelo(), true);
        this.coche      = coche;
        this.precioBase = coche.getPrecio();
        inicializarUI();
    }

    private void inicializarUI() {
        setSize(540, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(VentanaPrincipal.COLOR_SECUNDARIO);

        // Cabecera oscura
        JPanel head = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(VentanaPrincipal.COLOR_PRIMARIO);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(VentanaPrincipal.COLOR_ACENTO);
                g.fillRect(0, getHeight()-3, getWidth(), 3);
            }
        };
        head.setOpaque(false);
        head.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblHead = new JLabel("🎨  " + coche.getMarca() + " " + coche.getModelo() + "  ·  " + coche.getAnio() + "  ·  " + coche.getPotenciaCV() + " CV");
        lblHead.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblHead.setForeground(VentanaPrincipal.COLOR_TEXTO);
        head.add(lblHead, BorderLayout.WEST);
        add(head, BorderLayout.NORTH);

        // Panel central
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        centro.setBorder(new EmptyBorder(15, 20, 10, 20));

        // ── Color ──
        JPanel secColor = seccion("🎨  COLOR DE CARROCERÍA");
        JPanel rowColor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        rowColor.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        cbColor = new JComboBox<>(COLORES);
        cbColor.setSelectedItem(coche.getColor());
        cbColor.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        cbColor.setForeground(VentanaPrincipal.COLOR_TEXTO);
        cbColor.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cbColor.setPreferredSize(new Dimension(180, 32));
        JLabel lColor = new JLabel("Color seleccionado:");
        lColor.setForeground(VentanaPrincipal.COLOR_TEXTO_SUAVE);
        lColor.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rowColor.add(lColor); rowColor.add(cbColor);
        secColor.add(rowColor);
        centro.add(secColor);
        centro.add(Box.createVerticalStrut(12));

        // ── Extras ──
        JPanel secExtras = seccion("⚙️  EXTRAS Y EQUIPAMIENTO");
        secExtras.setLayout(new BoxLayout(secExtras, BoxLayout.Y_AXIS));

        chkTechoSolar = chk("Techo solar panorámico",   "+ 1.500 €");
        chkCamara     = chk("Cámara de marcha atrás",   "+   400 €");
        chkBoston     = chk("Sonido premium Boston",     "+   900 €");
        chkAsientos   = chk("Asientos calefactables",    "+   600 €");
        chkLED        = chk("Faros LED matriciales",     "+   750 €");
        chkLlantas    = chk("Llantas deportivas 19\"",   "+ 1.200 €");
        chkPiloto     = chk("Piloto automático",         "+ 2.500 €");
        chkCuero      = chk("Tapicería de cuero",        "+ 1.800 €");

        // Marcar los ya seleccionados
        String ex = coche.getExtras();
        chkTechoSolar.setSelected(ex.contains("Techo")); chkCamara.setSelected(ex.contains("Cámara"));
        chkBoston.setSelected(ex.contains("Boston"));    chkAsientos.setSelected(ex.contains("Asientos"));
        chkLED.setSelected(ex.contains("LED"));          chkLlantas.setSelected(ex.contains("Llantas"));
        chkPiloto.setSelected(ex.contains("Piloto"));    chkCuero.setSelected(ex.contains("Cuero"));

        JCheckBox[] todos = {chkTechoSolar, chkCamara, chkBoston, chkAsientos, chkLED, chkLlantas, chkPiloto, chkCuero};
        for (JCheckBox c : todos) { secExtras.add(c); secExtras.add(Box.createVerticalStrut(4)); }
        centro.add(secExtras);

        JScrollPane scrollCentro = new JScrollPane(centro);
        scrollCentro.setBorder(null);
        scrollCentro.getViewport().setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        add(scrollCentro, BorderLayout.CENTER);

        // ── Precio final ──
        JPanel barPrecio = new JPanel(new FlowLayout(FlowLayout.CENTER));
        barPrecio.setBackground(VentanaPrincipal.COLOR_PRIMARIO);
        barPrecio.setBorder(new EmptyBorder(12, 0, 12, 0));
        lblPrecioFinal = new JLabel();
        lblPrecioFinal.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblPrecioFinal.setForeground(new Color(74, 222, 128));
        barPrecio.add(lblPrecioFinal);

        // Botones
        JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bots.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        JButton btnG = btn("✅ Guardar personalización", VentanaPrincipal.COLOR_ACENTO, Color.WHITE);
        JButton btnC = btn("Cancelar", VentanaPrincipal.COLOR_SUPERFICIE, VentanaPrincipal.COLOR_TEXTO);
        bots.add(btnC); bots.add(btnG);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(VentanaPrincipal.COLOR_SECUNDARIO);
        sur.add(barPrecio, BorderLayout.NORTH);
        sur.add(bots,      BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);

        // Listeners
        java.awt.event.ActionListener al = e -> actualizarPrecio();
        for (JCheckBox c : todos) c.addActionListener(al);

        btnC.addActionListener(e -> dispose());
        btnG.addActionListener(e -> guardar());

        actualizarPrecio();
    }

    private JPanel seccion(String titulo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VentanaPrincipal.COLOR_BORDE),
            new EmptyBorder(10, 15, 10, 15)));
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(VentanaPrincipal.COLOR_ACENTO);
        lbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        p.add(lbl);
        return p;
    }

    private JCheckBox chk(String nombre, String precio) {
        JCheckBox c = new JCheckBox("<html><b>" + nombre + "</b>  <span style='color:#888'>" + precio + "</span></html>");
        c.setBackground(VentanaPrincipal.COLOR_SUPERFICIE);
        c.setForeground(VentanaPrincipal.COLOR_TEXTO);
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.setFocusPainted(false);
        return c;
    }

    private void actualizarPrecio() {
        double t = precioBase;
        if (chkTechoSolar.isSelected()) t += 1500; if (chkCamara.isSelected())   t += 400;
        if (chkBoston.isSelected())     t += 900;  if (chkAsientos.isSelected())  t += 600;
        if (chkLED.isSelected())        t += 750;  if (chkLlantas.isSelected())   t += 1200;
        if (chkPiloto.isSelected())     t += 2500; if (chkCuero.isSelected())     t += 1800;
        lblPrecioFinal.setText(String.format("💰  Precio total: %.2f €", t));
        coche.setPrecio(t);
    }

    private void guardar() {
        coche.setColor((String) cbColor.getSelectedItem());
        StringBuilder sb = new StringBuilder();
        if (chkTechoSolar.isSelected()) sb.append("Techo solar, "); if (chkCamara.isSelected())   sb.append("Cámara marcha atrás, ");
        if (chkBoston.isSelected())     sb.append("Sonido Boston, "); if (chkAsientos.isSelected()) sb.append("Asientos calefactables, ");
        if (chkLED.isSelected())        sb.append("Faros LED, ");    if (chkLlantas.isSelected())   sb.append("Llantas deportivas, ");
        if (chkPiloto.isSelected())     sb.append("Piloto automático, "); if (chkCuero.isSelected()) sb.append("Tapicería Cuero, ");
        String extras = sb.toString();
        if (extras.endsWith(", ")) extras = extras.substring(0, extras.length()-2);
        coche.setExtras(extras);
        actualizarPrecio();
        JOptionPane.showMessageDialog(this, "✅ Personalización guardada.");
        dispose();
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
