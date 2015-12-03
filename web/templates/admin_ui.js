var createTemplateButton = {
    id: "createTemplateButton",
    view: "button",
    tooltip: "Add template",
    type: "icon",
    icon: "plus",
    width: 30
};

var createTaskPopup = {
    id: "createTaskPopup",
    view: "popup",
    body: {
        view: "form",
        width: 300,
        elements: [{
            view: "text",
            name: "name",
            label: "Name",
            labelPosition: "top",
            invalidMessage: "Name can not be empty"
        }, {
            view: "textarea",
            name: "description",
            label: "Description",
            labelPosition: "top",
            height: 100
        }, {
            id: "createTemplateConfirmButton",
            view: "button",
            value: "Create",
            type: "form",
            inputWidth: 80,
            align: "right"
        }],
        rules: {
            "name": webix.rules.isNotEmpty
        }
    }
};

var editTemplateButton = webix.copy(createTemplateButton);
editTemplateButton.id = "editTemplateButton";
editTemplateButton.tooltip = "Edit template";
editTemplateButton.icon = "edit";

var editTemplatePopup = webix.copy(createTaskPopup);
editTemplatePopup.id = "editTemplatePopup";
editTemplatePopup.body.elements[editTemplatePopup.body.elements.length - 1].id = "editTemplateConfirmButton";
editTemplatePopup.body.elements[editTemplatePopup.body.elements.length - 1].value = "Update";

var deleteTemplateButton = webix.copy(createTemplateButton);
deleteTemplateButton.id = "deleteTemplateButton";
deleteTemplateButton.tooltip = "Delete template";
deleteTemplateButton.icon = "remove";

var createStepButton = webix.copy(createTemplateButton);
createStepButton.id = "createStepButton";
createStepButton.tooltip = "Add task";

var createStepPopup = webix.copy(createTaskPopup);
createStepPopup.id = "createStepPopup";
createStepPopup.body.elements[createStepPopup.body.elements.length - 1].id = "createStepConfirmButton";

var editStepButton = webix.copy(editTemplateButton);
editStepButton.id = "editStepButton";
editStepButton.tooltip = "Edit task";

var editStepPopup = webix.copy(editTemplatePopup);
editStepPopup.id = "editStepPopup";
editStepPopup.body.elements[editStepPopup.body.elements.length - 1].id = "editStepConfirmButton";

var deleteStepButton = webix.copy(deleteTemplateButton);
deleteStepButton.id = "deleteStepButton";
deleteStepButton.tooltip = "Delete task";

var createCategoryButton = webix.copy(createTemplateButton);
createCategoryButton.id = "createCategoryButton";
createCategoryButton.tooltip = "Add category";

var createCategoryPopup = webix.copy(createTaskPopup);
createCategoryPopup.id = "createCategoryPopup";
createCategoryPopup.body.elements = [{
    view: "text",
    name: "name",
    label: "Name",
    labelPosition: "top",
    invalidMessage: "Name can not be empty"
}, {
    id: "createCategoryConfirmButton",
    view: "button",
    type: "form",
    value: "Create",
    inputWidth: 80,
    align: "right"
}];

var editCategoryButton = webix.copy(editTemplateButton);
editCategoryButton.id = "editCategoryButton";
editCategoryButton.tooltip = "Edit category";

var editCategoryPopup = webix.copy(createCategoryPopup);
editCategoryPopup.id = "editCategoryPopup";
editCategoryPopup.body.elements[editCategoryPopup.body.elements.length - 1].id = "editCategoryConfirmButton";
editCategoryPopup.body.elements[editCategoryPopup.body.elements.length - 1].value = "Update";

var deleteCategoryButton = webix.copy(deleteTemplateButton);
deleteCategoryButton.id = "deleteCategoryButton";
deleteCategoryButton.tooltip = "Delete category";

var categoryLabel = {
    view: "label",
    label: "Category:",
    width: "auto"
};

var templateLabel = webix.copy(categoryLabel);
templateLabel.label = "Template:";

var stepLabel = webix.copy(categoryLabel);
stepLabel.label = "Step:";

var separatorLabel = webix.copy(categoryLabel);
separatorLabel.label = "|";

var adminToolBar = {
    view: "toolbar",
    cols: [
        categoryLabel,
        createCategoryButton,
        deleteCategoryButton,
        editCategoryButton,
        separatorLabel,
        templateLabel,
        createTemplateButton,
        deleteTemplateButton,
        editTemplateButton,
        separatorLabel,
        stepLabel,
        createStepButton,
        deleteStepButton,
        editStepButton
    ]
};

var admin_ui = {
    init: function () {
        scheme = {
            rows: [
                adminToolBar,
                scheme
            ]
        };
        stepTree.drag = true;
        stepTree.select = true;
        stepTree.css = "selected";
        webix.ui(createTaskPopup);
        webix.ui(editTemplatePopup);
        webix.ui(createStepPopup);
        webix.ui(editStepPopup);
        webix.ui(createCategoryPopup);
        webix.ui(editCategoryPopup);
    }
};