<<<<<<< HEAD
# Session Countdown Timer для IDE от JetBrains

#### Плагин позволяет установить таймер обратного отсчёта рабочего сеанса с возможностью паузы и настройки времени. Таймер отображается в нижнем статус-баре интерфейса IDE.

## Архитектура

### Основные компоненты:

- **SessionCountdownWidget.kt** – основной виджет плагина:

  - Реализует `StatusBarWidget` и `StatusBarWidgetFactory` для интеграции в строку состояния.

  - Показывает таймер обратного отсчёта в формате `MM:SS` (⏳ или ⏸).

  - Обновляется каждую секунду с помощью `ScheduledExecutorService`.

  - Обрабатывает пользовательские действия:
    - Левый клик – открывает меню с двумя опциями: "Пауза/Возобновление" и "Установить время".
    - Ввод времени через всплывающее диалоговое окно.

  - При завершении таймера отображает сообщение в центре экрана.

- **SessionCountdownWidgetFactory.kt** – фабрика виджетов:

  - Использует `StatusBarWidgetFactory` для создания и управления виджетом.

  - Устанавливает виджет в строку состояния при запуске IDE.

## Взаимодействие компонентов:

1. **SessionCountdownWidget**:
  - Управляет логикой таймера (пауза, возобновление, установка нового времени).
  - Обновляет текст в строке состояния каждую секунду.
  - При завершении таймера вызывает метод для показа уведомления.

2. **SessionCountdownWidgetFactory**:
  - Регистрирует виджет в статусной строке.

3. **User Actions**:
  - **Левый клик**: вызывает меню с выбором опций:
    - **Пауза/Возобновление**: приостанавливает или запускает таймер.
    - **Установить время**: открывает диалоговое окно для ввода нового времени сеанса.
  - **Таймер завершён**: показывает уведомление в центре экрана.

## Формат отображения времени:

- **MM:SS** – если длительность таймера менее часа.
- **HH:MM:SS** – если длительность таймера превышает час.

Пример отображения:  
⏳ `25:00` – таймер работает.  
⏸ `10:15` – таймер на паузе.

## Примечание:

- Поддерживается с билдов 220 и выше платформы IntelliJ.
-  Протестировано на IntelliJ IDEA Community 2023.2.
-  Работает на всех IDE, основанных на платформе IntelliJ (PyCharm, WebStorm, CLion и т.д.).

## Сборка и интеграция:

1. **Проверка конфигурации проекта**:
  - Убедитесь, что установлены корректные версии JVM (17+), Gradle и IntelliJ IDEA SDK.

2. **Сборка плагина**:
    ```bash
    ./gradlew clean buildPlugin
    ```
   После успешной сборки файл плагина будет находиться в директории `build/distributions/` с именем:
    ```
    session-countdown-timer-1.0-SNAPSHOT.zip
    ```

3. **Установка плагина в IDE**:
  - Откройте **Settings → Plugins**.
  - Нажмите на иконку шестерёнки в правом верхнем углу окна.
  - Выберите **Install Plugin from Disk...**.
  - Укажите путь к файлу `session-countdown-timer-1.0-SNAPSHOT.zip`.
  - Перезапустите IDE для применения изменений.

## Использование:

- Таймер автоматически появляется в **статусной строке**.
- **Левый клик** на таймер:
  - Вызывает меню с опциями: **Пауза/Возобновление** и **Установить время**.
- **Ввод времени**:
  - В окне "Set Timer" введите количество минут для нового сеанса.
- **Завершение таймера**:
  - Появляется уведомление в центре экрана с предложением сделать перерыв или начать новый сеанс.

---
=======
# SessionCountdownTimer

![Build](https://github.com/afterchanges/SessionCountdownTimer/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [ ] Get familiar with the [template documentation][template].
- [ ] Adjust the [pluginGroup](./gradle.properties) and [pluginName](./gradle.properties), as well as the [id](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
- [ ] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the `MARKETPLACE_ID` in the above README badges. You can obtain it once the plugin is published to JetBrains Marketplace.
- [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
This Fancy IntelliJ Platform Plugin is going to be your implementation of the brilliant ideas that you have.

This specific section is a source for the [plugin.xml](/src/main/resources/META-INF/plugin.xml) file which will be extracted by the [Gradle](/build.gradle.kts) during the build process.

To keep everything working, do not remove `<!-- ... -->` sections. 
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "SessionCountdownTimer"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/afterchanges/SessionCountdownTimer/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
>>>>>>> 707282f154e20e07e4213068557b4a1c94b87b11
