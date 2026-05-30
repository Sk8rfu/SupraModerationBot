<p align="center">
  <img src="https://github.com/Sk8rfu/SupraModerationBot/blob/assets/supra.jpg?raw=true?raw=true">
</p>

<p align="center">
<img src="https://img.shields.io/badge/Java-17-orange">
<img src="https://img.shields.io/badge/Maven-3.8+-blue">
<img src="https://img.shields.io/badge/JDA-5.0-purple">
<img src="https://img.shields.io/badge/Build-Success-brightgreen">
<img src="https://img.shields.io/badge/License-MIT-green">
<img src="https://img.shields.io/badge/Status-Active-success">
</p>

# SupraModerationBot
Мощен Discord модерационен бот, написан на Java + JDA, създаден да направи модерацията лесна, бърза и ефективна.

## 🚀 Инсталация и стартиране

### 1. Инсталирай нужните зависимости

Java 17+

Maven 3.8+

---
Провери версиите:

```
java -version
mvn -version
```

### 2. Клонирай проекта
---

```
git clone https://github.com/Sk8rfu/SupraModerationBot.git
cd SupraModerationBot
```

### 3. Добави своя Discord токен
---
Отвори файла:

```
src/main/java/com/mikubot/config/Config.java
```
И замени:

```
public static final String TOKEN = "YOUR_TOKEN_HERE";
```

## ⚠️ Важно:
Никога не качвай реалния си токен в GitHub.
Ако случайно го качиш → смени го веднага от Discord Developer Portal.

### 4. Компилирай проекта
---

Командата трябва да се изпълни в главната папка на проекта, там където се намира:
```
pom.xml
```
Тоест:

```
cd SupraModerationBot
mvn clean package
```
След успешна компилация Maven ще създаде JAR файл в:

```
target/
```

### 5. Стартирай бота
---
```
java -jar target/SupraModerationBot-1.0-SNAPSHOT.jar
```

### 📁 Структура на проекта
---
```
src/
 └── main/
     └── java/
         └── com/
             └── mikubot/
                 ├── commands/        # Команди
                 ├── config/          # Config.java (токенът)
                 ├── listeners/       # Event listeners
                 ├── util/            # Помощни класове
                 ├── Main.java        # Главен клас
                 └── CommandManager.java        # Slash команди
```

### 📜 Команди
---

### 🧾 Информация

/ping	Показва latency

/help	Показва менюто с команди

/userinfo	Информация за потребител

/serverinfo	Информация за сървъра

/avatar	Показва аватар

/about	Информация за бота

---

### 🔨 Модерация

/ban	Банва потребител

/tempban	Временен бан (1s, 1m, 1h, 1d, 1w)

/unban	Премахва бан

/banid	Банва потребител по ID

/banlist	Показва списък с баннати

/kick	Киква потребител

/mute	Мютва (timeout)

/unmute	Размютва (timeout)

/muterole	Мютва чрез роля

/unmuterole	Размютва чрез роля

/warn	Предупреждава потребител

/unwarn	Премахва всички предупреждения

/warnings	Показва предупрежденията

---

### ⚙️ Управление

/giverole	Дава роля

/removerole	Маха роля

/createrole	Създава роля

/editrole	Редактира роля

/deleterole	Изтрива роля

/clear	Изтрива съобщения

/slowmode	Задава slowmode

/lock	Заключва канал

/unlock	Отключва канал

/nickname	Променя прякор

/invite	Създава покана (1s, 1m, 1h, 1d, 1w)

---

### 👥 Социални

/report	Изпраща репорт към модераторите

/suggest	Изпраща предложение

---

### ❤️ Създаден с любов
### Този бот е направен с много желание, старание и ❤️, за да бъде полезен на всеки Discord сървър, който има нужда от стабилна и лесна модерация.
