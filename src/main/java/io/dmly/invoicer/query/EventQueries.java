package io.dmly.invoicer.query;

public class EventQueries {
    public static final String SELECT_EVENTS_BY_USER_ID_QUERY = """
            SELECT uev.id, uev.device, uev.ip_address, ev.type, ev.description, uev.created_at
            FROM UserEvents uev JOIN Events ev ON uev.event_id = ev.id
            WHERE uev.user_id = :id
            ORDER BY uev.created_at DESC
            LIMIT 10
            """;

    public static final String INSERT_EVENT_BY_USER_EMAIL_QUERY = """
            INSERT INTO UserEvents (user_id, event_id, device, ip_address) 
            VALUES( 
                (SELECT id FROM Users WHERE email = :email LIMIT 1),    -- user_id
                (SELECT id FROM Events WHERE type = :type LIMIT 1),     -- event_id
                :device,
                :ipAddress                 
            )
            """;
}
