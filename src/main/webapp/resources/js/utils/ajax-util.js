var ajax_util = {
    get: function (url, parameters, onSuccess, onError) {
        webix.ajax(url, parameters, {
            success: function (text, data, ajax) {
                onSuccess(text, data, ajax);
            },
            error: function (text, data, ajax) {
                ajax_util.onError(text, data, ajax, onError);
            }
        });
    },
    post: function (url, parameters, onSuccess, onError) {
        webix.ajax().
        post(url, parameters, {
            success: function (text, data, ajax) {
                onSuccess(text, data, ajax);
            },
            error: function (text, data, ajax) {
                ajax_util.onError(text, data, ajax, onError);
            }
        })
    },
    postJson: function (url, object, onSuccess, onError) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        post(url, JSON.stringify(object), {
            success: function (text, data, ajax) {
                onSuccess(text, data, ajax);
            },
            error: function (text, data, ajax) {
                ajax_util.onError(text, data, ajax, onError);
            }
        })
    },
    putJson: function (url, object, onSuccess, onError) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
        put(url, JSON.stringify(object), {
            success: function (text, data, ajax) {
                onSuccess(text, data, ajax);
            },
            error: function (text, data, ajax) {
                ajax_util.onError(text, data, ajax, onError);
            }
        })
    },
    deleteId: function (url, id, onSuccess, onError) {
        webix.ajax().
        del(url + "/" + id, {
            success: function (text, data, ajax) {
                onSuccess(text, data, ajax);
            },
            error: function (text, data, ajax) {
                ajax_util.onError(text, data, ajax, onError);
            }
        });
    },
    onError: function(text, data, ajax, onError) {
        if(onError) {
            if(onError(text, data, ajax)) {
                return;
            }
        }
        webix.message({type: "error", text: text, expire: 10000});
    }
};