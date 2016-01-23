var ajax_util = {
    get: function (url, parameters, onSuccess) {
        webix.ajax(url, parameters, {
            success: function (text) {
                onSuccess(text);
            },
            error: function (text) {
                ajax_util.showError(text);
            }
        });
    },
    post: function (url, parameters, onSuccess) {
        webix.ajax().
        post(url, parameters, {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text) {
                ajax_util.showError(text);
            }
        })
    },
    postJson: function (url, object, onSuccess) {
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
    putJson: function (url, object, onSuccess) {
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