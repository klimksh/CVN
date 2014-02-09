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

        var link = video_id_parser($(this).val());
        if (link){
            var request_link = "https://gdata.youtube.com/feeds/api/videos/" + link + "?v=2";
            $("#video").append('<img src="/public/img/loading.gif"/>');
            $.ajax({
                type: "GET",
                url: request_link,
                dataType: "xml",
                success: function (data) {
                    $xml = $(data);
                    var title = $xml.find("title");
                    $("#video-title").val(title.text());
                    $("#video-title").prop('disabled', false);

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
                        $("#video-link-group").addClass("has-error");
                    }
                }
            })
        }
        else {
            $(".modal-body form input:text").each(function(){this.value = ""; if (this.id != "video-link"){this.disabled=true;}});
            $(".modal-body form textarea").each(function(){this.value = "";this.disabled=true;});
            $("#video-info").empty();
            $("#video-info").append("<div id='player'></div>");
            
        }
    })


    $("#video-save").click(function(){

    })
});

$(function(){
    $('.truncate').succinct({
        size: 120
    });
    
});

