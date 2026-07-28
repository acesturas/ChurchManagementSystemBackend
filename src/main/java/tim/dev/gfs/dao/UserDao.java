package tim.dev.gfs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import tim.dev.gfs.model.User;

@Repository
public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User findByUsername(String username) {

        String sql = """
        		SELECT id,
				       username,
				       password,
				       role,
				       enabled,
				       full_name,
				       email
				  FROM users
				 WHERE username = ?
        		""";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

            	if (rs.next()) {

            	    User user = new User();

            	    user.setId(rs.getLong("id"));
            	    user.setUsername(rs.getString("username"));
            	    user.setPassword(rs.getString("password"));

            	    user.setRole(rs.getString("role"));
            	    user.setEnabled(rs.getBoolean("enabled"));

            	    user.setFullName(rs.getString("full_name"));
            	    user.setEmail(rs.getString("email"));

            	    return user;
            	}
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve user.", e);
        }

        return null;
    }
    
    public boolean existsByUsername(String username) {

        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void insertUser(User user) {

        String sql = """
            INSERT INTO users
            (username, password, full_name, email, role, enabled)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getRole());
            ps.setBoolean(6, user.isEnabled());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}