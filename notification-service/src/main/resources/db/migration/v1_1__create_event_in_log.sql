-- Consumer-side inbox
CREATE TABLE event_in_log (
    event_id        VARCHAR(100) PRIMARY KEY,
    event_type      VARCHAR(50)  NOT NULL,
    source_service  VARCHAR(50)  NOT NULL,
    topic_name      VARCHAR(100) NOT NULL,
    received_at     TIMESTAMP    NOT NULL,
    processed_at    TIMESTAMP
);
