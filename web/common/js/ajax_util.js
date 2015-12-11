var ajax_util = {
    getJson: function (url, parameters, onSuccess) {
        webix.ajax(url, parameters, {
            success: function (text) {
                onSuccess(text);
            },
            error: function (text, data) {
                ajax_util.showError(data);
            }
        });
    },
    postJson: function (url, content, onSuccess) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        post(url, JSON.stringify(content), {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text, data) {
                ajax_util.showError(data);
            }
        })
    },
    putJson: function (url, content, onSuccess) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        put(url, JSON.stringify(content), {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text, data) {
                ajax_util.showError(data);
            }
        })
    },
    deleteId: function (url, id, onSuccess) {
        webix.ajax().
        del(url + "/" + id, {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text, data) {
                ajax_util.showError(data);
            }
        });
    },
    showError: function (data) {
        webix.message({type: "error", text: data.json().message, expire: 10000});
    }
};