$(document).ready(function() {
    function syntaxHighlight(json) {
        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
            var cls = 'number';
            if (/^"/.test(match)) {
                if (/:$/.test(match)) {
                    cls = 'key';
                } else {
                    cls = 'string';
                }
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
    }

    $(".api-row").click(function() {
        var id = $(this).attr('class').split(" ")[1].split("-")[2];
        $.ajax({
            type: "GET",
            url: "http://192.168.69.207:8080/content/" + id,
            dataType: "html",
            success: function(data) {
                $(".result").html("");
                $(".result-" + id).append(data);
            },
            error: function() {
                alert("failed!!! 111");
            }
        });
    });
});