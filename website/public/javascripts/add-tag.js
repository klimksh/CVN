/**
 * Created by D Mak on 13.12.13.
 */
$("#tags-input").keyup(function (event) {

    if (event.which == 188 && $(this).val()) {
        var newTag = $(this).val().replace(",", "");
        $("#tags-list").append('<span name="' + newTag + '" class="label label-warning" style="font-size:14px; margin:5px">' + newTag + '  <span id="delete-tag" style="padding:3px; font-size: 16px">&times;</span></span>');
        var tags = $("#tags").val();
        tags += newTag;
        $("#tags").val(tags);

        console.log($("#tags").val());
        $(this).val("");
    }
})

$("#tags-list").on("mouseover", "#delete-tag",function () {
    $(this).css("color", "#196A75");
    $("body").css("cursor", "pointer");
}).on("mouseout", "#delete-tag",function () {
        $(this).css("color", "white");
        $("body").css("cursor", "default");
    }).on("click", "#delete-tag", function () {
        $(this).parent().remove();
        var tagText = $(this).parent().attr("name");
        var tags = $("#tags").val().replace("" + tagText + "; ", "");
    })

