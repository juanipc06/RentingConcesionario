package dao;

import model.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDAO {
    private List<Cliente> clientes;
    private int nextId;

    public ClienteDAO() {
        this.clientes = new ArrayList<>();
        this.nextId = 1;
        cargarClientesEjemplo();
    }

    // Agregar cliente
    public void agregar(Cliente cliente) {
        cliente.setId(nextId++);
        clientes.add(cliente);
    }

    // Eliminar por ID
    public boolean eliminar(int id) {
        return clientes.removeIf(c -> c.getId() == id);
    }

    // Actualizar cliente
    public boolean actualizar(Cliente clienteActualizado) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == clienteActualizado.getId()) {
                clientes.set(i, clienteActualizado);
                return true;
            }
        }
        return false;
    }

    // Buscar por ID
    public Cliente buscarPorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Buscar por DNI
    public Cliente buscarPorDni(String dni) {
        return clientes.stream()
                .filter(c -> c.getDni().equalsIgnoreCase(dni))
                .findFirst()
                .orElse(null);
    }

    // Buscar por nombre o apellidos
    public List<Cliente> buscarPorNombre(String texto) {
        String textoBajo = texto.toLowerCase();
        return clientes.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(textoBajo)
                          || c.getApellidos().toLowerCase().contains(textoBajo))
                .collect(Collectors.toList());
    }

    // Listar todos
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    // Datos de ejemplo
    private void cargarClientesEjemplo() {
        agregar(new Cliente(0, "Carlos", "García López", "12345678A", "612345678", "carlos@email.com", "Calle Mayor 1, Sevilla"));
        agregar(new Cliente(0, "María", "Martínez Ruiz", "87654321B", "623456789", "maria@email.com", "Av. Constitución 5, Sevilla"));
        agregar(new Cliente(0, "Pedro", "Sánchez Torres", "11223344C", "634567890", "pedro@email.com", "C/ Sierpes 10, Sevilla"));
        agregar(new Cliente(0, "Laura", "Fernández Gil", "44332211D", "645678901", "laura@email.com", "Plaza Nueva 3, Sevilla"));
    }
}
