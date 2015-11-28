var processPopupForm = function (form) {
    if (form.validate()) {
        var values = form.getValues();
        form.clear();
        var popup = form.getParentView();
        popup.hide();
        return values;
    }
};

var onCreateCategoryConfirmButtonClick = function () {
    var category = processPopupForm($$("createCategoryForm"));
    if (category) {
        webix.ajax().
        header({"Content-Type": "application/json;charset=utf-8"}).
        post("/category", JSON.stringify(category), {
            success: function (text, data) {
                $$("categoryRichSelect").getList().add(data.json().data);
            },
            error: function (text, data) {
                webix.message(data.json().message);
            }
        });
    }
};

var onDeleteCategoryButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category.filter === "category") {
        webix.ajax().
        del("/category/" + category.id, {
            success: function () {
                $$("categoryRichSelect").getList().remove(category.id);
            },
            error: function (text, data) {
                webix.message(data.json().message);
            }
        });
    }
};

var onEditCategoryButtonClick = function () {
    $$("editCategoryForm").setValues($$("categoryRichSelect").getList().getSelectedItem());
};

var onEditCategoryConfirmButtonClick = function () {
    var category = processPopupForm($$("editCategoryForm"));
    if (category.filter === "category") {
        webix.ajax().
        header({"Content-Type": " application/json;charset=utf-8"}).
        put("/category", JSON.stringify(category), {
            success: function () {
                $$("categoryRichSelect").getList().updateItem(category.id, category);
                $$("categoryRichSelect").refresh();
            },
            error: function (text, data) {
                webix.message(data.json().message);
            }
        });
    }
};

var admin_logic = {
    attachEvents: function () {
        createCategoryButton.popup.body.elements[1].on = {
            onItemClick: onCreateCategoryConfirmButtonClick
        };
        deleteCategoryButton.on = {
            onItemClick: onDeleteCategoryButtonClick
        };
        editCategoryButton.popup.body.elements[1].on = {
            onItemClick: onEditCategoryConfirmButtonClick
        };
        editCategoryButton.on = {
            onItemClick: onEditCategoryButtonClick
        };
    }
};

admin_logic.attachEvents();