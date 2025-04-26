package com.restaurant;

import com.restaurant.controller.ProductoController;
import com.restaurant.repository.ProductoRepository;
import static spark.Spark.*;

public class RestaurantApplication {
    public static void main(String[] args) {
        // Configuración de Spark
        port(8080);

        // Configuración CORS para desarrollo
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        });

        // Inicialización de dependencias
        ProductoRepository repository = new ProductoRepository();
        ProductoController controller = new ProductoController(repository);

        // Definición de rutas
        path("/api", () -> {
            // Rutas para productos
            get("/productos", controller::obtenerTodos);
            get("/productos/:id", controller::obtenerPorId);
            post("/productos", controller::crearProducto);
            put("/productos/:id", controller::actualizarProducto);
            delete("/productos/:id", controller::eliminarProducto);
            get("/productos/categoria/:categoria", controller::buscarPorCategoria);
        });

        // Ruta raíz
        get("/", (req, res) -> "Bienvenido a la API del Restaurante");

        // Ruta para el favicon
        get("/favicon.ico", (req, res) -> {
            res.status(204);
            return "";        
        });

        // Ruta de prueba para verificar que el servidor está funcionando
        get("/ping", (req, res) -> "¡El servidor está funcionando!");
    }
}