package app.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class RandomGenerator {
    public final Random random = new Random();

    public <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public String randomString() {
        int leftLimit = 97; // буква 'a'
        int rightLimit = 122; // буква 'z'
        int targetStringLength = 10;
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public LocalDateTime randomLocalDateTime() {
        LocalDateTime dateTimeStart = LocalDateTime.now();
        long dateStart = dateTimeStart.toEpochSecond(ZoneOffset.UTC);
        LocalDateTime dateTimeEnd = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        long dateEnd = dateTimeEnd.toEpochSecond(ZoneOffset.UTC);
        long randomDate = ThreadLocalRandom.current().nextLong(dateStart, dateEnd);
        return LocalDateTime.ofEpochSecond(randomDate, 0, ZoneOffset.UTC);

    }

    public <E> E getRandomElementOfList(List<E> list) {
        return list.get(random.nextInt(list.size()));
    }

    public <E> List<E> getRandomElements(List<E> list) {
        int x = random.nextInt(list.size());
        List<E> result = new ArrayList<>(list);
        Collections.shuffle(result);
        return result.subList(0, x);
    }

    public Long getRandomId() {
        int randomInt = random.nextInt(1000 - 100) + 100;
        return (long) randomInt;
    }

}
