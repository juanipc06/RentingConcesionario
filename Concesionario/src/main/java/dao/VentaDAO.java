package dao;

import model.Venta;
import model.Cliente;
import model.Coche;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VentaDAO {
    private List<Venta> ventas;
    private int nextId;

    public VentaDAO() {
        this.ventas = new ArrayList<>();
        this.nextId = 1;
    }

    // Registrar venta
    public void agregar(Venta venta) {
        venta.setId(nextId++);
        venta.getCoche().setEstado("Vendido");
        ventas.add(venta);
    }

    // Eliminar por ID
    public boolean eliminar(int id) {
        Venta venta = buscarPorId(id);
        if (venta != null) {
            venta.getCoche().setEstado("Disponible");
            return ventas.removeIf(v -> v.getId() == id);
        }
        return false;
    }

    // Buscar por ID
    public Venta buscarPorId(int id) {
        return ventas.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Listar todas
    public List<Venta> listarTodas() {
        return new ArrayList<>(ventas);
    }

    // Ventas por cliente
    public List<Venta> buscarPorCliente(Cliente cliente) {
        return ventas.stream()
                .filter(v -> v.getCliente().getId() == cliente.getId())
                .collect(Collectors.toList());
    }

    // Ventas por rango de fechas
    public List<Venta> buscarPorFechas(LocalDate desde, LocalDate hasta) {
        return ventas.stream()
                .filter(v -> !v.getFechaVenta().isBefore(desde) && !v.getFechaVenta().isAfter(hasta))
                .collect(Collectors.toList());
    }

    // Total facturado
    public double totalFacturado() {
        return ventas.stream()
                .mapToDouble(Venta::getPrecioFinal)
                .sum();
    }

    // Número de ventas
    public int numeroVentas() {
        return ventas.size();
    }
}
