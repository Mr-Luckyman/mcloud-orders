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

### 📁 Структура

```text
mcloud-orders/
├── docker-compose.yml
├── .env
├── .env.example
└── README.md
```