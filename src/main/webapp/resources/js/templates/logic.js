var onCategoryRichSelectSelectChange = function (id) {
    var category = $$("categoryRichSelect").getList().getItem(id);
    if (category) {
        dataStoreHelper.reload("templateList", category, TEMPLATE_API_ENDPOINT);
        dataStoreHelper.setValueSilently("templateDescription");
        $$("stepTree").clearAll();
    }
};

var onCategoryRichSelectSelectLoad = function () {
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
        ajax_util.postObject(TASK_API_ENDPOINT + "?templateId=" + template.id, {}, function () {
            window.location = ("/mytasks.jsp");
        });
    }
};

var logic = {
    init: function () {
        webix.DataDriver.json.child = "children";
        $$("categoryRichSelect").attachEvent("onChange", onCategoryRichSelectSelectChange);
        $$("categoryRichSelect").getList().attachEvent("onAfterLoad", onCategoryRichSelectSelectLoad);
        $$("templateList").attachEvent("onSelectChange", onTemplatesListSelectChange);
        $$("stepTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("useTemplateButton").attachEvent("onItemClick", onUseTemplateButtonClick);
        dataStoreHelper.reload("categoryRichSelectList", {}, CATEGORY_API_ENDPOINT);
    }
};