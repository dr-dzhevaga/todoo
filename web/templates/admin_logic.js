var processPopup = function (button) {
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
    var category = processPopup(this);
    if (category) {
        webix.ajax().header({"Content-Type": "application/x-www-form-urlencoded; charset=utf-8"}).
        post("/category", category, {
            error: function (text, data) {
                webix.message(data.json().message);
            },
            success: function (text, data) {
                var categoryRichSelect = $$("categoryRichSelect");
                var categoryList = categoryRichSelect.getList();
                categoryList.add(data.json().data);
            }
        });
    }
};

var onDeleteCategoryButton = function () {
    var categoryRichSelect = $$("categoryRichSelect");
    var categoryList = categoryRichSelect.getList();
    var category = categoryList.getSelectedItem();
    if (category.filter === "category") {
        webix.ajax().del("/category/" + category.id, {
            error: function (text, data) {
                webix.message(data.json().message);
            },
            success: function () {
                categoryList.remove(category.id);
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
            onItemClick: onDeleteCategoryButton
        };
    }
};

admin_logic.attachEvents();