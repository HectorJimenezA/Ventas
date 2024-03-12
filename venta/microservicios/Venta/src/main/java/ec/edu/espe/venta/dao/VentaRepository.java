package ec.edu.espe.venta.dao;

import ec.edu.espe.venta.domain.Venta;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends MongoRepository<Venta, String> {

    Venta findById(String id);
    List<Venta> findByVenta(String id, String Venta);

}
