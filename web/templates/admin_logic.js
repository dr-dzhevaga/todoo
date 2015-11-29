function showPopup(id, parent) {
    $$(id).show(parent.$view);
    $$(id).getBody().focus();
}

var getPopupValue = function (id) {
    var form = $$(id).getBody();
    if (form.validate()) {
        $$(id).hide();
        var value = form.getValues();
        form.clear();
        return value;
    }
};

var onCreateCategoryButtonClick = function () {
    showPopup("createCategoryPopup", this);
};

var onCreateCategoryConfirmButtonClick = function () {
    var category = getPopupValue("createCategoryPopup");
    if (category) {
        webix.ajax().
        header({
            "Content-Type": "application/json;charset=utf-8"
        }).
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
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category.filter === "category") {
        showPopup("editCategoryPopup", this);
    }
};

var onEditCategoryConfirmButtonClick = function () {
    var category = getPopupValue("editCategoryPopup");
    webix.ajax().
    header({
        "Content-Type": " application/json;charset=utf-8"
    }).
    put("/category", JSON.stringify(category), {
        success: function () {
            $$("categoryRichSelect").getList().updateItem(category.id, category);
            $$("categoryRichSelect").refresh();
        },
        error: function (text, data) {
            webix.message(data.json().message);
        }
    });
};

var admin_logic = {
    init: function () {
        $$("createCategoryButton").attachEvent("onItemClick", onCreateCategoryButtonClick);
        $$("createCategoryConfirmButton").attachEvent("onItemClick", onCreateCategoryConfirmButtonClick);
        $$("editCategoryButton").attachEvent("onItemClick", onEditCategoryButtonClick);
        $$("editCategoryConfirmButton").attachEvent("onItemClick", onEditCategoryConfirmButtonClick);
        $$("editCategoryPopup").getBody().bind($$("categoryRichSelect").getList());
        $$("deleteCategoryButton").attachEvent("onItemClick", onDeleteCategoryButtonClick);
    }
};