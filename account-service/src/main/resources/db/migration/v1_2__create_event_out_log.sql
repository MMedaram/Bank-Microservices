-- Producer-side event outbox
CREATE TABLE event_out_log (
    event_id        VARCHAR(100) PRIMARY KEY,
    event_type      VARCHAR(50)  NOT NULL,
    aggregate_type  VARCHAR(50)  NOT NULL,
    aggregate_id    VARCHAR(100) NOT NULL,
    topic_name      VARCHAR(100) NOT NULL,
    payload         JSON         NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    retry_count     INT          DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL,
    published_at    TIMESTAMP
);