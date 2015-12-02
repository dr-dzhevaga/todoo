var onTaskTreeLoad = function () {
    this.openAll();
};

var onCategoryRichSelectSelectChange = function (id) {
    var category = $$("categoryRichSelect").getList().getItem(id);
    if (category) {
        webix.ajax("/api/templates", category, function (text) {
            $$("templatesList").clearAll();
            $$("templatesList").parse(text);
        });
    }
};

var onTemplatesListSelectChange = function () {
    var template = $$("templatesList").getSelectedItem();
    $$("templateDescription").setValue(template.description);
};

var logic = {
    init: function () {
        $$("categoryRichSelect").attachEvent("onChange", onCategoryRichSelectSelectChange);
        $$("templatesList").attachEvent("onSelectChange", onTemplatesListSelectChange);
        $$("tasksTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("tasksTree").openAll();
    }
};