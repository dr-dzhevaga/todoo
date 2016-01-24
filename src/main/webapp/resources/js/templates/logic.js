var onCategoryRichSelectSelectChange = function (id) {
    var category = $$("categoryRichSelect").getList().getItem(id);
    if (category) {
        dataStoreHelper.reload("templateList", category, TEMPLATE_API_ENDPOINT);
        dataStoreHelper.setValueSilently("templateDescription");
        $$("stepTree").clearAll();
    }
};

var onCategoryRichSelectLoad = function () {
    $$("categoryRichSelect").setValue($$("categoryRichSelect").getList().getFirstId());
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
        ajax_util.post(TASK_API_ENDPOINT, {"templateId": template.id}, function () {
            window.location = TASKS_PAGE;
        });
    }
};

var logic = {
    init: function () {
        webix.DataDriver.json.child = "children";
        $$("categoryRichSelect").attachEvent("onChange", onCategoryRichSelectSelectChange);
        $$("categoryRichSelect").getList().attachEvent("onAfterLoad", onCategoryRichSelectLoad);
        $$("templateList").attachEvent("onSelectChange", onTemplatesListSelectChange);
        $$("stepTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("useTemplateButton").attachEvent("onItemClick", onUseTemplateButtonClick);
        dataStoreHelper.reload("categoryRichSelectList", {}, CATEGORY_API_ENDPOINT);
    }
};