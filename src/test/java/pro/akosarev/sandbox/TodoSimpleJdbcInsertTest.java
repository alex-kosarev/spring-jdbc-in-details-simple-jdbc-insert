package pro.akosarev.sandbox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Profiles;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TodoSimpleJdbcInsertTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    TodoSimpleJdbcInsert insert;

    @BeforeEach
    void setUp(ApplicationContext applicationContext) {
        this.insert = new TodoSimpleJdbcInsert(applicationContext.getBean(JdbcTemplate.class));
        if (applicationContext.getEnvironment().acceptsProfiles(Profiles.of("pgindocker"))) {
            this.jdbcTemplate.execute("select setval('sandbox.t_todo_id_seq', 1, false)");
        } else {
            this.jdbcTemplate.execute("alter sequence sandbox.t_todo_id_seq restart with 1");
        }
    }

    @Test
    void execute_ReturnsAffectedRowsCount() {
        // given
        final var title = "Новая задача";
        final var details = "Описание новой задачи";

        // when
        final var affectedRows = this.insert.execute(Map.of("c_title", title, "c_details", details));

        // then
        assertEquals(1, affectedRows);
    }

    @Test
    void executeAndReturnKey_ReturnsKey() {
        // given
        final var title = "Новая задача";
        final var details = "Описание новой задачи";

        final var insert = new SimpleJdbcInsert(this.jdbcTemplate);
        insert.withSchemaName("sandbox")
                .withTableName("t_todo")
                .usingColumns("c_title", "c_details")
                .usingGeneratedKeyColumns("id");

        // when
        final var key = insert.executeAndReturnKey(Map.of("c_title", title, "c_details", details));

        // then
        assertEquals(1, key);
    }

    @Test
    void executeAndReturnKeyHolder_ReturnsKeyHolder() {
        // given
        final var title = "Новая задача";
        final var details = "Описание новой задачи";

        // when
        final var keyHolder = this.insert.executeAndReturnKeyHolder(Map.of("c_title", title, "c_details", details));

        // then
        assertEquals(1, keyHolder.getKeys().get("id"));
        assertNotNull(keyHolder.getKeys().get("c_created_at"));
    }

    @Test
    void executeBatch_ReturnsAffectedRowsCounts() {
        // given
        final var title1 = "Первая задача";
        final var details1 = "Описание первой задачи";

        final var title2 = "Вторая задача";
        final var details2 = "Описание второй задачи";

        // when
        final var affectedRows = this.insert
                .executeBatch(Map.of("c_title", title1, "c_details", details1),
                        Map.of("c_title", title2, "c_details", details2));

        // then
        assertArrayEquals(new int[]{1, 1}, affectedRows);
    }
}