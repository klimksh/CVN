var helper = (function () {
    var BASE_API_PATH = 'plus/v1/';

    return {
            onSignInCallback: function (authResult) {
               
                $.get(login_token_url({a: authResult.access_token}), function () {
                });
                gapi.client.load('plus', 'v1', function () {
                    if (authResult['access_token']) {
                        $('#authOps').show('slow');
                        $('#gConnect').hide();
                        $('#userDetailss').show();
                        $('#logout').show();
                        $('#disconnect').hide();                                           
                        helper.profile();
                    } else if (authResult['error']) {
                        console.log(authResult['error']);
                        $('#authOps').hide('slow');
                        $('#gConnect').show();
                        console.log("error");
                        $('#logout').hide();
                        $('#userDetailss').hide();
                        $('#disconnect').hide();


                    }
                });
            },
            disconnect: function () {
                $.ajax({
                    type: 'GET',
                    url: 'https://accounts.google.com/o/oauth2/revoke?token=' +
                            gapi.auth.getToken().access_token,
                    async: false,
                    contentType: 'application/json',
                    dataType: 'jsonp',
                    success: function (result) {
                        $('#authOps').hide();
                        $('#profile').empty();
                        $('#visiblePeople').empty();
                        $('#authResult').empty();
                        $('#gConnect').show();
                        $('#userDetailss').hide();
                        $('#logout').hide();
                        $('#disconnect').hide();
                        
                        $.get(disconnect_account_url(), function () {
                        });
                    },
                    error: function (e) {
                    }
                });
            },

            profile: function () {
                var request = gapi.client.plus.people.get({'userId': 'me'});
                request.execute(function (profile) {
                    a = 1;
                    var f = profile.emails[0].value;
                    $.get(login_url({email: f, id: profile.id, displayName: profile.displayName}), function () {
                    });
                    $('#profile').empty();
                    if (profile.error) {
                        $('#profile').prepend(profile.error);
                        return;
                    }
                    $('#Gphoto').prepend(
                            
                            $('<img src=\"'+ profile.image.url+'\" style=\"width: 40px; margin-bottom: 5px\" class=\"img-circle\"/>')
                    );
                    $('#nameButton').html(profile.displayName);   
                   
                  
                });
            }
        };
    })();
    $(document).ready(function () {
        $('#disconnect').click(helper.disconnect);
        $('#logout').click(helper.logout);
        $('#logout2').click(function(){
            $('#gConnect').show();
            $('#userDetailss').hide();
            $("#Gphoto").css("display","none");
            gapi.auth.signOut();
            $.get(logout_url(), function () {
                    gapi.auth.signOut();
            });
            window.location.replace(index_url); 
        });
        $('#loaderror').hide();
        if ($('[data-clientid="201851680458-sumhf8v1qua2s1bn33dk9o8q34b0kvav.apps.googleusercontent.com"]').length > 0) {
            alert('This sample requires your OAuth credentials (client ID) ' +
                    'from the Google APIs console:\n' +
                    '    https://code.google.com/apis/console/#:access\n\n' +
                    'Find and replace YOUR_CLIENT_ID with your client ID.'
            );
        }
    });
    function onSignInCallback(authResult) {
        helper.onSignInCallback(authResult);

    }
