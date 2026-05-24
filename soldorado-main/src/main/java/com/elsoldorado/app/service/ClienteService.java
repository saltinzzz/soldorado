package com.elsoldorado.app.service;
import com.elsoldorado.app.model.Cliente;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final List<Cliente> clientes = new ArrayList<>();
    private Long secuencia = 1L;

    public List<Cliente> listarClientes() { 
        return clientes; 
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Cliente registrarCliente(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank())
            throw new RuntimeException("El nombre es obligatorio");
        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank())
            throw new RuntimeException("El teléfono es obligatorio");
        cliente.setId(secuencia++);
        clientes.add(cliente);
        return cliente;
    }
}