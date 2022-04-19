package ru.job4j.weather.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.model.Weather;
import ru.job4j.weather.service.WeatherService;

import java.time.Duration;

@RestController
public class WeatherControl {

    @Autowired
    private final WeatherService weathers;

    public WeatherControl(WeatherService weathers) {
        this.weathers = weathers;
    }

    @GetMapping(value = "/get/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weathers.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weathers.findById(id);
    }

    @GetMapping(value = "/get/hottest")
    public Mono<Weather> hottest() {
        return weathers.findHottest();
    }

    @GetMapping(value = "/get/cityGreatThen/{temp}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> cityGreatThen(@PathVariable Integer temp) {
        Flux<Weather> data = weathers.findCityGreatThen(temp);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }
}