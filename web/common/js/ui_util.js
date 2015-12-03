var popup = {
    show: function (id, parent) {
        $$(id).show(parent.$view);
        $$(id).getBody().focus();
    },
    getValue: function (id) {
        var form = $$(id).getBody();
        if (form.validate()) {
            $$(id).hide();
            var value = form.getValues();
            form.clear();
            return value;
        }
    }
};

var uiComponent = {
    reload: function (id, filter, url) {
        ajax.getJson(url, filter, function (text) {
            $$(id).clearAll();
            $$(id).parse(text);
        });
    },
    setValueSilently: function (id, value) {
        $$(id).blockEvent();
        $$(id).setValue(value || "");
        $$(id).unblockEvent();
    }
};

