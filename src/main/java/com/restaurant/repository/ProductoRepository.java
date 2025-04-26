package com.restaurant.repository;

import com.restaurant.model.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductoRepository {
    private List<Producto> productos;

    public ProductoRepository() {
        this.productos = new ArrayList<>();
        inicializarProductos();
    }

    private void inicializarProductos() {
        // Agregamos algunos productos de ejemplo
        agregarProducto(new Producto(
            UUID.randomUUID().toString(),
            "Bandeja Paisa",
            "Plato típico colombiano con frijoles, arroz, carne molida, chicharrón, huevo frito, plátano maduro y aguacate",
            25000.0,
            "Platos Principales"
        ));

        agregarProducto(new Producto(
            UUID.randomUUID().toString(),
            "Ajiaco Santafereño",
            "Sopa tradicional bogotana con tres tipos de papas, pollo, crema y alcaparras",
            22000.0,
            "Sopas"
        ));

        agregarProducto(new Producto(
            UUID.randomUUID().toString(),
            "Limonada Natural",
            "Bebida refrescante preparada con limones frescos",
            5000.0,
            "Bebidas"
        ));
    }

    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos);
    }

    public Optional<Producto> obtenerPorId(String id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Producto agregarProducto(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(UUID.randomUUID().toString());
        }
        productos.add(producto);
        return producto;
    }

    public boolean actualizarProducto(String id, Producto productoActualizado) {
        Optional<Producto> productoExistente = obtenerPorId(id);
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setCategoria(productoActualizado.getCategoria());
            return true;
        }
        return false;
    }

    public boolean eliminarProducto(String id) {
        return productos.removeIf(p -> p.getId().equals(id));
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return productos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }
}