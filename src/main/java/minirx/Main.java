package minirx;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Observable.create(emitter -> {
            for (int i = 1; i <= 5; i++) {
                emitter.onNext(i);
            }
            emitter.onComplete();
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.single())
        .map(i -> "Item: " + i)
        .filter(item -> !item.contains("3"))
        .flatMap(item -> Observable.create(emitter -> {
            emitter.onNext(item + " [processed]");
            emitter.onComplete();
        }))
        .subscribe(new Observer<String>() {
            public void onNext(String item) {
                System.out.println("Received: " + item);
            }
            public void onError(Throwable t) {
                t.printStackTrace();
            }
            public void onComplete() {
                System.out.println("Completed");
            }
        });

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {}
    }
}
