var createTodoButton = {
    id: "createTemplateButton",
    view: "button",
    type: "icon",
    icon: "plus",
    tooltip: "Create todo",
    align: "left",
    width: 30
};

var createTodoPopup = {
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
            id: "createTodoConfirmButton",
            view: "button",
            type: "form",
            value: "Create",
            inputWidth: 80,
            align: "right"
        }],
        rules: {
            "name": webix.rules.isNotEmpty
        }
    }
};

var deleteTodoButton = webix.copy(createTodoButton);
deleteTodoButton.id = "deleteTodoButton";
deleteTodoButton.icon = "remove";
deleteTodoButton.tooltip = "Delete todo";

var todoList = {
    id: "todoList",
    view: "list",
    template: "#name#<div class='created'>created: #created#</div>",
    defaultData: {
        description: ""
    },
    type: {
        height: 62
    },
    data: listData,
    select: true
};

var todoName = {
    id: "todoName",
    view: "text",
    readonly: true
};

var todoDescription = {
    id: "todoDescription",
    view: "textarea",
    height: 60,
    readonly: true
};

var createTaskButton = webix.copy(createTodoButton);
createTaskButton.id = "createTaskButton";
createTaskButton.tooltip = "Create task";

var createTaskPopup = webix.copy(createTodoPopup);
createTaskPopup.id = "createTaskPopup";
createTaskPopup.body.elements[createTaskPopup.body.elements.length - 1].id = "createTaskConfirmButton";

var tasksTree = {
    id: "tasksTree",
    view: "tree",
    css: "selected",
    drag: "order",
    select: true,
    template: function (obj, common) {
        var content = "<span>";
        content += common.icon(obj, common) + common.checkbox(obj, common);
        if (obj.completed) {
            content += "<span class='completed'>";
        }
        content += obj.name;
        content += "<span class='description'>" + " " + obj.description + "</span>";
        if (obj.completed) {
            content += "</span>";
        }
        content += "<i class='delete fa fa-trash-o fa-fw'></i>";
        content += "</span>";
        return content;
    },
    data: treeData
};

var taskDescription = {
    id: "taskDescription",
    view: "textarea",
    readonly: true
};

var taskName = {
    id: "taskName",
    view: "text",
    readonly: true
};

var scheme = {
    view: "layout",
    cols: [{
        rows: [{
            cols: [
                createTodoButton,
                deleteTodoButton
            ]
        },
            todoList
        ],
        width: 250
    }, {
        rows: [
            todoName,
            todoDescription,
            createTaskButton,
            tasksTree
        ],
        width: "auto"
    }, {
        rows: [
            taskName,
            taskDescription
        ],
        width: 200
    }]
};

var ui = {
    init: function () {
        webix.ui(createTodoPopup);
        webix.ui(createTaskPopup);
        webix.ui(scheme);
    }
};