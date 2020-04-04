import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ReactorTest {

    @Test
    public void flux_hello() {
        Flux<String> fruitFlux = Flux.just("apple", "orange", "banana");

        StepVerifier.create(fruitFlux)
                .expectNext("apple")
                .expectNext("orange")
                .expectNext("banana")
                .verifyComplete();
    }

    @Test
    void flux_from() {
        String[] fruitsArray = new String[] { "apple", "orange", "banana" };

        Flux<String> fruitsFluxFromArray = Flux.fromArray(fruitsArray);
        StepVerifier.create(fruitsFluxFromArray)
                .expectNext("apple")
                .expectNext("orange")
                .expectNext("banana")
                .verifyComplete();
    }

    @Test
    void flux_range() {
        Flux<Integer> rangeFlux = Flux.range(1,5);

        StepVerifier.create(rangeFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    void flux_interval() {
        Flux<Long> invervalFlux = Flux.interval(Duration.ofSeconds(1L)).take(2);

        StepVerifier.create(invervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void flux_zip() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa");
        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples");

        Flux<Tuple2<String, String>> zip = Flux.zip(characterFlux, foodFlux);

    }

    @Test
    public void flatMap() {
        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] split = p.split("\\s");
                            return new Player(split[0], split[1]);
                        })
                        .subscribeOn(Schedulers.parallel())
                );

        List<Player> playerList = Arrays.asList(
                new Player("Michael", "Jordan"),
                new Player("Scottie", "Pippen"),
                new Player("Steve", "Kerr"));

        StepVerifier.create(playerFlux)
                .expectNextMatches(p -> playerList.contains(p))
                .expectNextMatches(p -> playerList.contains(p))
                .expectNextMatches(p -> playerList.contains(p))
                .verifyComplete();
    }

}

@Data
@AllArgsConstructor
class Player {
    private String name;
    private String surname;
}
