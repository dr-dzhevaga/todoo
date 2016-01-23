var popupHelper = {
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

var dataStoreHelper = {
    reload: function (id, filter, url) {
        ajax_util.get(url, filter, function (text) {
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

String.prototype.trunc = String.prototype.trunc || function (n) {
        return (this.length > n) ? this.substr(0, n - 1) + '&hellip;' : this;
    };