package org.example.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class SupplementRepository {

    private final JdbcTemplate jdbcTemplate;

    public SupplementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<SupplementEntity> rowMapper = (rs, rowNum) -> SupplementEntity.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .price(rs.getDouble("price"))
            .build();

    //Create
    public void save(SupplementEntity supplement) {
        String sql = "INSERT INTO supplement_entity (name, price) VALUES (?, ?)";
        jdbcTemplate.update(sql, supplement.getName(), supplement.getPrice());
    }

    //Read All
    public List<SupplementEntity> findAll() {
        String sql = "SELECT * FROM supplement_entity";
        return jdbcTemplate.query(sql, rowMapper);
    }

    //Update
    public void updatePrice(Long id, double newPrice) {
        String sql = "UPDATE supplement_entity SET price = ? WHERE id = ?";
        jdbcTemplate.update(sql, newPrice, id);
    }

    //Delete
    public void delete(Long id) {
        String sql = "DELETE FROM supplement_entity WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}