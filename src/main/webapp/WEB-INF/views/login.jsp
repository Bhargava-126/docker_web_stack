<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login | Docker Web Stack</title>
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
    <h1>Login</h1>
    <div class="alert error" ${empty error ? 'hidden' : ''}>${error}</div>
    <div class="alert ok" ${empty message ? 'hidden' : ''}>${message}</div>

    <form id="authForm" method="post" action="${pageContext.request.contextPath}/login" novalidate>
      <label>Username
        <input type="text" name="username" value="${username}" autocomplete="username" required>
      </label>
      <label>Password
        <input type="password" name="password" autocomplete="current-password" required>
      </label>
      <button type="submit">Login</button>
    </form>
    <p class="muted">No account? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
  </main>

  <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
