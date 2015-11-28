var createTodoButton = {
    view: "button",
    tooltip: "Add todo",
    type: "icon",
    icon: "plus",
    width: 30,
    popup: {
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
                view: "button",
                type: "form",
                value: "Create",
                inputWidth: 80,
                align: "right"
            }
            ],
            rules: {
                "name": webix.rules.isNotEmpty
            }
        }
    }
};

var editTodoButton = webix.copy(createTodoButton);
editTodoButton.tooltip = "Edit todo";
editTodoButton.icon = "edit";
editTodoButton.popup.body.elements[2].value = "Update";

var deleteTodoButton = {
    view: "button",
    tooltip: "Delete todo",
    type: "icon",
    icon: "remove",
    width: 30
};

var createTaskButton = webix.copy(createTodoButton);
createTaskButton.tooltip = "Add task";

var editTaskButton = webix.copy(editTodoButton);
editTaskButton.tooltip = "Edit task";

var deleteTaskButton = webix.copy(deleteTodoButton);
deleteTaskButton.tooltip = "Delete task";

var createCategoryButton = webix.copy(createTodoButton);
createCategoryButton.popup.body.id = "createCategoryForm";
createCategoryButton.popup.body.elements = [{
    view: "text",
    name: "name",
    label: "Name",
    labelPosition: "top",
    invalidMessage: "Name can not be empty"
}, {
    view: "button",
    type: "form",
    value: "Create",
    inputWidth: 80,
    align: "right"
}
];

var editCategoryButton = webix.copy(createCategoryButton);
editCategoryButton.tooltip = "Rename category";
editCategoryButton.icon = "edit";
editCategoryButton.popup.body.id = "editCategoryForm";
editCategoryButton.popup.body.elements[1].value = "Update";

var deleteCategoryButton = webix.copy(deleteTodoButton);
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
    }
};

admin_ui.init();