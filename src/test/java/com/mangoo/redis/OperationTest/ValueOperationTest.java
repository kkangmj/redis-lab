package com.mangoo.redis.OperationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValueOperationTest {

    @Autowired
    RedisOperations<String, String> operations;
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void beforeEach() {
        valueOperations = operations.opsForValue();
    }

    @Test
    void valueOperations() {
        valueOperations.set("A", "a");
        valueOperations.set("B", "b");

        assertThat(valueOperations.get("A")).isEqualTo("a");
        assertThat(valueOperations.get("B")).isEqualTo("b");
    }
}
