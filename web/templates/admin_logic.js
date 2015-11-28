var getUIComponent = function (id) {
    return $$(id);
};

var getUIComponentList = function (id) {
    return getUIComponent(id).getList();
};

var getSelectedItem = function (id) {
    return getUIComponentList(id).getSelectedItem();
};

var getValueFromButtonForm = function (button) {
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
    var category = getValueFromButtonForm(this);
    if (category) {
        webix.ajax().
        header({"Content-Type": "application/json;charset=utf-8"}).
        post("/category", JSON.stringify(category), {
            success: function (text, data) {
                getUIComponentList("categoryRichSelect").add(data.json().data);
            },
            error: function (text, data) {
                webix.message(data.json().message);
            }
        });
    }
};

var onDeleteCategoryButtonClick = function () {
    var category = getSelectedItem("categoryRichSelect");
    if (category.filter === "category") {
        webix.ajax().
        del("/category/" + category.id, {
            success: function () {
                getUIComponentList("categoryRichSelect").remove(category.id);
            },
            error: function (text, data) {
                webix.message(data.json().message);
            }
        });
    }
};

var onEditCategoryButtonClick = function () {
    getUIComponent("categoryNameText").setValue(getSelectedItem("categoryRichSelect").name);
};

var onEditCategoryConfirmButtonClick = function () {
    var category = getSelectedItem("categoryRichSelect");
    var updatedCategory = getValueFromButtonForm(this);
    updatedCategory.id = category.id;
    if (category.filter === "category") {
        webix.ajax().
        header({"Content-Type": " application/json;charset=utf-8"}).
        put("/category", JSON.stringify(updatedCategory), {
            success: function () {
                category.name = updatedCategory.name;
                getUIComponent("categoryRichSelect").refresh();
                getUIComponentList("categoryRichSelect").refresh();
            },
            error: function (text, data) {
                webix.message(data.json().message);
            }
        });
    }
};

var admin_logic = {
    attachEvents: function () {
        addCategoryButton.popup.body.elements[1].on = {
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