package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Cliente;
import com.elsoldorado.app.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente registrarCliente(Cliente cliente) {
        validarCliente(cliente);
        if (clienteRepository.existsByTelefono(cliente.getTelefono())) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con ese teléfono");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        if (clienteActualizado.getNombre() != null && !clienteActualizado.getNombre().isBlank()) {
            cliente.setNombre(clienteActualizado.getNombre());
        }
        if (clienteActualizado.getTelefono() != null && !clienteActualizado.getTelefono().isBlank()) {
            cliente.setTelefono(clienteActualizado.getTelefono());
        }
        cliente.setEmail(clienteActualizado.getEmail());
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.delete(cliente);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
    }
}
