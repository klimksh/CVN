/**
 * Created by D Mak on 06.12.13.
 */

$("#video-link").change(function () {
    var request_link = "https://gdata.youtube.com/feeds/api/videos/" + $(this).val() + "?v=2";
    var link = $(this).val();
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
            //console.log(thumbnail);
            //$("#video").css("background-image", "url(" + thumbnail+ ")");
            $("#loading").remove();


            console.log(link);
            var player;
            player = new YT.Player('player', {
                height: '360',
                width: '480',
                videoId: '' + link + '',
                events: { }
            });


        }
    })
})


$("#video-save").click(function(){

})
