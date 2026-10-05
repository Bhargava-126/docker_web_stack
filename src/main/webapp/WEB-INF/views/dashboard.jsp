<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard | Docker Web Stack</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <header class="topbar">
    <span class="brand">DEVOPS<small>docker project</small></span>
    <nav>
      <form method="post" action="${pageContext.request.contextPath}/logout" class="inline">
        <button type="submit" class="link">Logout</button>
      </form>
    </nav>
  </header>

  <main class="card wide">
    <h1>Welcome, ${username}!</h1>
    <p>You are logged in. Your session is stored in the database, so it works no matter which replica serves you.</p>
    <ul class="stats">
      <li><span>Served by container</span><strong>${host}</strong></li>
      <li><span>Session expires in</span><strong id="countdown" data-seconds="${ttl}">--:--</strong></li>
    </ul>
    <p class="muted">Refresh a few times: the container ID changes (Swarm load balancing) but you stay logged in.</p>
  </main>

  <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
