$(function(){
    $("#tagsInput").keyup(function (event) {
        if (event.which == 188 && $(this).val()) {
            var newTag = $(this).val().replace(",", "").trim();
            $("#tagsList").append('<span name="' + newTag + '" class="label label-danger" style="font-size:14px; margin:5px">' + newTag + '  <span class="deleteTag" style="padding:3px; font-size: 16px" >&times;</span></span>');
            var tags = $("#tags").val();
            tags += newTag+",";
            $("#tags").val(tags);
            $(this).val("");
            console.log(tags);
        }
    });

    $("#tagsList").on("click", ".deleteTag", function() {
        var tagElem = $(this).parent();
        var tags = $("#tags").val().replace(tagElem.attr("name") + ",", "");
        $("#tags").val(tags);
        tagElem.remove();
    });

});

function addTag(e, caller){
    if (e.which == 188 && caller.val()) {
        var newTag = caller.val().replace(",", "").trim();
        $("#tagsList").append('<span name="' + newTag + '" class="label label-danger" style="font-size:14px; margin:5px">' + newTag + '  <span class="deleteTag" style="padding:3px; font-size: 16px" onclick="deleteTag($(this))">&times;</span></span>');
        var tags = $("#tags").val();
        tags += newTag+",";
        $("#tags").val(tags);
        caller.val("");
    }
}

function deleteTag(caller){
    var tagElem = caller.parent();
    var tags = $("#tags").val().replace(tagElem.attr("name") + ",", "");
    $("#tags").val(tags);
    tagElem.remove();
}