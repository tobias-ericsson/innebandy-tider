<% include '/WEB-INF/includes/header.gtpl' %>

<p><%= request.header%></p>
<% request.games.each { game -> %>
    <p>${game}</p>
<% } %>
<p>total: <%= request.games.size()%></p>

<% include '/WEB-INF/includes/footer.gtpl' %>