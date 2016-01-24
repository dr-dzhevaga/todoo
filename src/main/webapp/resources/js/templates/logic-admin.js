var onCreateCategoryButtonClick = function () {
    popupHelper.show("createCategoryPopup", this);
};

var onCreateCategoryConfirmButtonClick = function () {
    var category = popupHelper.getValue("createCategoryPopup");
    if (category) {
        ajax_util.postJson(CATEGORY_API_ENDPOINT, category, function (text, data) {
            $$("categoryRichSelect").getList().add(data.json());
        });
    }
};

var onDeleteCategoryButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category.filter === "category") {
        ajax_util.deleteId(CATEGORY_API_ENDPOINT, category.id, function () {
            $$("categoryRichSelect").getList().remove(category.id);
        });
    }
};

var onEditCategoryButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category.filter === "category") {
        popupHelper.show("editCategoryPopup", this);
    }
};

var onEditCategoryConfirmButtonClick = function () {
    var category = popupHelper.getValue("editCategoryPopup");
    if(category) {
        ajax_util.putJson(CATEGORY_API_ENDPOINT, category, function () {
            $$("categoryRichSelect").getList().updateItem(category.id, category);
            $$("categoryRichSelect").refresh();
        });
    }
};

var onCreateTemplateButtonClick = function () {
    var category = $$("categoryRichSelect").getList().getSelectedItem();
    if (category && category.filter === "category") {
        $$("createTaskPopup").getBody().setValues({categoryId: category.id});
        popupHelper.show("createTaskPopup", this);
    } else {
        webix.message("Select category first");
    }
};

var onCreateTemplateConfirmButtonClick = function () {
    var template = popupHelper.getValue("createTaskPopup");
    if (template) {
        ajax_util.postJson(TEMPLATE_API_ENDPOINT, template, function (text, data) {
            $$("templateList").add(data.json());
        });
    }
};

var onDeleteTemplateButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    if (template) {
        ajax_util.deleteId(TEMPLATE_API_ENDPOINT, template.id, function () {
            $$("templateList").remove(template.id);
            dataStoreHelper.setValueSilently("templateDescription");
            $$("stepTree").clearAll();
        });
    }
};

var onEditTemplateButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    if (template) {
        popupHelper.show("editTemplatePopup", this);
    }
};

var onEditTemplateConfirmButtonClick = function () {
    var template = popupHelper.getValue("editTemplatePopup");
    if(template) {
        ajax_util.putJson(TEMPLATE_API_ENDPOINT, template, function () {
            $$("templateList").updateItem(template.id, template);
            $$("templateList").refresh();
            $$("templateDescription").setValue(template.description);
        });
    }
};

var onCreateStepButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    var step = $$("stepTree").getSelectedItem();
    var parent = step ? step : template;
    if (parent) {
        $$("createStepPopup").getBody().setValues({parentId: parent.id, templateId: template.id});
        popupHelper.show("createStepPopup", this);
    } else {
        webix.message("Select parent template first");
    }
};

var onCreateStepConfirmButtonClick = function () {
    var step = popupHelper.getValue("createStepPopup");
    if (step) {
        ajax_util.postJson(TEMPLATE_API_ENDPOINT, step, function () {
            ajax_util.get(TEMPLATE_API_ENDPOINT, {filter: "parent", id: step.templateId}, function (text) {
                $$("stepTree").clearAll();
                $$("stepTree").parse(text);
            });
        });
    }
};

var onCreateStepsFromTextButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    if (template) {
        $$("createStepsFromTextPopup").getBody().setValues({templateId: template.id, sourceType: "text"});
        popupHelper.show("createStepsFromTextPopup", this);
    } else {
        webix.message("Select parent template first");
    }
};

var onCreateStepFromTextConfirmButtonClick = function () {
    var parameters = popupHelper.getValue("createStepsFromTextPopup");
    if (parameters) {
        ajax_util.post(TEMPLATE_API_ENDPOINT, parameters, function () {
            ajax_util.get(TEMPLATE_API_ENDPOINT, {filter: "parent", id: parameters.templateId}, function (text) {
                $$("stepTree").clearAll();
                $$("stepTree").parse(text);
            });
        });
    }
};

var onEditStepButtonClick = function () {
    var step = $$("stepTree").getSelectedItem();
    if (step) {
        popupHelper.show("editStepPopup", this);
    } else {
        webix.message("Select step first");
    }
};

var onEditStepConfirmButtonClick = function () {
    var step = popupHelper.getValue("editStepPopup");
    if(step) {
        ajax_util.putJson(TEMPLATE_API_ENDPOINT, step, function () {
            $$("stepTree").updateItem(step.id, step);
            $$("stepTree").refresh();
        });
    }
};

var onDeleteStepButtonClick = function () {
    var step = $$("stepTree").getSelectedItem();
    if (step) {
        ajax_util.deleteId(TEMPLATE_API_ENDPOINT, step.id, function () {
            $$("stepTree").remove(step.id);
        });
    }
};

var onStepTreeAfterDrop = function (context) {
    var source = this.getItem(context.source);
    var target = this.getItem(context.target);
    source.order = target.order;
    source.parentId = target.parentId;
    ajax_util.putJson(TEMPLATE_API_ENDPOINT, source, function () {
        var template = $$("templateList").getSelectedItem();
        dataStoreHelper.reload("stepTree", {filter: "parent", id: template.id}, TEMPLATE_API_ENDPOINT);
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
        $$("createStepsFromTextButton").attachEvent("onItemClick", onCreateStepsFromTextButtonClick);
        $$("createStepsFromTextConfirmButton").attachEvent("onItemClick", onCreateStepFromTextConfirmButtonClick);
        $$("editStepButton").attachEvent("onItemClick", onEditStepButtonClick);
        $$("editStepConfirmButton").attachEvent("onItemClick", onEditStepConfirmButtonClick);
        $$("editStepPopup").getBody().bind($$("stepTree"));
        $$("deleteStepButton").attachEvent("onItemClick", onDeleteStepButtonClick);
        $$("stepTree").attachEvent("onAfterDrop", onStepTreeAfterDrop);
    }
};