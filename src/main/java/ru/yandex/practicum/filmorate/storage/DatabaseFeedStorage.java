package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Repository
public class DatabaseFeedStorage extends DatabaseStorage<Event> implements FeedStorage {
    private static final String INSERT_QUERY =
            "INSERT INTO events(entity_id, event_type, operation, user_id,  created_at)" +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String READ_EVENT_FEED_FOR_USER_QUERY = "SELECT * FROM EVENTS WHERE USER_ID = ?;";


    public DatabaseFeedStorage(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Event create(Event event) {
        long id = insert(
                INSERT_QUERY,
                event.getEntityId(),
                event.getEventType().toString(),
                event.getOperation().toString(),
                event.getUserId(),
                event.getCreatedAt());
        event.setId(id);
        return event;
    }

    @Override
    public List<Event> readEventFeedForUser(Long userId) {
        return jdbc.query(READ_EVENT_FEED_FOR_USER_QUERY, mapper, userId);
    }
}
