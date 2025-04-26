package com.restaurant.controller;

import com.google.gson.Gson;
import com.restaurant.model.Producto;
import com.restaurant.repository.ProductoRepository;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductoController {
    private final ProductoRepository repository;
    private final Gson gson;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
        this.gson = new Gson();
    }

    public String obtenerTodos(Request req, Response res) {
        res.type("application/json");
        return gson.toJson(repository.obtenerTodos());
    }

    public String obtenerPorId(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");
        return repository.obtenerPorId(id)
                .map(gson::toJson)
                .orElseGet(() -> {
                    res.status(404);
                    return crearMensajeError("Producto no encontrado");
                });
    }

    public String crearProducto(Request req, Response res) {
        res.type("application/json");
        try {
            Producto nuevoProducto = gson.fromJson(req.body(), Producto.class);
            Producto productoCreado = repository.agregarProducto(nuevoProducto);
            res.status(201);
            return gson.toJson(productoCreado);
        } catch (Exception e) {
            res.status(400);
            return crearMensajeError("Error al crear el producto: " + e.getMessage());
        }
    }

    public String actualizarProducto(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");
        try {
            Producto productoActualizado = gson.fromJson(req.body(), Producto.class);
            boolean actualizado = repository.actualizarProducto(id, productoActualizado);
            if (actualizado) {
                return gson.toJson(repository.obtenerPorId(id).get());
            } else {
                res.status(404);
                return crearMensajeError("Producto no encontrado");
            }
        } catch (Exception e) {
            res.status(400);
            return crearMensajeError("Error al actualizar el producto: " + e.getMessage());
        }
    }

    public String eliminarProducto(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");
        boolean eliminado = repository.eliminarProducto(id);
        if (eliminado) {
            res.status(204);
            return "";
        } else {
            res.status(404);
            return crearMensajeError("Producto no encontrado");
        }
    }

    public String buscarPorCategoria(Request req, Response res) {
        res.type("application/json");
        String categoria = req.params(":categoria");
        return gson.toJson(repository.buscarPorCategoria(categoria));
    }

    private String crearMensajeError(String mensaje) {
        Map<String, String> error = new HashMap<>();
        error.put("error", mensaje);
        return gson.toJson(error);
    }
}