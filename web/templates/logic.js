var onTaskTreeLoad = function () {
    this.openAll();
};

var onCategoryRichSelectSelectChange = function (id) {
    var filter = $$("categoryRichSelect").getList().getItem(id);
    if (filter) {
        webix.ajax("/api/templates", filter, function (text) {
            $$("templatesList").clearAll();
            $$("templatesList").parse(text);
        });
    }
};

var onTemplatesListSelectChange = function () {
    var template = $$("templatesList").getSelectedItem();
    $$("templateDescription").setValue(template.description);
    var filter = {filter: "parent", id: template.id};
    webix.ajax("/api/templates", filter, function (text) {
        $$("tasksTree").clearAll();
        $$("tasksTree").parse(text);
    });
};

var logic = {
    init: function () {
        $$("categoryRichSelect").attachEvent("onChange", onCategoryRichSelectSelectChange);
        $$("templatesList").attachEvent("onSelectChange", onTemplatesListSelectChange);
        $$("tasksTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("tasksTree").openAll();
    }
};