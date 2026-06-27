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
    "db": {"status": "UP"},
    "kafka": {"status": "UP"},
    "livenessState": {"status": "UP"},
    "readinessState": {"status": "UP"}
  }
}
```

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