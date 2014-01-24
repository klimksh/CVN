var clientId = '201851680458-sumhf8v1qua2s1bn33dk9o8q34b0kvav';
var apiKey = 'AIzaSyAdjHPT5Pb7Nu56WJ_nlrMGOAgUAtKjiPM';
var scopes = "https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.me";
var image_url=imageURL();
var nameGPlus=getName().toString();
function handleClientLoad() {
    window.setTimeout(checkAuth,1);
}
function checkAuth() {
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: true}, handleAuthResult);
}

function handleAuthResult(authResult) {
    if (authResult && !authResult.error) 
        makeApiCall();
}

function login()
    {
        gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
        $('#authOps').show('slow');
        $('#gConnect').hide();
        $('#userDetailss').show();
        $('#logout').show();
        $('#disconnect').hide();    
        $('#Gphoto').show();
        return false;  
    }
function logout()
    {
        document.cookie = "PLAY_SESSION=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        document.cookie = "photo_url=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        document.cookie = "name_plus=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
        $.get(logout_url(), function () {
                gapi.auth.signOut();
        });
        gapi.auth.signOut();
        window.setInterval( function() {redirect(); } ,500);
    }
function redirect(){
    window.location.replace(index_url);
}
function handleAuthClick(event) {
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
    return false;
}
function makeApiCall() {
    gapi.client.load('plus', 'v1', function() {
        var request = gapi.client.plus.people.get({
            'userId': 'me'
        });
        request.execute(function(profile) {
            var f = profile.emails[0].value;
            $.get(login_url({email: f, id: profile.id, displayName: profile.displayName}), function () {});
            document.cookie="photo_url="+profile.image.url+";"
            document.cookie="name_plus=\""+profile.displayName+"\";"
            $('#profile').empty();
            if (profile.error) {
                $('#profile').prepend(profile.error);
                return;
            }
            $('#Gphoto').prepend($('<img src=\"'+ profile.image.url+'\" style=\"width: 50px; margin-bottom: 5px\" class=\"img-circle\"/>'));
            $('#nameButton').html(profile.displayName);           
        });
    });
}
window.onload=function(){
    var exist=false;
    var ss=document.cookie;
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
      var c = ca[i];
      if (c.indexOf("PLAY")!=-1) 
          if (c.indexOf("email")!=-1) 
             exist=true;
    }
    var isHeLogedON=true; 
    sessionParams = { 'client_id': clientId,  'session_state': null};
    gapi.auth.checkSessionState(sessionParams, function(stateMatched) { 
    if (stateMatched == true) { 
        isHeLogedON=false;
          document.cookie = "PLAY_SESSION=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
          document.cookie = "photo_url=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
          document.cookie = "name_plus=; expires=Thu, 01 Jan 1970 00:00:00 GMT";                        
    } 
});
if(exist  && isHeLogedON) {
    $('#authOps').show('slow');
    $('#gConnect').hide();
    $('#userDetailss').show();
    $('#logout').show();
    $('#disconnect').hide();    
    $('#Gphoto').show();
       
    $('#profile').empty();
    gapi.client.load('plus', 'v1', function() {
        var request = gapi.client.plus.people.get({
            'userId': 'me'
        });
        request.execute(function(profile) {
            $('#profile').empty();
            $('#Gphoto').prepend($("<img src=\""+image_url+"\" style=\"width: 50px; margin-bottom: 5px\" class=\"img-circle\"/>"));
            $('#nameButton').html(nameGPlus); 
        });
    });
             
}
else
    $('#gConnect').show();
}
function imageURL(){
    var ss=document.cookie;
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        if (c.indexOf("photo_url")!=-1)
             return c.substring(10,c.length);
    }
    return "";    
}
function getName(){
    var ss=document.cookie;
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        if (c.indexOf("name_plus")!=-1)
             return c.substring(12,c.length-1);
    }
    return "";
}