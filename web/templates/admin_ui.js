var createTodoButton = {
    id: "createTodoButton",
    view: "button",
    tooltip: "Add todo",
    type: "icon",
    icon: "plus",
    width: 30
};

var createTodoPopup = {
    id: "createTodoPopup",
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
            id: "createTodoConfirmButton",
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

var editTodoButton = webix.copy(createTodoButton);
editTodoButton.id = "editTodoButton";
editTodoButton.tooltip = "Edit todo";
editTodoButton.icon = "edit";

var editTodoPopup = webix.copy(createTodoPopup);
editTodoPopup.id = "editTodoPopup";
editTodoPopup.body.elements[editTodoPopup.body.elements.length - 1].id = "editTodoConfirmButton";
editTodoPopup.body.elements[editTodoPopup.body.elements.length - 1].value = "Update";

var deleteTodoButton = webix.copy(createTodoButton);
deleteTodoButton.id = "deleteTodoButton";
deleteTodoButton.tooltip = "Delete todo";
deleteTodoButton.icon = "remove";

var createTaskButton = webix.copy(createTodoButton);
createTaskButton.id = "createTaskButton";
createTaskButton.tooltip = "Add task";

var createTaskPopup = webix.copy(createTodoPopup);
createTaskPopup.id = "createTaskPopup";
createTaskPopup.body.elements[createTaskPopup.body.elements.length - 1].id = "createTaskConfirmButton";

var editTaskButton = webix.copy(editTodoButton);
editTaskButton.id = "editTaskButton";
editTaskButton.tooltip = "Edit task";

var editTaskPopup = webix.copy(editTodoPopup);
editTaskPopup.id = "editTaskPopup";
editTaskPopup.body.elements[editTaskPopup.body.elements.length - 1].id = "editTaskConfirmButton";

var deleteTaskButton = webix.copy(deleteTodoButton);
deleteTaskButton.id = "deleteTaskButton";
deleteTaskButton.tooltip = "Delete task";

var createCategoryButton = webix.copy(createTodoButton);
createCategoryButton.id = "createCategoryButton";
createCategoryButton.tooltip = "Add category";

var createCategoryPopup = webix.copy(createTodoPopup);
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

var editCategoryButton = webix.copy(editTodoButton);
editCategoryButton.id = "editCategoryButton";
editCategoryButton.tooltip = "Edit category";

var editCategoryPopup = webix.copy(createCategoryPopup);
editCategoryPopup.id = "editCategoryPopup";
editCategoryPopup.body.elements[editCategoryPopup.body.elements.length - 1].id = "editCategoryConfirmButton";
editCategoryPopup.body.elements[editCategoryPopup.body.elements.length - 1].value = "Update";

var deleteCategoryButton = webix.copy(deleteTodoButton);
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
        createTodoButton,
        deleteTodoButton,
        editTodoButton,
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
        webix.ui(createTodoPopup);
        webix.ui(editTodoPopup);
        webix.ui(createTaskPopup);
        webix.ui(editTaskPopup);
        webix.ui(createCategoryPopup);
        webix.ui(editCategoryPopup);
    }
};