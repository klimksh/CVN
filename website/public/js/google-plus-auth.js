// var clientId = '564983320580-r29l8abnd6ldq9ugek8th5h4epk4u6al.apps.googleusercontent.com';
var clientId = google_auth_client_id;

var apiKey = 'AIzaSyAdjHPT5Pb7Nu56WJ_nlrMGOAgUAtKjiPM';
var scopes = "https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.me";
var image_url=imageURL();
var nameGPlus=getName().toString();

function handleClientLoad() {
    window.setTimeout(checkAuth,1);
}

function checkAuth() {
	console("i am checking here");
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: true}, handleAuthResult);
}

function handleAuthResult(authResult) {
    if (authResult && !authResult.error) 
        makeApiCall();
}

function login(){
    gapi.auth.authorize({client_id: clientId, scope: scopes, immediate: false}, handleAuthResult);
    $('#login_btn').hide();
    $('#profile_btn').show(); 
    return false;  
}

function logout(){
    document.cookie = "PLAY_SESSION=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "photo_url=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "name_plus=; expires=Thu, 01 Jan 1970 00:00:00 GMT";
    $.ajax({
            type: "GET",
            async: false,
            url: logout_url()
	})
	.done(function( msg ) {
        gapi.auth.signOut();
		redirect();
	});
       
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
            $('#authOps').show('slow');
            $('#gConnect').hide();
            $('#add-new-note').show();
            $('#userDetailss').show();
            $('#logout').show();
            $('#disconnect').hide();    
            $('#Gphoto').show();
            $('.dropdown').show();
            $('#profile').empty();
            if (profile.error) {
                $('#profile').prepend(profile.error);
                return false;
            }
            $('#personal_image').html($("<img src=\""+profile.image.url+"\" style=\"width: 20px; margin-bottom: 0px\" class=\"glyphicon glyphicon-user\"/>"));
            $('#personal_image2').html($("<img src=\""+profile.image.url+"\" style=\"width: 25px; margin-bottom: 0px\" class=\"glyphicon glyphicon-user\"/>"));
            $('#personal_name').html(profile.displayName);           
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
          if (c.indexOf("userEmail")!=-1) 
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
        $('#login_btn').hide();
        $('#profile_btn').show();  
        gapi.client.load('plus', 'v1', function() {
            var request = gapi.client.plus.people.get({
                'userId': 'me'
            });
            request.execute(function(profile) {
                $('#personal_name').html(nameGPlus); 
                $('#personal_image2').html($("<img src=\""+image_url+"\" style=\"width: 25px; margin-bottom: 0px\" class=\" img-circle\"/>"));
                $('#personal_image').html($("<span class=\" glyphicon glyphicon-user\"/></span>"));
            });
        });
             
    }
    else {
    	$('#login_btn').show();
    	$('#profile_btn').hide();
    }
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