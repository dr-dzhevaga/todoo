var reloadTemplates = function (id, filter) {
    ajax.getJson(TEMPLATE_API_ENDPOINT, filter, function (text) {
        $$(id).clearAll();
        $$(id).parse(text);
    });
};

var onCategoryRichSelectSelectChange = function (id) {
    var category = $$("categoryRichSelect").getList().getItem(id);
    if (category) {
        reloadTemplates("templateList", category);
        $$("stepTree").clearAll();
    }
};

var onCategoryRichSelectSelectLoad = function () {
    $$("categoryRichSelect").setValue(1);
};

var onTemplatesListSelectChange = function () {
    var template = $$("templateList").getSelectedItem();
    $$("templateDescription").setValue(template.description);
    reloadTemplates("stepTree", {filter: "parent", id: template.id});
};

var onStepTreeAfterDrop = function (context) {
    var source = this.getItem(context.source);
    var target = this.getItem(context.target);
    source.order = target.order;
    source.parentId = target.parentId;
    ajax.putJson(TEMPLATE_API_ENDPOINT, source, function () {
        var template = $$("templateList").getSelectedItem();
        reloadTemplates("stepTree", {filter: "parent", id: template.id});
    });
};

var onTaskTreeLoad = function () {
    this.openAll();
};

var logic = {
    init: function () {
        $$("categoryRichSelect").attachEvent("onChange", onCategoryRichSelectSelectChange);
        $$("categoryRichSelect").getList().attachEvent("onAfterLoad", onCategoryRichSelectSelectLoad);
        $$("templateList").attachEvent("onSelectChange", onTemplatesListSelectChange);
        $$("stepTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("stepTree").attachEvent("onAfterDrop", onStepTreeAfterDrop);
    }
};