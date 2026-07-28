package tim.dev.gfs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import tim.dev.gfs.model.Events;

@Repository
public class EventsDao {

    private final DataSource dataSource;

    public EventsDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Events> getAllEvents() {

        List<Events> events = new ArrayList<>();

        String sql = """
                SELECT id,
                       event_name,
                       description,
                       event_start_date,
                       event_end_date,
                       start_time,
                       end_time,
                       location
                FROM events
                ORDER BY event_start_date ASC, start_time ASC
                """;

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Events e = new Events();

                e.setId(rs.getString("id"));
                e.setEventName(rs.getString("event_name"));
                e.setDescription(rs.getString("description"));
                e.setLocation(rs.getString("location"));

                if (rs.getDate("event_start_date") != null)
                    e.setEventStartDate(rs.getDate("event_start_date").toLocalDate());

                if (rs.getDate("event_end_date") != null)
                    e.setEventEndDate(rs.getDate("event_end_date").toLocalDate());

                if (rs.getTime("start_time") != null)
                    e.setStartTime(rs.getTime("start_time").toLocalTime());

                if (rs.getTime("end_time") != null)
                    e.setEndTime(rs.getTime("end_time").toLocalTime());

                events.add(e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve events", e);
        }

        return events;
    }
}