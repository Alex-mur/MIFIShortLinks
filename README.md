﻿# Сервис коротких ссылок

Простой сервис для сокращения URL, который поддерживает управление ссылками через консоль. Он позволяет создавать уникальные короткие ссылки, ограничивать количество переходов и удалять протухшие ссылки по истечении времени.

## Содержание

1. [Описание](#описание)
2. [Особенности](#особенности)
3. [Установка](#установка)
4. [Использование](#использование)

## Описание

Этот сервис предоставляет функциональность для сокращения длинных URL в уникальные короткие ссылки. Приложение реализует следующие функции:

- Принимает длинные URL и генерирует уникальные короткие ссылки.
- Ограничивает количество переходов по коротким ссылкам. Ссылка удаляется, как только лимит переходов исчерпан.
- Удаляет старые или протухшие ссылки по истечении времени.
- Уведомляет пользователей, если ссылка становится недоступной.
- Управляется через консоль, в том числе позволяет переходить по сокращенным ссылкам.

## Особенности

- Сокращение URL с уникальными идентификаторами.
- Поддержка лимита переходов по каждой ссылке.
- Удаление протухших ссылок по заданному времени жизни.
- Уведомление пользователя о недоступности ссылки.
- Простое управление через командную строку.
- Конфигурационный файл для настроек параметров работы

## Установка

### 1. Клонируйте репозиторий:

```bash
git clone https://github.com/Alex-mur/MIFIShortLinks.git
```

### 2. Соберите проект в IntelliJ IDEA либо с помощью maven

### 3. При необходимости можно изменить параметры в конфигурационном файле:
```src/main/resources/app_config.cfg```

#### Для изменения доступны следующие параметры:

- DEFAULT_URL_LIFETIME_HOURS - Время жизни ссылки, по-умолчанию 48
- SITE_BASE_URL - Базовый адрес сайта коротких ссылок
- SHORT_URL_ID_LENGTH - Длина короткой ссылки, допустимые значения от 2 до 8, по-умолчанию 6

## Использование

Сервис управляется через консоль.
После запуска приложения вы увидите понятное меню. Выберите нужное действие и следуйте подсказкам на экране. 
