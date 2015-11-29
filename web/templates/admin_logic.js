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

var onCreateCategoryButtonClick = function () {
    showPopup("createCategoryPopup", this);
};

var onCreateCategoryConfirmButtonClick = function () {
    var category = getPopupValue("createCategoryPopup");
    if (category) {
        ajax.postJson("/api/categories", category, function (data) {
            $$("categoryRichSelect").getList().add(data.json().data);
        });
    }
};

var onDeleteCategoryButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category.filter === "category") {
        ajax.deleteId("/api/categories", category.id, function () {
            $$("categoryRichSelect").getList().remove(category.id);
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
    ajax.putJson("/api/categories", category, function () {
        $$("categoryRichSelect").getList().updateItem(category.id, category);
        $$("categoryRichSelect").refresh();
    });
};

var onCreateTemplateButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category && category.filter === "category") {
        $$("createTemplatePopup").getBody().setValues({categoryId: category.id});
        showPopup("createTemplatePopup", this);
    } else {
        webix.message("Select category first");
    }
};

var onCreateTemplateConfirmButtonClick = function () {
    var template = getPopupValue("createTemplatePopup");
    if (template) {
        ajax.postJson("/api/templates", template, function (data) {
            $$("templatesList").add(data.json().data);
        });
    }
};

var admin_logic = {
    init: function () {
        $$("createCategoryButton").attachEvent("onItemClick", onCreateCategoryButtonClick);
        $$("createCategoryConfirmButton").attachEvent("onItemClick", onCreateCategoryConfirmButtonClick);
        $$("editCategoryButton").attachEvent("onItemClick", onEditCategoryButtonClick);
        $$("editCategoryConfirmButton").attachEvent("onItemClick", onEditCategoryConfirmButtonClick);
        $$("editCategoryPopup").getBody().bind($$("categoryRichSelect").getList());
        $$("deleteCategoryButton").attachEvent("onItemClick", onDeleteCategoryButtonClick);

        $$("createTemplateButton").attachEvent("onItemClick", onCreateTemplateButtonClick);
        $$("createTemplateConfirmButton").attachEvent("onItemClick", onCreateTemplateConfirmButtonClick);
    }
};