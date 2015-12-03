var onCategoryRichSelectSelectChange = function (id) {
    var category = $$("categoryRichSelect").getList().getItem(id);
    if (category) {
        uiComponent.reload("templateList", category, TEMPLATE_API_ENDPOINT);
        $$("stepTree").clearAll();
    }
};

var onCategoryRichSelectSelectLoad = function () {
    $$("categoryRichSelect").setValue(1);
};

var onTemplatesListSelectChange = function () {
    var template = $$("templateList").getSelectedItem();
    $$("templateDescription").setValue(template.description);
    uiComponent.reload("stepTree", {filter: "parent", id: template.id}, TEMPLATE_API_ENDPOINT);
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
    }
};