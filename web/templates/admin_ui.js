var createTemplateButton = {
    id: "createTemplateButton",
    view: "button",
    tooltip: "Add template",
    type: "icon",
    icon: "plus",
    width: 30
};

var createTemplatePopup = {
    id: "createTemplatePopup",
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

var editTemplatePopup = webix.copy(createTemplatePopup);
editTemplatePopup.id = "editTemplatePopup";
editTemplatePopup.body.elements[editTemplatePopup.body.elements.length - 1].id = "editTemplateConfirmButton";
editTemplatePopup.body.elements[editTemplatePopup.body.elements.length - 1].value = "Update";

var deleteTemplateButton = webix.copy(createTemplateButton);
deleteTemplateButton.id = "deleteTemplateButton";
deleteTemplateButton.tooltip = "Delete template";
deleteTemplateButton.icon = "remove";

var createTaskButton = webix.copy(createTemplateButton);
createTaskButton.id = "createTaskButton";
createTaskButton.tooltip = "Add task";

var createTaskPopup = webix.copy(createTemplatePopup);
createTaskPopup.id = "createTaskPopup";
createTaskPopup.body.elements[createTaskPopup.body.elements.length - 1].id = "createTaskConfirmButton";

var editTaskButton = webix.copy(editTemplateButton);
editTaskButton.id = "editTaskButton";
editTaskButton.tooltip = "Edit task";

var editTaskPopup = webix.copy(editTemplatePopup);
editTaskPopup.id = "editTaskPopup";
editTaskPopup.body.elements[editTaskPopup.body.elements.length - 1].id = "editTaskConfirmButton";

var deleteTaskButton = webix.copy(deleteTemplateButton);
deleteTaskButton.id = "deleteTaskButton";
deleteTaskButton.tooltip = "Delete task";

var createCategoryButton = webix.copy(createTemplateButton);
createCategoryButton.id = "createCategoryButton";
createCategoryButton.tooltip = "Add category";

var createCategoryPopup = webix.copy(createTemplatePopup);
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

var todoLabel = webix.copy(categoryLabel);
todoLabel.label = "Todo:";

var taskLabel = webix.copy(categoryLabel);
taskLabel.label = "Task:";

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
        todoLabel,
        createTemplateButton,
        deleteTemplateButton,
        editTemplateButton,
        separatorLabel,
        taskLabel,
        createTaskButton,
        deleteTaskButton,
        editTaskButton
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
        tasksTree.drag = "order";
        webix.ui(createTemplatePopup);
        webix.ui(editTemplatePopup);
        webix.ui(createTaskPopup);
        webix.ui(editTaskPopup);
        webix.ui(createCategoryPopup);
        webix.ui(editCategoryPopup);
    }
};