var onCreateTaskButtonClick = function () {
    popupHelper.show("createTaskPopup", this);
};

var onCreateTaskConfirmButtonClick = function () {
    var task = popupHelper.getValue("createTaskPopup");
    if (task) {
        ajax_util.postObject(TASK_API_ENDPOINT, task, function (data) {
            $$("taskList").add(data.json());
        });
    }
};

var onDeleteTaskButtonClick = function () {
    var task = $$("taskList").getSelectedItem();
    if (!task) {
        return;
    }
    webix.confirm({
        ok: "Yes",
        cancel: "No",
        text: "Are you sure you want to delete task \"" + task.name + "\"?",
        callback: function (result) {
            if (result) {
                ajax_util.deleteId(TASK_API_ENDPOINT, task.id, function () {
                    $$("taskList").remove(task.id);
                    $$("stepTree").clearAll();
                    dataStoreHelper.setValueSilently("taskName");
                    dataStoreHelper.setValueSilently("taskDescription");
                    dataStoreHelper.setValueSilently("stepName");
                    dataStoreHelper.setValueSilently("stepDescription");
                });
            }
        }
    });
};

var onTaskListSelectChange = function () {
    var task = this.getSelectedItem();
    dataStoreHelper.reload("stepTree", {filter: "parent", id: task.id}, TASK_API_ENDPOINT);
    dataStoreHelper.setValueSilently("taskName", task.name);
    $$("taskName").define("readonly", !task);
    $$("taskName").refresh();
    dataStoreHelper.setValueSilently("taskDescription", task.description);
    $$("taskDescription").define('readonly', !task);
    $$("taskDescription").refresh();
    dataStoreHelper.setValueSilently("stepName");
    dataStoreHelper.setValueSilently("stepDescription");
};

var onTaskNameChange = function (name) {
    var task = $$("taskList").getSelectedItem();
    task.name = name;
    ajax_util.putObject(TASK_API_ENDPOINT, task, function () {
        $$("taskList").refresh();
    });
};

var onTaskDescriptionChange = function (description) {
    var task = $$("taskList").getSelectedItem();
    task.description = description;
    ajax_util.putObject(TASK_API_ENDPOINT, task, function () {
        $$("taskList").refresh();
    });
};

var onCreateStepButtonClick = function () {
    var task = $$("taskList").getSelectedItem();
    var step = $$("stepTree").getSelectedItem();
    var parent = step ? step : task;
    if (parent) {
        $$("createStepPopup").getBody().setValues({parentId: parent.id, taskId: task.id});
        popupHelper.show("createStepPopup", this);
    } else {
        webix.message("Select parent task first");
    }
};

var onCreateStepConfirmButtonClick = function () {
    var step = popupHelper.getValue("createStepPopup");
    if (step) {
        ajax_util.postObject(TASK_API_ENDPOINT, step, function () {
            dataStoreHelper.reload("stepTree", {filter: "parent", id: step.taskId}, TASK_API_ENDPOINT);
        });
    }
};

var onStepTreeLoad = function () {
    $$("stepTree").data.each(
        function (step) {
            if (step.completed) {
                $$("stepTree").checkItem(step.id);
            }
        }
    );
    $$("stepTree").openAll();
};

var onStepTreeSelectChange = function () {
    var step = this.getSelectedItem();
    dataStoreHelper.setValueSilently("stepName", step.name);
    $$("stepName").define("readonly", !step);
    $$("stepName").refresh();
    dataStoreHelper.setValueSilently("stepDescription", step.description);
    $$("stepDescription").define('readonly', !step);
    $$("stepDescription").refresh();
};

var onStepTreeItemCheck = function (id, state) {
    var step = $$("stepTree").getItem(id);
    step.completed = state;
    ajax_util.putObject(TASK_API_ENDPOINT, step, function () {
        $$("stepTree").refresh();
    });
};

var onStepTreeItemDeleteIconClick = function (e, id) {
    ajax_util.deleteId(TASK_API_ENDPOINT, id, function () {
        $$("stepTree").remove(id);
        dataStoreHelper.setValueSilently("stepName");
        dataStoreHelper.setValueSilently("stepDescription");
    });
};

var onStepTreeAfterDrop = function (context) {
    var source = this.getItem(context.source);
    var target = this.getItem(context.target);
    source.order = target.order;
    source.parentId = target.parentId;
    ajax_util.putObject(TASK_API_ENDPOINT, source, function () {
        var task = $$("taskList").getSelectedItem();
        dataStoreHelper.reload("stepTree", {filter: "parent", id: task.id}, TASK_API_ENDPOINT);
    });
};

var onStepNameChange = function (name) {
    var step = $$("stepTree").getSelectedItem();
    step.name = name;
    ajax_util.putObject(TASK_API_ENDPOINT, step, function () {
        $$("stepTree").refresh();
    });
};

var onStepDescriptionChange = function (description) {
    var step = $$("stepTree").getSelectedItem();
    step.description = description;
    ajax_util.putObject(TASK_API_ENDPOINT, step, function () {
        $$("stepTree").refresh();
    });
};

var logic = {
    init: function () {
        webix.DataDriver.json.child = "children";
        $$("createTaskButton").attachEvent("onItemClick", onCreateTaskButtonClick);
        $$("createTaskConfirmButton").attachEvent("onItemClick", onCreateTaskConfirmButtonClick);
        $$("deleteTaskButton").attachEvent("onItemClick", onDeleteTaskButtonClick);
        $$("taskList").attachEvent("onSelectChange", onTaskListSelectChange);
        $$("taskName").attachEvent("onChange", onTaskNameChange);
        $$("taskDescription").attachEvent("onChange", onTaskDescriptionChange);
        $$("createStepButton").attachEvent("onItemClick", onCreateStepButtonClick);
        $$("createStepConfirmButton").attachEvent("onItemClick", onCreateStepConfirmButtonClick);
        $$("stepTree").attachEvent("onAfterLoad", onStepTreeLoad);
        $$("stepTree").openAll();
        $$("stepTree").attachEvent("onSelectChange", onStepTreeSelectChange);
        $$("stepTree").attachEvent("onItemCheck", onStepTreeItemCheck);
        $$("stepTree").on_click.delete = onStepTreeItemDeleteIconClick;
        $$("stepTree").attachEvent("onAfterDrop", onStepTreeAfterDrop);
        $$("stepName").attachEvent("onChange", onStepNameChange);
        $$("stepDescription").attachEvent("onChange", onStepDescriptionChange);
        dataStoreHelper.reload("taskList", {}, TASK_API_ENDPOINT);
    }
};