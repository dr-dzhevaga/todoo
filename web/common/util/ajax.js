var ajax = {
    postJson: function (url, obj, onSuccess) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        post(url, JSON.stringify(obj), {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text, data) {
                ajax.showError(data);
            }
        })
    },
    putJson: function (url, obj, onSuccess) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        put(url, JSON.stringify(obj), {
            success: function (text, data) {
                onSuccess(data);
            },
            error: function (text, data) {
                ajax.showError(data);
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
                ajax.showError(data);
            }
        });
    },
    showError: function (data) {
        webix.message({type: "error", text: data.json().message, expire: 10000});
    }
};