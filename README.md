# mcloud-orders

### Kafka playground для учебного проекта.

## 🚀 Запуск

### 1. Настройка

Создай `.env` на основе `.env.example` и добавь KAFKA_CLUSTER_ID:

```bash
# Сгенерируй ID
docker run --rm confluentinc/cp-kafka:7.7.1 kafka-storage random-uuid

# Добавь в .env
KAFKA_CLUSTER_ID=твой_uuid
```

### 2. Запуск

```bash
docker-compose up -d
```

### 3. Проверка

```bash
docker ps
```

Должны работать 3 контейнера: kafka, postgres, akhq.

### 4. Остановка

```bash
docker-compose down
```

### 🔧 Команды проверки

```bash
# Список топиков
docker exec -it mcloud-orders-kafka-1 kafka-topics --list --bootstrap-server localhost:9092

# Логи
docker-compose logs kafka --tail 50
```

### 📦 Топики

| Топик            | Партиции | Назначение       |
|------------------|----------|------------------|
| order-events     | 10       | События заказов  |
| payment-events   | 5        | События платежей |
| inventory-events | 3        | События склада   |

### 🔍 Интерфейс

AKHQ: http://localhost:8085

### 📦 Топики для приоритетов

| Топик                    | Партиции | Назначение                |
|--------------------------|----------|---------------------------|
| `orders.priority.high`   | 3        | Высокоприоритетные заказы |
| `orders.priority.normal` | 3        | Обычные заказы            |
| `orders.priority.low`    | 1        | Низкоприоритетные заказы  |

Маршрутизация:

- **HIGH** → `orders.priority.high`
- **NORMAL** → `orders.priority.normal`
- **LOW** → `orders.priority.low`

Ключ партиционирования — **регион** (`region`),
чтобы все заказы из одного региона попадали в одну партицию.

### 🚀 Запуск приложения

```bash
# С профилем local (разработка)
./gradlew bootRun --args='--spring.profiles.active=local'

# Или через IDEA с VM options: -Dspring.profiles.active=local
```

### 🏥 Проверка здоровья

```bash
curl http://localhost:8080/actuator/health
```

Ожидаемый ответ:

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "kafka": {
      "status": "UP"
    },
    "livenessState": {
      "status": "UP"
    },
    "readinessState": {
      "status": "UP"
    }
  }
}
```

### 📨 Проверка отправки заказов

#### 1. Создать заказ (POST)

```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
        "customerId": "550e8400-e29b-41d4-a716-446655440000",
        "region": "EU",
        "priority": "HIGH",
        "amount": 1200.50,
        "lines": [
            {"productId": "6ba7b814-9dad-11d1-80b4-00c04fd430c8", "quantity": 2, "price": 600.25}
        ]
      }'
```

#### Ожидаемый ответ (HTTP 202):

```json
{
  "orderId": "f984427e-5903-49b0-970c-35c1c4d96d7b",
  "status": "NEW",
  "amount": 1200.50,
  "createdAt": "2026-06-28T19:47:21.914986"
}
```

#### 2. Проверить метрики

```bush
curl http://localhost:8080/api/v1/orders/metrics
```

#### Ожидаемый ответ:

```json
{
  "totals": {
    "success": 1,
    "failure": 0
  },
  "topics": {
    "orders.priority.high": {
      "success": 1,
      "failure": 0
    }
  }
}
```

#### 3. Проверить в AKHQ

1. Открой http://localhost:8085
2. Перейди в Topics → orders.priority.high
3. Перейди в Data → Search
4. Убедись, что сообщение появилось:

- Key = регион (например, EU)
- Value = данные заказа в JSON

#### 4. Доступные эндпоинты

| Метод | 	Эндпоинт                 | 	Описание                         |
|-------|---------------------------|-----------------------------------|
| POST  | 	/api/v1/orders	          | Создать заказ и отправить в Kafka |
| GET	  | /api/v1/orders/{orderId}	 | Получить статус заказа (TODO)     |
| GET	  | /api/v1/orders/metrics	   | Статистика отправки сообщений     |
| GET	  | /actuator/health	         | Проверка здоровья приложения      |

### 🔧 Spring-профили

| Профиль	 | Описание                                                            |
|----------|---------------------------------------------------------------------|
| local	   | Для локальной разработки. Использует PostgreSQL, Kafka на localhost |
| test	    | Для тестов. Использует H2 in-memory, Kafka заглушки                 |
| ci	      | Для CI/CD. Использует H2 in-memory, не требует внешних сервисов     |

### 📚 OpenAPI контракт

Документация API доступна в docs/api/order-api.yaml

### 🧪 CI/CD

Проект использует GitHub Actions для CI. При каждом push'е запускаются тесты.

См. .github/workflows/ci.yml

### 📚 OpenAPI контракт

Документация API:  
📄 [`src/main/resources/docs/api/order-api.yaml`](src/main/resources/docs/api/order-api.yaml)

Просмотр в Swagger Editor:  
🌐 [Открыть в Swagger Editor](https://editor.swagger.io/?url=https://raw.githubusercontent.com/Mr-Luckyman/mcloud-orders/feature/MKAFKA-3/src/main/resources/docs/api/order-api.yaml)