package minirx;

import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) {

    Observable<String> observable = Observable.<String>create(emitter -> {
      emitter.onNext("Hello");
      emitter.onNext("World");
      emitter.onComplete();
    });

    observable.subscribe(new Observer<String>() {
      @Override
      public void onNext(String item) {
        System.out.println("Received: " + item);
      }

      @Override
      public void onError(Throwable t) {
        t.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("Completed");
      }
    });
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
    }
  }
}
