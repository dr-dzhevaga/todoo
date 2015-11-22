var categorySelect = {
    view: "select",
    label: "Category:",
    options: categoryData
};

var templatesList = {
    view: "list",
    select: true,
    template: "#name#",
    data: templatesData
};

var tasksTree = {
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
    cols: [
        {
            rows: [
                categorySelect,
                templatesList
            ],
            width: 250
        }, {
            rows: [
                useTodoButton,
                tasksTree
            ],
            width: "auto"
        }
    ]

};

var ui = {
    init: function () {
        webix.ui(scheme);
    }
};