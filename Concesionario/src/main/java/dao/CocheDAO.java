package dao;

import model.Coche;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CocheDAO {
    private List<Coche> coches;
    private int nextId;

    public CocheDAO() {
        this.coches = new ArrayList<>();
        this.nextId = 1;
        cargarCochesEjemplo();
    }

    // Agregar un coche
    public void agregar(Coche coche) {
        coche.setId(nextId++);
        coches.add(coche);
    }

    // Eliminar por ID
    public boolean eliminar(int id) {
        return coches.removeIf(c -> c.getId() == id);
    }

    // Actualizar coche
    public boolean actualizar(Coche cocheActualizado) {
        for (int i = 0; i < coches.size(); i++) {
            if (coches.get(i).getId() == cocheActualizado.getId()) {
                coches.set(i, cocheActualizado);
                return true;
            }
        }
        return false;
    }

    // Buscar por ID
    public Coche buscarPorId(int id) {
        return coches.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Listar todos
    public List<Coche> listarTodos() {
        return new ArrayList<>(coches);
    }

    // Listar solo disponibles
    public List<Coche> listarDisponibles() {
        return coches.stream()
                .filter(c -> c.getEstado().equals("Disponible"))
                .collect(Collectors.toList());
    }

    // Buscar por marca
    public List<Coche> buscarPorMarca(String marca) {
        return coches.stream()
                .filter(c -> c.getMarca().equalsIgnoreCase(marca))
                .collect(Collectors.toList());
    }

    // Buscar por rango de precio
    public List<Coche> buscarPorPrecio(double min, double max) {
        return coches.stream()
                .filter(c -> c.getPrecio() >= min && c.getPrecio() <= max)
                .collect(Collectors.toList());
    }

    // Datos de ejemplo para arrancar la app
    private void cargarCochesEjemplo() {
        agregar(new Coche(0, "Toyota", "Corolla", 2023, "Blanco", "Gasolina", "Automático", 132, 24500));
        agregar(new Coche(0, "BMW", "Serie 3", 2022, "Negro", "Diésel", "Automático", 190, 48900));
        agregar(new Coche(0, "Volkswagen", "Golf", 2023, "Gris", "Gasolina", "Manual", 150, 29800));
        agregar(new Coche(0, "Tesla", "Model 3", 2024, "Rojo", "Eléctrico", "Automático", 283, 52000));
        agregar(new Coche(0, "Ford", "Mustang", 2023, "Azul", "Gasolina", "Manual", 450, 61000));
        agregar(new Coche(0, "Seat", "Ibiza", 2023, "Blanco", "Gasolina", "Manual", 95, 16900));
        agregar(new Coche(0, "Audi", "A4", 2022, "Gris", "Diésel", "Automático", 163, 44500));
        agregar(new Coche(0, "Mercedes", "Clase C", 2023, "Plata", "Diésel", "Automático", 200, 55000));
    }
}
