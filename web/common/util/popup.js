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
