var categoryRichSelect = {
    id: "categoryRichSelect",
    view: "richselect",
    label: "Category:",
    options: {
        template: "#name#",
        url: "/api/categories",
        body: {
            template: "#name#"
        }
    }
};

var templatesList = {
    id: "templatesList",
    view: "list",
    select: true,
    template: "#name#",
    url: "/api/templates"
};

var tasksTree = {
    id: "tasksTree",
    view: "tree",
    template: "{common.checkbox()}#name# <span class='description'>#description#</span>",
    data: tasksData
};

var useTodoButton = {
    view: "button",
    tooltip: "Use this todo",
    type: "icon",
    icon: "hand-o-right",
    align: "left",
    width: 30
};

var scheme = {
    view: "layout",
    cols: [{
        rows: [
            categoryRichSelect,
            templatesList
        ],
        width: 250
    }, {
        rows: [
            useTodoButton,
            tasksTree
        ],
        width: "auto"
    }]
};

var ui = {
    init: function () {
        webix.ui(scheme);
    }
};