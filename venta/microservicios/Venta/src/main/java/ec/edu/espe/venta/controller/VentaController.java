package ec.edu.espe.venta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/Ventas")
public class VentaController {

    private final VentaService VentaService;

    public VentaController(VentaService VentaService) {
        this.VentaService = VentaService;
    }

    @GetMapping
    public ResponseEntity<List<Ventas>> listarVentas() {
        log.info("Obteniendo listado de ventas");
        return ResponseEntity.ok(this.VentaService.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable(name = "id") String id) {
        log.info("Obteniendo venta con ID: {}", id);
        try {
            return ResponseEntity.ok(this.VentaService.obtenerPorId(id));
        } catch(RuntimeException rte) {
            log.error("Error al obtener Venta por ID", rte);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> crear(@RequestBody Venta Venta) {
        log.info("Se va a crear el Venta: {}", Venta);
        try {
            this.VentaService.crear(Venta);
            return ResponseEntity.noContent().build();
        } catch(RuntimeException rte) {
            log.error("Error al crear el Venta", rte);
            return ResponseEntity.badRequest().build();
        }
    }
}
