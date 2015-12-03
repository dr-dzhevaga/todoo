var onCreateCategoryButtonClick = function () {
    popup.show("createCategoryPopup", this);
};

var onCreateCategoryConfirmButtonClick = function () {
    var category = popup.getValue("createCategoryPopup");
    if (category) {
        ajax.postJson(CATEGORY_API_ENDPOINT, category, function (data) {
            $$("categoryRichSelect").getList().add(data.json().data);
        });
    }
};

var onDeleteCategoryButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category.filter === "category") {
        ajax.deleteId(CATEGORY_API_ENDPOINT, category.id, function () {
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
    ajax.putJson(CATEGORY_API_ENDPOINT, category, function () {
        $$("categoryRichSelect").getList().updateItem(category.id, category);
        $$("categoryRichSelect").refresh();
    });
};

var onCreateTemplateButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category && category.filter === "category") {
        $$("createTaskPopup").getBody().setValues({categoryId: category.id});
        popup.show("createTaskPopup", this);
    } else {
        webix.message("Select category first");
    }
};

var onCreateTemplateConfirmButtonClick = function () {
    var template = popup.getValue("createTaskPopup");
    if (template) {
        ajax.postJson(TEMPLATE_API_ENDPOINT, template, function (data) {
            $$("templateList").add(data.json().data);
        });
    }
};

var onDeleteTemplateButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    if (template) {
        ajax.deleteId(TEMPLATE_API_ENDPOINT, template.id, function () {
            $$("templateList").remove(template.id);
            $$("stepTree").clearAll();
        });
    }
};

var onEditTemplateButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    if (template) {
        popup.show("editTemplatePopup", this);
    }
};

var onEditTemplateConfirmButtonClick = function () {
    var template = popup.getValue("editTemplatePopup");
    ajax.putJson(TEMPLATE_API_ENDPOINT, template, function () {
        $$("templateList").updateItem(template.id, template);
        $$("templateList").refresh();
        $$("templateDescription").setValue(template.description);
    });
};

var onCreateStepButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    var step = $$("stepTree").getSelectedItem();
    var parent = step ? step : template;
    if (parent) {
        $$("createStepPopup").getBody().setValues({parentId: parent.id, rootId: template.id});
        popup.show("createStepPopup", this);
    } else {
        webix.message("Select parent template first");
    }
};

var onCreateStepConfirmButtonClick = function () {
    var step = popup.getValue("createStepPopup");
    if (step) {
        ajax.postJson(TEMPLATE_API_ENDPOINT, step, function () {
            ajax.getJson(TEMPLATE_API_ENDPOINT, {filter: "parent", id: step.rootId}, function (text) {
                $$("stepTree").clearAll();
                $$("stepTree").parse(text);
            });
        });
    }
};

var onEditStepButtonClick = function () {
    var step = $$("stepTree").getSelectedItem();
    if (step) {
        popup.show("editStepPopup", this);
    } else {
        webix.message("Select step first");
    }
};

var onEditStepConfirmButtonClick = function () {
    var step = popup.getValue("editStepPopup");
    ajax.putJson(TEMPLATE_API_ENDPOINT, step, function () {
        $$("stepTree").updateItem(step.id, step);
        $$("stepTree").refresh();
    });
};

var onDeleteStepButtonClick = function () {
    var step = $$("stepTree").getSelectedItem();
    if (step) {
        ajax.deleteId(TEMPLATE_API_ENDPOINT, step.id, function () {
            $$("stepTree").remove(step.id);
        });
    }
};

var onStepTreeAfterDrop = function (context) {
    var source = this.getItem(context.source);
    var target = this.getItem(context.target);
    source.order = target.order;
    source.parentId = target.parentId;
    ajax.putJson(TEMPLATE_API_ENDPOINT, source, function () {
        var template = $$("templateList").getSelectedItem();
        uiComponent.reload("stepTree", {filter: "parent", id: template.id}, TEMPLATE_API_ENDPOINT);
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
        $$("editTemplatePopup").getBody().bind($$("templateList"));
        $$("deleteTemplateButton").attachEvent("onItemClick", onDeleteTemplateButtonClick);

        $$("createStepButton").attachEvent("onItemClick", onCreateStepButtonClick);
        $$("createStepConfirmButton").attachEvent("onItemClick", onCreateStepConfirmButtonClick);
        $$("editStepButton").attachEvent("onItemClick", onEditStepButtonClick);
        $$("editStepConfirmButton").attachEvent("onItemClick", onEditStepConfirmButtonClick);
        $$("editStepPopup").getBody().bind($$("stepTree"));
        $$("deleteStepButton").attachEvent("onItemClick", onDeleteStepButtonClick);
        $$("stepTree").attachEvent("onAfterDrop", onStepTreeAfterDrop);
    }
};