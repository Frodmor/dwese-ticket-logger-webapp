package org.alixar.daw2.FelixRodriguez.dwese_ticket_logger_webapp.DAO;
import org.alixar.daw2.FelixRodriguez.dwese_ticket_logger_webapp.entities.Region;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
@Repository
public class RegionDAOImpl implements RegionDAO {

    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger =
            LoggerFactory.getLogger(RegionDAOImpl.class);
    private final JdbcTemplate jdbcTemplate;
    // Inyección de JdbcTemplate
    public RegionDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Region> ListAllRegion() {
        logger.info("Listing all regions from the database.");
        String sql = "SELECT * FROM regions";
        List<Region> regions = jdbcTemplate.query(sql, new
                BeanPropertyRowMapper<>(Region.class));
        logger.info("Retrieved {} regions from the database.", regions.size());
        return regions;
    }

    @Override
    public void insertarRegion(Region region) {
        logger.info("Inserting region with code: {} and name: {}",
                region.getCode(), region.getName());
        String sql = "INSERT INTO regions (code, name) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, region.getCode(),
                region.getName());
        logger.info("Inserted region. Rows affected: {}", rowsAffected);
    }

    @Override
    public void actualizarRegion(Region region) {
        logger.info("Updating region with id: {}", region.getId());
        String sql = "UPDATE regions SET code = ?, name = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, region.getCode(),
                region.getName(), region.getId());
        logger.info("Updated region. Rows affected: {}", rowsAffected);
    }

    @Override
    public void borrarRegion(Long id) {
        logger.info("Deleting region with id: {}", id);
        String sql = "DELETE FROM regions WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        logger.info("Deleted region. Rows affected: {}", rowsAffected);
    }


    @Override
    public Region obtenerPorId(Long id) {
        logger.info("Retrieving region by id: {}", id);
        String sql = "SELECT * FROM regions WHERE id = ?";
        try {
            Region region = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Region.class), id);
            logger.info("Region retrieved: {} - {}", region.getCode(), region.getName());
            return region;
        } catch (Exception e) {
            logger.warn("No region found with id: {}", id);
            return null;
        }
    }

    @Override
    public boolean existePorCodigo(String code) {
        logger.info("Checking if region with code: {} exists", code);
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                code.toUpperCase());
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists: {}", code, exists);
        return exists;
    }

    @Override
    public boolean existePorCodigoNoPorId(String code, Long id) {
        logger.info("Checking if region with code: {} exists excluding id: {}",
                code, id);
        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ? AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                code.toUpperCase(), id);
        boolean exists = count != null && count > 0;
        logger.info("Region with code: {} exists excluding id {}: {}", code, id,
                exists);
        return exists;
    }


}

