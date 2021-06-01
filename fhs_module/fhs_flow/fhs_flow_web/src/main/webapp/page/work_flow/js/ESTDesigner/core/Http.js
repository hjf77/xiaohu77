var Http = {
    post: function (url, data, success, error) {
        $.ajax({
            url: url,
            type: 'POST',
            data: data,
            dataType: 'json',
            error: error,
            success: success
        });
    },
    get: function (url,  success, error) {
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            error: error,
            success: success
        });
    }
}