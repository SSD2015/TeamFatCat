$ ->
  $.get "/users", (users) ->
    $.each users, (index, user) ->
      @obj = $("<tr></tr>")
      @obj.append $('<td>').text user.id
      @obj.append $('<td>').text user.username
      @obj.append $('<td>').text user.password
      @obj.append $('<td>').text user.firstName
      @obj.append $('<td>').text user.lastName
      @obj.append $('<td>').text user.major
      @obj.append $('<td>').text user.year
      @obj.append $('<td>').text user.team

      $('#usersList').append $(@obj)