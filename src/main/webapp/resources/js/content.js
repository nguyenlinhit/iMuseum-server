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

    $("#submitbtn").click(function() {
        var link = $("#apiURL").val();
        var method = $("#apiMethod").val();
        if (method === "GET") {
            $.ajax({
                type: "GET",
                url: link,
                dataType: "json",
                success: function(data) {
                    var result = JSON.stringify(data, undefined, 4);
                    $(".api-result").html("");
                    $(".api-result").append($('<pre>').append(syntaxHighlight(result)));
                },
                error: function() {
                    alert("failed!!!   33");
                }
            });
        } else {
            var elem = document.getElementsByClassName("attrValue");
            var data = "{";
            var len = elem.length;
            for (var i = 0; i < len; i++) {
                data += '"' + $(elem[i]).attr('name') + '":"' + elem[i].value + '"';
                if (i + 1 != len) { data += ','; }
            }
            data += "}";
            
            $.ajax({
                type: "POST",
                url: link,
                //data: JSON.stringify({ Markers: data }),
                //data: JSON.stringify(data),
                traditional: true,
                data: data,
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                // statusCode: {
                //     200: function(response) {
                //         alert('11');
                //     },
                //     201: function(response) {
                //         alert('12');
                //     },
                //     400: function(response) {
                //         alert('13');
                //     },
                //     404: function(response) {
                //         alert('14');
                //     }
                // },
                success: function(data) {
                    // alert(request.getResponseHeader('some_header'));
                    var result = JSON.stringify(data, undefined, 4);
                    $(".api-result").html("");
                    $(".api-result").append($('<pre>').append(syntaxHighlight(result)));
                    alert("success");
                    
                },
                error: function(data) {
                    alert("fail!!! 123");
                    // alert(request.getResponseHeader('some_header'));
                    // var result = JSON.stringify(data, undefined, 4);
                    // $(".api-result").html("");
                    // $(".api-result").append($('<pre>').append(syntaxHighlight(result)));
                }
                
                
            });
        }
    });
    $("#submit").click(function() {
        var elem = document.getElementsByClassName("attrValue");
        var names = [];
        var data = "{";
        var len = elem.length;
        for (var i = 0; i < len; ++i) {
            data += '"' + $(elem[i]).attr('name') + '":' + elem[i].value;
            if (i + 1 != len) { data += ','; }
        }
        data += "}";
        alert(data);
    });