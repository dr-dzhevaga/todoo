var categoryRichSelect = {
    id: "categoryRichSelect",
    view: "richselect",
    label: "Filter:",
    options: {
        template: "#name#",
        body: {
            id: "categoryRichSelectList",
            template: "#name#"
        }
    }
};

var templateList = {
    id: "templateList",
    view: "list",
    select: true,
    template: "#name#<div class='description'>#description#</div>",
    type: {
        height: 50
    }
};

var templateDescription = {
    id: "templateDescription",
    view: "textarea",
    height: 60,
    readonly: true
};

var stepTree = {
    id: "stepTree",
    view: "tree",
    template: function (obj, common) {
        var content = common.checkbox(obj, common);
        content += "<div class='step'>";
        content += "<span class='name'>" + obj.name + "</span>";
        content += "<span class='description'>" + (obj.description || "") + "</span>";
        content += "</div>";
        return content;
    },
    css: "selected"
};

var useTemplateButton = {
    id: "useTemplateButton",
    view: "button",
    tooltip: "Use this template",
    label: "Use template",
    type: "icon",
    icon: "hand-o-right",
    align: "left",
    width: 110
};

var scheme = {
    view: "layout",
    cols: [{
        rows: [
            categoryRichSelect,
            templateList
        ],
        width: 250
    }, {
        rows: [
            templateDescription,
            useTemplateButton,
            stepTree
        ],
        width: "auto"
    }]
};

var ui = {
    init: function () {
        webix.ui(scheme);
    }
};