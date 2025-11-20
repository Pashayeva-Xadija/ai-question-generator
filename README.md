# 🤖 AI Question Generator — Spring Boot 3, JWT, Redis, HuggingFace

AI Question Generator is a fully functional backend service that generates quiz questions using HuggingFace AI models.  
All generated questions are validated, saved to the database, moderated, and can be added into quizzes.  
The system uses **JWT authentication**, **Redis token blacklisting**, **rate limiting**, and **centralized exception handling** for complete security.

✔ This project fully implements **100% of the required features**.

---

# ✨ Key Features

| Feature | Status | Description |
|--------|--------|-------------|
| 🔐 JWT Authentication | ✔ Completed | Login, Register, Refresh, Logout via Redis Blacklist |
| 👥 Role-Based Access | ✔ Completed | STUDENT, TEACHER, ADMIN roles |
| 🤖 AI Question Generation | ✔ Completed | HuggingFace integration + Prompt Engineering |
| 🧠 AI Output Validation | ✔ Completed | Ensures exactly 1 correct option, shuffles answers |
| 📦 DB Storage | ✔ Completed | All AI-generated questions saved in PostgreSQL |
| 👮 Moderator Workflow | ✔ Completed | APPROVE / REJECT questions |
| 🔍 Search & Filtering | ✔ Completed | Topic, difficulty, status, type, pagination |
| 📝 Quiz Builder | ✔ Completed | Create quizzes and attach questions |
| 🚪 Secure Logout | ✔ Completed | Token revocation using Redis or fallback memory |
| 🚦 Rate Limiting | ✔ Completed | Per-user/per-endpoint limit (Redis + fallback) |
| ❗ Global Exception Handler | ✔ Completed | Unified JSON responses |
| 📜 Swagger Documentation | ✔ Completed | Auto-generated API UI |

---

# 🧱 Project Structure

```
src/main/java/az/devlab/aiquestiongenerator
│
├── bootstrap/         
├── config/             
├── controller/      
├── dto/               
├── enums/              
├── exception/          
├── integration/       
├── mapper/             
├── model/            
├── ratelimit/          
├── repository/         
├── security/           
├── service/           
├── serviceimpl/        
└── util/ 
```

---

# 🧭 System Architecture

```
Client
  ↓
AuthController → JWT Tokens
  ↓
Authenticated User
  ↓
QuestionGenerationController
  ↓
PromptBuilderService → Builds AI prompt
  ↓
AiQuestionClient (WebClient → HuggingFace API)
  ↓
HuggingFaceResponseParser → JSON parsing
  ↓
QuestionGenerationUtils → validate & shuffle
  ↓
QuestionService → save to DB
  ↓
AdminQuestionModerationController → APPROVE/REJECT
  ↓
QuizService → quiz creation workflow
```

---

# 🤖 AI Question Generation Flow

```
Teacher/Admin
   ↓
POST /api/questions/generate
   ↓
Request validation (count, difficulty, type)
   ↓
PromptBuilderService builds strict JSON prompt
   ↓
AiQuestionClient sends request to HuggingFace
   ↓
ResponseParser parses JSON array
   ↓
QuestionGenerationUtils:
    • ensureSingleCorrectOption()
    • shuffleOptions()
   ↓
QuestionService persists questions in DB
   ↓
Returns structured list of questions
```

---

# 🔐 Authentication Flow (JWT + Redis Blacklist)

```
User logs in
   ↓
Server returns Access & Refresh Token
   ↓
Each request is validated via JWT
   ↓
Logout:
   token → Redis blacklist stored with expiration
   ↓
TokenBlacklistService checks:
       - Redis.exists()
       - OR fallback in-memory storage
```

---

# 🚦 Rate Limiting Logic

```
Request → RateLimitService.tryConsume(key)

If Redis available:
    INCR key with TTL (windowSeconds)
Else:
    Local in-memory counter

If count > maxRequests:
    → HTTP 429 TOO_MANY_REQUESTS
Else:
    → Request allowed
```

---

# 🧩 Implemented Components

### 🛡 GlobalExceptionHandler
- Handles all custom exceptions
- Handles Spring validation errors
- Handles malformed JSON
- Handles AI service failures (returns 503)
- Clean unified error format

### 🧰 Utility Layer
- `Constants.java` – shared constants
- `ApiErrorCodes.java` – central error catalog
- `QuestionGenerationUtils.java` – validation, shuffling, normalization

### 🗄 Service Layer (Interfaces)
- AuthService
- UserService
- QuestionGenerationService
- QuestionService
- QuizService
- AiQuestionClient
- PromptBuilderService
- ExplanationGenerationService (bonus)
- AlternativeQuestionService (bonus)
- TokenBlacklistService
- RateLimitService

### 🧠 serviceimpl/
- Full implementations for all services
- HuggingFace WebClient integration
- Prompt engineering
- Token blacklisting
- Rate limiting
- AI response parsing
- Quiz creation
- Question moderation
- Question search & pagination

### 🎛 Controller Layer
| Controller | Endpoints | Purpose |
|-----------|-----------|---------|
| AuthController | /auth/login, register, refresh, logout | Authentication |
| QuestionGenerationController | /questions/generate | AI question generation |
| QuestionManagementController | /questions | Search & filtering |
| AdminQuestionModerationController | /admin/questions/moderate | Approve/Reject |
| QuizController | /quizzes, /mine | Quiz builder |
| HealthController | /health | System ping |

---

# 📄 Sample Request

```json
POST /api/questions/generate
{
  "topic": "Java Streams",
  "questionCount": 5,
  "difficulty": "MEDIUM",
  "type": "MULTIPLE_CHOICE"
}
```

---

# 📄 Sample Response

```json
{
  "topic": "Java Streams",
  "difficulty": "MEDIUM",
  "generatedCount": 5,
  "questions": [
    {
      "id": 12,
      "questionText": "Which Stream operation is terminal?",
      "options": [
        {"text": "filter()", "correct": false},
        {"text": "map()", "correct": false},
        {"text": "collect()", "correct": true}
      ],
      "status": "PENDING"
    }
  ]
}
```

---

# 🔧 Running the Project

### Prerequisites
- Java 17
- Maven
- PostgreSQL
- Redis (optional but recommended)
- HuggingFace API key

### Run Application

```bash
mvn spring-boot:run
```

Swagger UI:

```
🔗: http://localhost:8080/swagger-ui/index.html
```

---

# 📬 Contact

Made with  by Xədicə Paşayeva
📧 Email: xadijapashayeva@gmail.com
🔗 LinkedIn: https://www.linkedin.com/in/xadija-pashayeva
