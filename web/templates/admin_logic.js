var onCreateCategoryButtonClick = function () {
    popup.show("createCategoryPopup", this);
};

var onCreateCategoryConfirmButtonClick = function () {
    var category = popup.getValue("createCategoryPopup");
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
        popup.show("editCategoryPopup", this);
    }
};

var onEditCategoryConfirmButtonClick = function () {
    var category = popup.getValue("editCategoryPopup");
    ajax.putJson("/api/categories", category, function () {
        $$("categoryRichSelect").getList().updateItem(category.id, category);
        $$("categoryRichSelect").refresh();
    });
};

var onCreateTemplateButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category && category.filter === "category") {
        $$("createTemplatePopup").getBody().setValues({categoryId: category.id});
        popup.show("createTemplatePopup", this);
    } else {
        webix.message("Select category first");
    }
};

var onCreateTemplateConfirmButtonClick = function () {
    var template = popup.getValue("createTemplatePopup");
    if (template) {
        ajax.postJson("/api/templates", template, function (data) {
            $$("templatesList").add(data.json().data);
        });
    }
};

var onDeleteTemplateButtonClick = function () {
    var template = $$("templatesList").getSelectedItem();
    if (template) {
        ajax.deleteId("/api/templates", template.id, function () {
            $$("templatesList").remove(template.id);
        });
    }
};

var onEditTemplateButtonClick = function () {
    var template = $$("templatesList").getSelectedItem();
    if (template) {
        popup.show("editTemplatePopup", this);
    }
};

var onEditTemplateConfirmButtonClick = function () {
    var template = popup.getValue("editTemplatePopup");
    ajax.putJson("/api/templates", template, function () {
        $$("templatesList").updateItem(template.id, template);
        $$("templatesList").refresh();
        $$("templateDescription").setValue(template.description);
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

        $$("createTemplateButton").attachEvent("onItemClick", onCreateTemplateButtonClick);
        $$("createTemplateConfirmButton").attachEvent("onItemClick", onCreateTemplateConfirmButtonClick);
        $$("editTemplateButton").attachEvent("onItemClick", onEditTemplateButtonClick);
        $$("editTemplateConfirmButton").attachEvent("onItemClick", onEditTemplateConfirmButtonClick);
        $$("editTemplatePopup").getBody().bind($$("templatesList"));
        $$("deleteTemplateButton").attachEvent("onItemClick", onDeleteTemplateButtonClick);
    }
};