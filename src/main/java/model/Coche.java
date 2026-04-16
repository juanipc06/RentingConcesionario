package model;

public class Coche {
    private int id;
    private String marca;
    private String modelo;
    private int anio;
    private String color;
    private String tipoCombustible;
    private String transmision;
    private int potenciaCV;
    private double precio;
    private String estado; // "Disponible", "Reservado", "Vendido"
    private String extras; // opciones personalizadas

    public Coche(int id, String marca, String modelo, int anio, String color,
                 String tipoCombustible, String transmision, int potenciaCV, double precio) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.tipoCombustible = tipoCombustible;
        this.transmision = transmision;
        this.potenciaCV = potenciaCV;
        this.precio = precio;
        this.estado = "Disponible";
        this.extras = "";
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public String getTransmision() { return transmision; }
    public void setTransmision(String transmision) { this.transmision = transmision; }

    public int getPotenciaCV() { return potenciaCV; }
    public void setPotenciaCV(int potenciaCV) { this.potenciaCV = potenciaCV; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getExtras() { return extras; }
    public void setExtras(String extras) { this.extras = extras; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + anio + ")";
    }
}
