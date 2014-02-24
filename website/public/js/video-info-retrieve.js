/**
 * Created by D Mak on 06.12.13.
 */
function video_id_parser(link){
    var match = link.match(/^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/);   
    if (match&&match[2].length==11){
        return match[2];
    }else{
        return false;
    }
}

$(document).ready(function () {
    $("#video-link").keyup(function () {
        if (video_id_parser($(this).val())){
            
            $("#video-link-group").addClass("has-success");
            $("#video-link-group").removeClass("has-error");
            $("#parse_status").addClass("glyphicon-ok ");
            $("#parse_status").removeClass("glyphicon-remove");
        } 
        else {
            $("#video-link-group").removeClass("has-success");
            $("#video-link-group").addClass("has-error");
            $("#parse_status").addClass("glyphicon-remove");
            $("#parse_status").removeClass("glyphicon-ok");
        }
    }); 
    $("#video-link").change(function () {
        var link = video_id_parser($(this).val());
        var request_link = "https://gdata.youtube.com/feeds/api/videos/" + link + "?v=2";
        $("#video").append('<img src="/public/img/loading.gif"/>');
        $.ajax({
            type: "GET",
            url: request_link,
            dataType: "xml",
            success: function (data) {
                $xml = $(data);
                var title = $xml.find("title");
                var title = $xml.find("title")[0]; //there is a list of titles , rewrite query?
                $("#video-title").val(title.textContent);


                var desc = $xml.find("media\\:description, description");
                $("#video-description").val(desc.text());
                $("#video-description").prop('disabled', false);

                var tags = $xml.find("media\\:keywords, keywords");
                $("#video-tags").val(tags.text());
                $("#video-tags").prop('disabled', false);

                var thumbnail = $xml.find("media\\:thumbnail[yt\\:name='hqdefault'], thumbnail").attr("url");
                $("#video-thumbnail").val(thumbnail);

                $("#video-id").val(link);

                $("#loading").remove();


                var player;
                player = new YT.Player('player', {
                    height: '360',
                    width: '480',
                    videoId: '' + link + '',
                    events: { }
                });


            },
            statusCode: {
                400: function() {
                    $.get(add_video_url, function( data ) {
                        $( "#addVideo" ).html( data );
                    });
                }
            }
        })
        
    })


    $("#video-save").click(function(){

    })
});

$(function(){
    $('.truncate').succinct({
        size: 120
    });
    
});

