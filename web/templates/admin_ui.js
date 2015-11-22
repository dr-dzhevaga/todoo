var deleteCategoryButton = {
    view: "button",
    tooltip: "Delete category",
    type: "icon",
    icon: "remove",
    width: 30
};

var addCategoryButton = {
    view: "button",
    tooltip: "Add category",
    type: "icon",
    icon: "plus",
    width: 30
};

var editCategoryButton = {
    view: "button",
    tooltip: "Rename category",
    type: "icon",
    icon: "edit",
    width: 30
};

var deleteTodoButton = webix.copy(deleteCategoryButton);
deleteTodoButton.tooltip = "Delete todo";

var addTodoButton = webix.copy(addCategoryButton);
addTodoButton.tooltip = "Add todo";

var editTodoButton = webix.copy(editCategoryButton);
editTodoButton.tooltip = "Edit todo";

var deleteTaskButton = webix.copy(deleteCategoryButton);
deleteTodoButton.tooltip = "Delete task";

var addTaskButton = webix.copy(addCategoryButton);
addTodoButton.tooltip = "Add task";

var editTaskButton = webix.copy(editCategoryButton);
editTodoButton.tooltip = "Edit task";

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
taskLabel.label = "|";

var adminToolBar = {
    view: "toolbar",
    cols: [
        categoryLabel,
        addCategoryButton,
        deleteCategoryButton,
        editCategoryButton,
        separatorLabel,
        todoLabel,
        addTodoButton,
        deleteTodoButton,
        editTodoButton,
        separatorLabel,
        taskLabel,
        addTaskButton,
        deleteTaskButton,
        editTaskButton
    ]
};

var admin_ui = {
    init: function () {
        scheme.rows.splice(1, 0, adminToolBar);
        tasksTree.drag = "order";
    }
};

admin_ui.init();