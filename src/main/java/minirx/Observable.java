package minirx;

import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Predicate;

public class Observable<T> {
    public interface OnSubscribe<T> {
        void subscribe(Observer<T> observer);
    }

    private final OnSubscribe<T> onSubscribe;
    private Scheduler subscribeScheduler = null;
    private Scheduler observeScheduler = null;

    public Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public <R> Observable<R> map(Function<T, R> mapper) {
        return create(observer ->
            subscribe(new Observer<>() {
                public void onNext(T item) {
                    observer.onNext(mapper.apply(item));
                }

                public void onError(Throwable t) {
                    observer.onError(t);
                }

                public void onComplete() {
                    observer.onComplete();
                }
            }));
    }

    public Observable<T> filter(Predicate<T> predicate) {
        return create(observer ->
            subscribe(new Observer<>() {
                public void onNext(T item) {
                    if (predicate.test(item)) {
                        observer.onNext(item);
                    }
                }

                public void onError(Throwable t) {
                    observer.onError(t);
                }

                public void onComplete() {
                    observer.onComplete();
                }
            }));
    }

    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return create(observer ->
            subscribe(new Observer<>() {
                public void onNext(T item) {
                    mapper.apply(item).subscribe(observer);
                }

                public void onError(Throwable t) {
                    observer.onError(t);
                }

                public void onComplete() {
                    observer.onComplete();
                }
            }));
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        this.subscribeScheduler = scheduler;
        return this;
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        this.observeScheduler = scheduler;
        return this;
    }

    public Disposable subscribe(Observer<T> observer) {
        Runnable task = () -> {
            Observer<T> wrappedObserver = observer;
            if (observeScheduler != null) {
                wrappedObserver = new Observer<>() {
                    public void onNext(T item) {
                        observeScheduler.execute(() -> observer.onNext(item));
                    }

                    public void onError(Throwable t) {
                        observeScheduler.execute(() -> observer.onError(t));
                    }

                    public void onComplete() {
                        observeScheduler.execute(observer::onComplete);
                    }
                };
            }

            try {
                onSubscribe.subscribe(wrappedObserver);
            } catch (Throwable t) {
                wrappedObserver.onError(t);
            }
        };

        if (subscribeScheduler != null) {
            subscribeScheduler.execute(task);
        } else {
            task.run();
        }

        return () -> System.out.println("Disposed");
    }
}
