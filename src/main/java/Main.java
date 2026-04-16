import ui.VentanaPrincipal;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola, soy Juani");
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            Color fondo   = new Color(38, 38, 48);
            Color texto   = new Color(240, 240, 245);
            Color selec   = new Color(220, 38, 38);
            Color borde   = new Color(50, 50, 65);

            UIManager.put("ComboBox.background",              fondo);
            UIManager.put("ComboBox.foreground",              texto);
            UIManager.put("ComboBox.selectionBackground",     selec);
            UIManager.put("ComboBox.selectionForeground",     texto);
            UIManager.put("ComboBox.buttonBackground",        fondo);
            UIManager.put("ComboBox.disabledBackground",      fondo);

            UIManager.put("ComboBoxUI",                       "javax.swing.plaf.basic.BasicComboBoxUI");

            UIManager.put("List.background",                  fondo);
            UIManager.put("List.foreground",                  texto);
            UIManager.put("List.selectionBackground",         selec);
            UIManager.put("List.selectionForeground",         texto);

            UIManager.put("TextField.background",             fondo);
            UIManager.put("TextField.foreground",             texto);
            UIManager.put("TextField.caretForeground",        texto);

            UIManager.put("Panel.background",                 new Color(22, 22, 28));
            UIManager.put("OptionPane.background",            new Color(28, 28, 35));
            UIManager.put("OptionPane.messageForeground",     texto);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}