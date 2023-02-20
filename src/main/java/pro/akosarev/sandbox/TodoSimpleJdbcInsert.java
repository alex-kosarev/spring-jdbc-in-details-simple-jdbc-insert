package pro.akosarev.sandbox;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public final class TodoSimpleJdbcInsert extends SimpleJdbcInsert {

    public TodoSimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.withSchemaName("sandbox")
                .withTableName("t_todo")
                .usingColumns("c_title", "c_details")
                .usingGeneratedKeyColumns("id", "c_created_at")
                .compile();
    }
}
