var onCategoryRichSelectSelectChange = function (id) {
    var category = $$("categoryRichSelect").getList().getItem(id);
    if (category) {
        dataStoreHelper.reload("templateList", category, TEMPLATE_API_ENDPOINT);
        dataStoreHelper.setValueSilently("templateDescription");
        $$("stepTree").clearAll();
    }
};

var onCategoryRichSelectSelectLoad = function () {
    $$("categoryRichSelect").setValue(1);
};

var onTemplatesListSelectChange = function () {
    var template = $$("templateList").getSelectedItem();
    $$("templateDescription").setValue(template.description);
    dataStoreHelper.reload("stepTree", {filter: "parent", id: template.id}, TEMPLATE_API_ENDPOINT);
};

var onTaskTreeLoad = function () {
    this.openAll();
};

var onUseTemplateButtonClick = function () {
    var template = $$("templateList").getSelectedItem();
    if (template) {
        ajax.postJson(TASK_API_ENDPOINT + "?templateId=" + template.id, {}, function () {
            window.location = ("/mytasks.html");
        });
    }
};

var logic = {
    init: function () {
        $$("categoryRichSelect").attachEvent("onChange", onCategoryRichSelectSelectChange);
        $$("categoryRichSelect").getList().attachEvent("onAfterLoad", onCategoryRichSelectSelectLoad);
        $$("templateList").attachEvent("onSelectChange", onTemplatesListSelectChange);
        $$("stepTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("useTemplateButton").attachEvent("onItemClick", onUseTemplateButtonClick);
    }
};