var ajax_util = {
    getJson: function (url, queryParameters, onSuccess) {
        webix.ajax(url, queryParameters, {
            success: function (text) {
                onSuccess(text);
            },
            error: function (text) {
                ajax_util.showError(text);
            }
        });
    },
    postObject: function (url, object, onSuccess) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        post(url, JSON.stringify(object), {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text) {
                ajax_util.showError(text);
            }
        })
    },
    putObject: function (url, object, onSuccess) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        put(url, JSON.stringify(object), {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text) {
                ajax_util.showError(text);
            }
        })
    },
    deleteId: function (url, id, onSuccess) {
        webix.ajax().
        del(url + "/" + id, {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text) {
                ajax_util.showError(text);
            }
        });
    },
    showError: function (text) {
        webix.message({type: "error", text: text, expire: 10000});
    }
};