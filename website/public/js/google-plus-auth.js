var clientId = google_auth_client_id;

var scopes = "https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.me";
var image_url = imageURL();
var nameGPlus = getName().toString();

function handleAuthResult(authResult) {
    if (authResult && !authResult.error) {
        callAPI();
    }
}

function login() {
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
}

function logout() {
    document.cookie = "PLAY_SESSION=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "photo_url=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "name_plus=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    $.ajax({
        type: "GET",
        async: false,
        url: logout_url()
    }).done(function () {
        gapi.auth.signOut();
        window.location.reload();
    });
}

function callAPI() {
    gapi.client.load('plus', 'v1', function () {
        var request = gapi.client.plus.people.get({
            'userId': 'me'
        });
        request.execute(function (profile) {
            var profileEmail = profile.emails[0].value;
            $.get(login_url({
                email: profileEmail,
                id: profile.id,
                displayName: profile.displayName
            }), function () {

            });
            document.cookie = "photo_url=" + profile.image.url + ";";
            document.cookie = "name_plus=\"" + profile.displayName + "\";";
            $('#login_btn').hide();
            $('#profile_btn').show();
            if (profile.error) {
                $('#profile').prepend(profile.error);
                return false;
            }
            $('#personal_image').html($("<img src=\"" + profile.image.url + "\" class=\"profileImage img-circle\"/>"));
            $('#personal_name').html(profile.displayName);
        });
    });
}

window.onload = function () {
    var userExist = false;
    var cookieInformations = document.cookie.split(';');
    for (var i = 0; i < cookieInformations.length; i++) {
        var information = cookieInformations[i];
        if (information.indexOf("PLAY") != -1)
            if (information.indexOf("userEmail") != -1)
                userExist = true;
    }
    var isLoggedIn = true;
    sessionParams = { 'client_id': clientId, 'session_state': null};
    gapi.auth.checkSessionState(sessionParams, function (stateMatched) {
        if (stateMatched == true) {
            isLoggedIn = false;
            document.cookie = "PLAY_SESSION=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
            document.cookie = "photo_url=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
            document.cookie = "name_plus=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        }
    });
    if (userExist && isLoggedIn) {
        $('#login_btn').hide();
        $('#profile_btn').show();
        gapi.client.load('plus', 'v1', function () {
            var request = gapi.client.plus.people.get({
                'userId': 'me'
            });
            request.execute(function () {
                $('#personal_image').html($("<img src=\"" + image_url + "\" class=\"img-circle profileImage\"/>"));
                $('#personal_name').html(nameGPlus);
            });
        });

    } else {
        $('#login_btn').show();
        $('#profile_btn').hide();
    }
}

function imageURL() {
    var cookieInformations = document.cookie.split(';');
    for (var i = 0; i < cookieInformations.length; i++) {
        var info = cookieInformations[i];
        if (info.indexOf("photo_url") != -1)
            return info.substring(10, info.length);
    }
    return "";
}

function getName() {
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        if (c.indexOf("name_plus") != -1)
            return c.substring(12, c.length - 1);
    }
    return "";
}