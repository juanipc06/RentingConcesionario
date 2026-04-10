package model;

import java.time.LocalDate;

public class Venta {
    private int id;
    private Cliente cliente;
    private Coche coche;
    private LocalDate fechaVenta;
    private double precioFinal;
    private String formaPago; // "Contado", "Financiado", "Leasing"
    private String observaciones;

    public Venta(int id, Cliente cliente, Coche coche, LocalDate fechaVenta,
                 double precioFinal, String formaPago, String observaciones) {
        this.id = id;
        this.cliente = cliente;
        this.coche = coche;
        this.fechaVenta = fechaVenta;
        this.precioFinal = precioFinal;
        this.formaPago = formaPago;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Coche getCoche() { return coche; }
    public void setCoche(Coche coche) { this.coche = coche; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public double getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(double precioFinal) { this.precioFinal = precioFinal; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "model.Venta #" + id + " - " + coche + " a " + cliente.getNombre() + " " + cliente.getApellidos();
    }
}
