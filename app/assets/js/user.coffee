$ ->
  $.get "/users", (users) ->
    $.each users, (index, user) ->
      $('#userslist').append $('<li>').text user.firstName