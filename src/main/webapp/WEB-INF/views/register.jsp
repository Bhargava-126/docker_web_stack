<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Sign up | Docker Web Stack</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <header class="topbar">
    <span class="brand">DEVOPS<small>docker project</small></span>
    <nav>
      <a href="${pageContext.request.contextPath}/login">Login</a>
      <a href="${pageContext.request.contextPath}/register">Sign up</a>
    </nav>
  </header>

  <main class="card">
    <h1>Create an account</h1>
    <div class="alert error" ${empty error ? 'hidden' : ''}>${error}</div>

    <form id="authForm" method="post" action="${pageContext.request.contextPath}/register" novalidate>
      <label>Username
        <input type="text" name="username" value="${username}" autocomplete="username" required>
      </label>
      <label>Email
        <input type="email" name="email" value="${email}" autocomplete="email" required>
      </label>
      <label>Password (min 8 characters)
        <input type="password" name="password" autocomplete="new-password" required>
      </label>
      <label>Confirm password
        <input type="password" name="confirm" autocomplete="new-password" required>
      </label>
      <button type="submit">Sign up</button>
    </form>
    <p class="muted">Already registered? <a href="${pageContext.request.contextPath}/login">Login</a></p>
  </main>

  <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
