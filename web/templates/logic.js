var onTaskTreeLoad = function () {
    this.openAll();
};

var onCategoryRichSelectSelectChange = function (id) {
    var filter = $$("categoryRichSelect").getList().getItem(id);
    if (filter) {
        ajax.getJson("/api/templates", filter, function (text) {
            $$("templateList").clearAll();
            $$("templateList").parse(text);
        });
    }
};

var onTemplatesListSelectChange = function () {
    var template = $$("templateList").getSelectedItem();
    $$("templateDescription").setValue(template.description);
    var filter = {filter: "parent", id: template.id};
    ajax.getJson("/api/templates", filter, function (text) {
        $$("stepTree").clearAll();
        $$("stepTree").parse(text);
    });
};

var logic = {
    init: function () {
        $$("categoryRichSelect").attachEvent("onChange", onCategoryRichSelectSelectChange);
        $$("templateList").attachEvent("onSelectChange", onTemplatesListSelectChange);
        $$("stepTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("stepTree").openAll();
    }
};