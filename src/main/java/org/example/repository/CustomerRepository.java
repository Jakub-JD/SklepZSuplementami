package org.example.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CustomerEntity> rowMapper = (rs, rowNum) -> CustomerEntity.builder()
            .id(rs.getLong("id"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .email(rs.getString("email"))
            .active(rs.getBoolean("is_active"))
            .build();

    //Create
    public void save(CustomerEntity customer) {
        String sql = "INSERT INTO customer_entity (first_name, last_name, email, is_active) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.isActive());
    }

    //Update
    public void update(Long id, CustomerEntity customer) {
        String sql = "UPDATE customer_entity SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getEmail(), id);
    }

    //Read All
    public List<CustomerEntity> findAllActive() {
        String sql = "SELECT * FROM customer_entity WHERE is_active = true";
        return jdbcTemplate.query(sql, rowMapper);
    }

    //Read
    public CustomerEntity findById(Long id) {
        String sql = "SELECT * FROM customer_entity WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst().orElse(null);
    }

    //Inactive flag
    public void deactivate(Long id) {
        String sql = "UPDATE customer_entity SET is_active = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    //Delete
    public void deletePermanently(Long id) {
        String sql = "DELETE FROM customer_entity WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}