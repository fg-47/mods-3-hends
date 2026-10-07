# Third Hand (Forge 1.20.1)

Добавляет игроку **третью руку**, работающую как вторая (левая), но:
- В неё можно положить **только андезит и его разновидности**
  (андезит, полированный андезит, плита/ступеньки/стена из андезита — проверка по имени блока, содержащему `andesite`).
- Предмет висит **над экраном**, зеркально левой руке.

## Управление
- Клавиша **V** (настраивается в настройках управления) — положить андезит из основной руки в третью руку / забрать обратно.

## Сборка
```bash
./gradlew build
```
Jar будет в `build/libs/thirdhand-1.0.0.jar`.

## Структура
- `capability/ThirdHandCapability.java` — capability хранения предмета, копирование при смерти.
- `network/ThirdHandNetworking.java` — пакет swap для сервера (сервер authoritative).
- `client/ThirdHandKeybinds.java`, `client/ThirdHandOverlay.java` — клавиша и отрисовка над хотбаром.
- `util/AndesiteUtil.java` — проверка "это андезит?".

## Требования
- Java 17, Forge 1.20.1-47.2.0+

## Важно про gradlew
В архиве нет бинарника `gradle/wrapper/gradle-wrapper.jar` (это бинарный файл, его нельзя сгенерировать текстом). Варианты:

**Вариант 1 (проще всего):** скачай Forge 1.20.1 MDK с https://files.minecraftforge.net (кнопка MDK), распакуй, и скопируй туда папку `src` и файлы `build.gradle` и `settings.gradle` из этого архива (замени их). Затем `gradlew.bat build`.

**Вариант 2:** установи Gradle (https://gradle.org/install/), зайди в папку проекта и выполни:
```
gradle wrapper --gradle-version 8.1.1
gradlew.bat build
```

## Сборка через GitHub (без установки Gradle/Java у себя)
1. Создай аккаунт на https://github.com
2. Создай новый репозиторий (кнопка New), название любое, публичный.
3. Нажми "uploading an existing file" и перетащи ВСЁ содержимое этого архива (папку src, build.gradle, settings.gradle, gradlew, gradlew.bat, папки gradle и .github).
4. Нажми Commit changes — сборка запустится автоматически.
5. Через 5-10 минут: вкладка **Actions** → последний запуск → внизу раздел **Artifacts** → скачай `thirdhand-jar`.
6. Положи скачанный jar в папку `mods` (нужен Forge 1.20.1 установленный).
