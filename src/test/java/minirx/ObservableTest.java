package minirx;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class ObservableTest {
  @Test
  public void testMapAndFilter() {
    Observable.<Integer>create(emitter -> {
      emitter.onNext(1);
      emitter.onNext(2);
      emitter.onNext(3);
      emitter.onComplete();
    })
        .map(i -> i * 2)
        .filter(i -> i > 2)
        .subscribe(new Observer<Integer>() {
          @Override
          public void onNext(Integer item) {
            System.out.println("Filtered & Mapped: " + item);
          }

          @Override
          public void onError(Throwable t) {
            t.printStackTrace();
          }

          @Override
          public void onComplete() {
            System.out.println("Stream completed");
          }
        });
  }
}
