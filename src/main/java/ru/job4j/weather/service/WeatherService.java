package ru.job4j.weather.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.job4j.weather.model.Weather;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {

    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();

    {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 17));
        weathers.put(4, new Weather(4, "Yalta", 32));
        weathers.put(5, new Weather(5, "Kiev", 23));
        weathers.put(6, new Weather(6, "Minsk", 21));
        weathers.put(7, new Weather(7, "Sochi", 31));
    }

    public Mono<Weather> findById(Integer id) {
        return Mono.justOrEmpty(weathers.get(id));
    }

    public Flux<Weather> all() {
        return Flux.fromIterable(weathers.values());
    }

    public Mono<Weather> findHottest() {
        Map.Entry<Integer, Weather> entry = weathers.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue(Comparator.comparingInt(Weather::getTemperature)))
                .get();
        return Mono.just(entry.getValue());
    }

    public Flux<Weather> findCityGreatThen(Integer temp) {
        Collection<Weather> rsl = new ArrayList<>();
        weathers.entrySet()
                .stream()
                .filter(e -> e.getValue().getTemperature() > temp)
                .forEach(e -> rsl.add(e.getValue()));
        return Flux.fromIterable(rsl);
    }
}