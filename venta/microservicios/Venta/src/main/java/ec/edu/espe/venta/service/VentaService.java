package ec.edu.espe.venta.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import ec.edu.espe.venta.dao.VentaRepository;
import ec.edu.espe.venta.domain.venta;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VentaService {

    private final VentaRepository VentaRepository;

    public ventaService(VentaRepository VentaRepository) {
        this.VentaRepository = VentaRepository;
    }

    public List<venta> listarTodo() {
        log.info("Se va a obtener todos las ventas");
        List<Venta> dtos = new ArrayList<>();
        for (Venta venta : this.VentaRepository.findAll()) {
            if ("ACT".equals(venta.getExistencia())) {
            dtos.add(venta);
            }
        }
        return dtos;
    }

    public venta obtenerPorIdentificacion(String tipoIdentificacion, String numeroIdentificacion) {
        log.info("Se va a obtener venta por TipoIdentificacion: {} y NumeroIdentificacion: {}", tipoIdentificacion,numeroIdentificacion);
        List<venta> ventas = this.VentaRepository.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion);
        if (ventas != null && !ventas.isEmpty()) {
            if ("ACT".equals(ventas.get(0).getExistencia())) {
                log.debug("venta obtenido: {}", ventas.get(0));
                return ventas.get(0);
            } else {
                throw new RuntimeException("venta con id: " + id
                     + " no se encuentra activo");
            }
        } else {
            throw new RuntimeException("No existe el venta con id: " + id);
        }
    }

    public venta obtenerPorId(String id) {
        log.info("Se va a obtener el venta con ID: {}", id);
        venta venta = this.VentaRepository.findByIdventa(id);
        if ("ACT".equals(venta.getExistencia())) {
            log.debug("venta obtenido: {}", venta);
            return venta;
        } else {
            throw new RuntimeException("venta con ID: " + id + " no se encuentra activo");
        }
    }
    
    @Transactional
    public void crear(venta venta) {
        try {
            venta.setExitencia("1");
            venta.setIdventa(new DigestUtils("MD2").digestAsHex(venta.getTipoIdentificacion()+venta.getNumeroIdentificacion()));
            log.debug("ID venta generado: {}", venta.getIdventa());
            venta.setFechaCreacion(new Date());
            this.VentaRepository.save(venta);
            log.info("Se creo el venta: {}", venta);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el venta.", e);
        }
    }

    @Transactional
    public void actualizar(venta venta) {
        try {
            venta ventaAux = this.VentaRepository.findByIdventa(venta.getIdventa());
            if ("ACT".equals(ventaAux.getEstado())) {
                venta.setExitencia("ACT");
                this.VentaRepository.save(venta);
                log.info("Se actualizaron los datos del venta: {}", venta);
            } else {
                log.error("No se puede actualizar, venta: {} se encuentra INACTIVO", ventaAux);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el venta.", e);
        }
    }

    @Transactional
    public void desactivar(String idventa) {
        log.info("Se va a desactivar el venta: {}", idventa);
        try {
            venta venta = this.VentaRepository.findByIdventa(idventa);
            log.debug("Desactivando venta: {}, estado: 0", idventa);
            venta.setEstado("INA");
            this.VentaRepository.save(venta);
            log.info("Se desactivo el venta: {}", idventa);
        } catch (Exception e) {
            throw new RuntimeException("Error al desactivar venta: " + idventa, e);
        }
    }
}

