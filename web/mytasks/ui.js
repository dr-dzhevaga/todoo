var createTaskButton = {
    id: "createTaskButton",
    view: "button",
    type: "icon",
    icon: "plus",
    tooltip: "Create todo",
    align: "left",
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
            id: "createTaskConfirmButton",
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

var deleteTaskButton = webix.copy(createTaskButton);
deleteTaskButton.id = "deleteTaskButton";
deleteTaskButton.icon = "remove";
deleteTaskButton.tooltip = "Delete todo";

var taskList = {
    id: "taskList",
    view: "list",
    template: "#name#<div class='created'>#created#</div>",
    type: {
        height: 50
    },
    url: TASK_API_ENDPOINT,
    select: true
};

var taskName = {
    id: "taskName",
    view: "text",
    readonly: true
};

var taskDescription = {
    id: "taskDescription",
    view: "textarea",
    height: 60,
    readonly: true
};

var createStepButton = webix.copy(createTaskButton);
createStepButton.id = "createStepButton";
createStepButton.tooltip = "Create task";

var createStepPopup = webix.copy(createTaskPopup);
createStepPopup.id = "createStepPopup";
createStepPopup.body.elements[createStepPopup.body.elements.length - 1].id = "createStepConfirmButton";

var stepTree = {
    id: "stepTree",
    view: "tree",
    css: "selected",
    drag: true,
    select: true,
    template: function (obj, common) {
        var content = common.icon(obj, common) + common.checkbox(obj, common);
        content += "<i class='delete fa fa-trash-o fa-fw'></i>";
        content += "<div class='step'>";
        if (obj.completed) {
            content += "<span class='completed'>";
        }
        content += obj.name;
        content += "<span class='description'>" + obj.description + "</span>";
        if (obj.completed) {
            content += "</span>";
        }
        content += "</div>";
        return content;
    }
};

var stepDescription = {
    id: "stepDescription",
    view: "textarea",
    readonly: true
};

var stepName = {
    id: "stepName",
    view: "text",
    readonly: true
};

var scheme = {
    view: "layout",
    cols: [{
        rows: [{
            cols: [
                createTaskButton,
                deleteTaskButton
            ]
        },
            taskList
        ],
        width: 250
    }, {
        rows: [
            taskName,
            taskDescription,
            createStepButton,
            stepTree
        ],
        width: "auto"
    }, {
        rows: [
            stepName,
            stepDescription
        ],
        width: 200
    }]
};

var ui = {
    init: function () {
        webix.ui(createTaskPopup);
        webix.ui(createStepPopup);
        webix.ui(scheme);
    }
};