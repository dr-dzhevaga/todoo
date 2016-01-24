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

var templateToolbar = {
    view: "toolbar",
    cols: [
        {
            view: "label",
            label: "Templates:",
            width: "auto"
        },
        createTemplateButton,
        deleteTemplateButton,
        editTemplateButton
    ]
};

var createStepButton = webix.copy(createTemplateButton);
createStepButton.id = "createStepButton";
createStepButton.tooltip = "Add step";

var createStepPopup = webix.copy(createTaskPopup);
createStepPopup.id = "createStepPopup";
createStepPopup.body.elements[createStepPopup.body.elements.length - 1].id = "createStepConfirmButton";

var createStepsFromTextButton = webix.copy(createTemplateButton);
createStepsFromTextButton.id = "createStepsFromTextButton";
createStepsFromTextButton.tooltip = "Create steps from text";
createStepsFromTextButton.icon = "file-text-o";

var createStepsFromTextPopup = webix.copy(createTaskPopup);
createStepsFromTextPopup.id = "createStepsFromTextPopup";
createStepsFromTextPopup.body = {
    view: "form",
    width: 600,
    elements: [{
        view: "textarea",
        name: "text",
        label: "Steps",
        labelPosition: "top",
        height: 300
    }, {
        id: "createStepsFromTextConfirmButton",
        view: "button",
        value: "Create",
        type: "form",
        inputWidth: 80,
        align: "right"
    }],
    rules: {
        "text": webix.rules.isNotEmpty
    }
};

var editStepButton = webix.copy(editTemplateButton);
editStepButton.id = "editStepButton";
editStepButton.tooltip = "Edit step";

var editStepPopup = webix.copy(editTemplatePopup);
editStepPopup.id = "editStepPopup";
editStepPopup.body.elements[editStepPopup.body.elements.length - 1].id = "editStepConfirmButton";

var deleteStepButton = webix.copy(deleteTemplateButton);
deleteStepButton.id = "deleteStepButton";
deleteStepButton.tooltip = "Delete step";

var stepToolbar = {
    view: "toolbar",
    cols: [
        {
            view: "label",
            label: "Steps:",
            width: "auto"
        },
        createStepsFromTextButton,
        createStepButton,
        deleteStepButton,
        editStepButton
    ]
};

var createCategoryButton = webix.copy(createTemplateButton);
createCategoryButton.id = "createCategoryButton";
createCategoryButton.tooltip = "Add category";

var createCategoryPopup = webix.copy(createTaskPopup);
createCategoryPopup.id = "createCategoryPopup";
createCategoryPopup.body.elements[1] = {
    id: "createCategoryConfirmButton",
    view: "button",
    type: "form",
    value: "Create",
    inputWidth: 80,
    align: "right"
};
createCategoryPopup.body.elements.pop();

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

var categoryToolbar = {
    view: "toolbar",
    cols: [
        {
            view: "label",
            label: "Categories:",
            width: "auto"
        },
        createCategoryButton,
        deleteCategoryButton,
        editCategoryButton
    ]
};

var admin_ui = {
    init: function () {
        Array.prototype.insert = function (index, item) {
            this.splice(index, 0, item);
        };
        scheme.cols[0].rows.insert(0, categoryToolbar);
        scheme.cols[0].rows.insert(2, templateToolbar);
        scheme.cols[1].rows.insert(2, stepToolbar);
        stepTree.drag = true;
        stepTree.select = true;
        webix.ui(createTaskPopup);
        webix.ui(editTemplatePopup);
        webix.ui(createStepPopup);
        webix.ui(createStepsFromTextPopup);
        webix.ui(editStepPopup);
        webix.ui(createCategoryPopup);
        webix.ui(editCategoryPopup);
    }
};