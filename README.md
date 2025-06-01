

````markdown
# mini-rxjava

Реализация упрощённой версии RxJava с поддержкой основных концепций реактивного программирования.

## Возможности

- `Observable`, `Observer`, `Disposable`
- Операторы `map`, `filter`, `flatMap`
- Управление потоками: `subscribeOn`, `observeOn`
- Три реализации `Scheduler`: IO, Computation, Single
- Обработка ошибок и отмена подписки
- Юнит-тесты на все ключевые элементы

## Пример использования

```java
Observable.create(emitter -> {
    emitter.onNext("Hello");
    emitter.onNext("World");
    emitter.onComplete();
})
.subscribeOn(new IOThreadScheduler())
.observeOn(new SingleThreadScheduler())
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
````

## Сборка и запуск

```bash
./gradlew build
./gradlew run
```

## Тесты

Юнит-тесты находятся в `src/test/java`. Покрывают:

* работу операторов (`map`, `filter`, `flatMap`)
* корректность работы `Schedulers`
* обработку ошибок
* отмену подписки (`Disposable`)

## Архитектура

* `Observable` управляет подпиской и трансформацией данных
* `Observer` — обработчик событий
* `Schedulers` позволяют переключать потоки
* Операторы реализованы как обёртки над исходным `Observable`

