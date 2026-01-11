# ☕ Zero Star Coffee – Java Web Application (Servlet + JSP + MySQL)

Zero Star Coffee is a modular Java web application built with **Jakarta Servlet/JSP**, **Tomcat 11**, and **MySQL**.  
The project focuses on clean architecture, reusable UI layouts, internationalization (i18n), flash messaging, and a
structured authentication system.

## 1. Project Overview

| Component              | Description                                  |
|------------------------|----------------------------------------------|
| **Language**           | Java 21+                                     |
| **Web Container**      | Apache Tomcat 11 (Jakarta EE 10)             |
| **Database**           | MySQL 8.x                                    |
| **Connection Pooling** | Apache DBCP2 via Tomcat JNDI                 |
| **Logging**            | Logback (daily rolling logs)                 |
| **UI**                 | JSP, Bootstrap 5, SCSS, shared layout system |

## 2. Project Structure

```
src/main/java/com.laptrinhweb.zerostarcafe/
│
├── core/
│   ├── database/
│   ├── security/
│   ├── utils/
│   └── validation/
│
├── domain/
│   ├── auth/
│   ├── auth_token/
│   ├── user/
│   └── user_role/
│
└── web/
    ├── admin/
    ├── auth/
    └── common/
        ├── filters/
        ├── listeners/
        ├── ErrorServlet
        └── FrontController
```

### JSP + Assets

```
src/main/webapp/
├── assets/
│   ├── client/
│   ├── admin/
│   └── shared/
└── WEB-INF/
    └── views/
        ├── client/
        ├── admin/
        └── shared/
```

### i18n Bundles

```
src/main/resources/
└── translate/
    ├── en-US/
    └── vi-VN/
```

## 3. JSP Layout System (Area-Based)

Each area (client, admin, auth) has its own layout file.

### Example: `client/main-layout.jsp`

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="${pageContext.request.contextPath}/">

<!DOCTYPE html>
<html lang="vi">
<head>
    <jsp:include page="/WEB-INF/views/shared/head.jsp"/>
    <link rel="stylesheet" href="assets/client/styles/app.css">
</head>

<body class="scroll-hidden">

    <jsp:include page="/WEB-INF/views/shared/header.jsp"/>
    <jsp:include page="${requestScope.pageContent}"/>
    <jsp:include page="/WEB-INF/views/shared/footer.jsp"/>

    <!-- Flash -->
    <c:if test="${not empty requestScope.messages}">
        <div class="toast-container" hidden>
            <c:forEach var="msg" items="${requestScope.messages}">
                <p data-type="${msg.type}"
                   data-message="${sessionScope.i18n.trans(msg.msgKey)}"></p>
            </c:forEach>
        </div>
    </c:if>

    <script src="assets/shared/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script type="module" src="assets/shared/js/base.js"></script>
</body>
</html>
```

## 4. JSP Path Rules

1. Always declare base tag in your layout:

```
<base href="${pageContext.request.contextPath}/">
```

2. `jsp:include` must use absolute path:

```
<jsp:include page="/WEB-INF/views/shared/header.jsp"/>
```

3. Asset paths must be relative (because of `<base>`):

```
<link rel="stylesheet" href="assets/client/styles/app.css">
```

## 5. Flash Message System

### Server-side

```java
Flash.from(request)
     .

success("message.register_success")
     .

send();
```

### Client-side

Handled automatically by `assets/shared/js/base.js`.

## 6. Internationalization (i18n)

Bundles:

```
resources/translate/en-US/*.properties
resources/translate/vi-VN/*.properties
```

Usage in JSP:

```
${sessionScope.i18n.trans('form.username')}
```

## 7. Routing Rules (Front Controller)

| Area   | URL Pattern            |
|--------|------------------------|
| Client | `/home`, `/about`, ... |
| Auth   | `/auth/*`              |
| Admin  | `/admin/*`             |

## 8. Authentication System (Business Logic Overview)

The authentication design follows a clean separation of responsibilities.

### 1. AuthService (Login/Register)

- Validates username & password
- Checks account status
- Returns an `AuthenticatedUser` object
- Does **not** manage sessions

### 2. AuthSessionService (Session Lifecycle)

Handles:

- Creating a new session after login
- Storing the authenticated user in session
- Ensuring **only one active session per user**
- Revoking old sessions
- Logout and session cleanup

### 3. AuthSessionManager (Global Session Registry)

Stores:

```
userId → HttpSession
```

Used for:

- single-login enforcement
- central session tracking

### 4. AuthTokenService (Persistent Login)

- Generates long-lived tokens (“auth_token”)
- Validates authToken from cookies
- Restores login without password
- Creates a new session if authToken is valid

### 5. AuthSessionListener (App Startup)

Initializes all authentication-related services and registers them inside `ServletContext`.

## 9. SCSS Build

```
sass ./app.scss ./app.css -w -q
```

## 10. Application URL

```
http://localhost:8080/zero_star_cafe/
```