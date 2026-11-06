package org.alixar.daw2.FelixRodriguez.dwese_ticket_logger_webapp.DAO;

import org.alixar.daw2.FelixRodriguez.dwese_ticket_logger_webapp.entities.Region;

import java.sql.SQLException;
import java.util.List;

public interface RegionDAO {
    List<Region> ListAllRegion() throws SQLException;
    void insertarRegion(Region region) throws SQLException;
    void actualizarRegion(Region region) throws SQLException;
    void borrarRegion(Long id)  throws SQLException;
    Region obtenerPorId(Long id)  throws SQLException;
    boolean existePorCodigo(String code)  throws SQLException;
    boolean existePorCodigoNoPorId(String code, Long id)  throws SQLException;
}
