var getFormValues = function (button) {
    var form = button.getParentView();
    if (form.validate()) {
        var values = form.getValues();
        form.clear();
        var popup = form.getParentView();
        popup.hide();
        return values;
    }
};

var onCreateCategoryConfirmButtonClick = function () {
    var values = getFormValues(this);
    if (values) {
        webix.ajax().header({"Content-Type": "application/x-www-form-urlencoded; charset=utf-8"}).
        post("/category", values, {
            error: function (text, data) {
                webix.message(data.json().message);
            },
            success: function (text, data) {

            }
        });
    }
};

var admin_logic = {
    attachEvents: function () {
        addCategoryButton.popup.body.elements[1].on = {
            onItemClick: onCreateCategoryConfirmButtonClick
        };
    }
};

admin_logic.attachEvents();