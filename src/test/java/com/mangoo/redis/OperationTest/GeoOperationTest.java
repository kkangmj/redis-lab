package com.mangoo.redis.OperationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.time.Duration;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GeoOperationTest {

    @Autowired
    RedisOperations<String, String> operations;
    private GeoOperations<String, String> geoOperations;

    @BeforeEach
    void beforeEach() {
        operations.delete("Location");
        geoOperations = operations.opsForGeo();
    }

    @Test
    void 동일한_좌표로_중복저장한다() {
        geoOperations.add("Location", new Point(126.9456393719818, 37.56021832464497), "이화여자대학교 정문");
        geoOperations.add("Location", new Point(126.94655846373047, 37.55695713486774), "이대역");
        geoOperations.add("Location", new Point(126.93953554179964, 37.564423010024804), "연세대학교 정문");
        geoOperations.add("Location", new Point(126.94155116216828, 37.563279759873005), "신촌역 2호선");
        geoOperations.add("Location", new Point(127.10346320293846, 37.51459490498446), "잠실역 8호선 1");
        geoOperations.add("Location", new Point(127.10346320293846, 37.51459490498446), "잠실역 8호선 2");   // 위와 동일한 좌표

        Circle circle = new Circle(new Point(127.10163268890135, 37.50764975295814), new Distance(1000, RedisGeoCommands.DistanceUnit.METERS));  // 석촌호수 산책길
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOperations.radius("Location", circle);

        System.out.println(results.getContent());
        assertThat(results.getContent()).isNotNull();
        assertThat(results.getContent().get(0).getContent().getName()).isEqualTo("잠실역 8호선 1");
        assertThat(results.getContent().get(1).getContent().getName()).isEqualTo("잠실역 8호선 2");
    }

    @Test
    void 일_밀리초_뒤_Location키를_만료한다() throws InterruptedException {
        geoOperations.add("Location", new Point(126.9456393719818, 37.56021832464497), "이화여자대학교 정문");
        geoOperations.add("Location", new Point(126.94655846373047, 37.55695713486774), "이대역");

        Circle circle = new Circle(new Point(126.9456393719818, 37.56021832464497), new Distance(100000, RedisGeoCommands.DistanceUnit.METERS));
        int beforeExpired = requireNonNull(geoOperations.radius("Location", circle)).getContent().size();

        operations.expire("Location", Duration.ofMillis(1));
        Thread.sleep(1);

        int afterExpired = requireNonNull(geoOperations.radius("Location", circle)).getContent().size();

        assertThat(beforeExpired).isEqualTo(2);
        assertThat(afterExpired).isEqualTo(0);
        assertThat(beforeExpired).isNotEqualTo(afterExpired);
    }
}
