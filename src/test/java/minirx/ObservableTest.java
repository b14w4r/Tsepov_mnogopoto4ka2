package minirx;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ObservableTest {
    @Test
    public void testMapAndFilter() {
        AtomicInteger count = new AtomicInteger(0);
        Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        })
        .map(i -> i * 2)
        .filter(i -> i > 2)
        .subscribe(new Observer<>() {
            public void onNext(Integer item) {
                count.incrementAndGet();
            }

            public void onError(Throwable t) {
                fail();
            }

            public void onComplete() {}
        });

        assertEquals(2, count.get());
    }
}
