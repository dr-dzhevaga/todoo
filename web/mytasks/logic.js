function showPopup(id, parent) {
    $$(id).show(parent.$view);
    $$(id).getBody().focus();
}

var getPopupValue = function (id) {
    var form = $$(id).getBody();
    if (form.validate()) {
        $$(id).hide();
        var value = form.getValues();
        form.clear();
        return value;
    }
};

var onCreateTodoButtonClick = function () {
    showPopup("createTodoPopup", this);
};

var onCreateTodoConfirmButtonClick = function () {
    getPopupValue("createTodoPopup");
};

var onDeleteTodoButtonClick = function () {
    var todo = $$("todoList").getSelectedItem();
    if (!todo) {
        return;
    }
    var message = "Are you sure you want to delete todoo \"" + todo.name + "\"?";
    webix.confirm({
        ok: "Yes",
        cancel: "No",
        text: message,
        callback: function (result) {
            if (result) {
                $$("todoList").remove(todo.id);
            }
        }
    });
};

var onTodoListSelectChange = function () {
    var todo = this.getSelectedItem();
    todoName = $$("todoName");
    todoName.blockEvent();
    todoName.setValue(todo.name || "");
    todoName.unblockEvent();
    todoName.define('readonly', !todo);
    todoName.refresh();
    todoDescription = $$("todoDescription");
    todoDescription.blockEvent();
    todoDescription.setValue(todo.description || "");
    todoDescription.unblockEvent();
    todoDescription.define('readonly', !todo);
    todoDescription.refresh();
};

var onTodoNameChange = function (newv, oldv) {
    var todo = $$("todoList").getSelectedItem();
    todo.name = newv;
    $$("todoList").refresh();
};

var onTodoDescriptionChange = function (newv, oldv) {
    var todo = $$("todoList").getSelectedItem();
    todo.description = newv;
    $$("todoList").refresh();
};

var onCreateTaskButtonClick = function () {
    showPopup("createTaskPopup", this);
};

var onCreateTaskConfirmButtonClick = function () {
    getPopupValue("createTaskPopup");
};

var onTaskTreeLoad = function () {
    this.openAll();
};

var onTaskTreeSelectChange = function () {
    var task = this.getSelectedItem();
    taskName = $$("taskName");
    taskName.blockEvent();
    taskName.setValue(task.name || "");
    taskName.unblockEvent();
    taskName.define('readonly', !task);
    taskName.refresh();
    taskDescription = $$("taskDescription");
    taskDescription.blockEvent();
    taskDescription.setValue(task.description || "");
    taskDescription.unblockEvent();
    taskDescription.define('readonly', !task);
    taskDescription.refresh();
};

var onTaskTreeItemCheck = function (id, state) {
    this.getItem(id).completed = state;
    this.refresh();
};

var onTaskTreeItemDeleteIconClick = function (e, id) {
    this.remove(id);
    return false;
};

var onTaskNameChange = function (newv, oldv) {
    var task = $$("tasksTree").getSelectedItem();
    task.name = newv;
    $$("tasksTree").refresh();
};

var onTaskDescriptionChange = function (newv, oldv) {
    var task = $$("tasksTree").getSelectedItem();
    task.description = newv;
    $$("tasksTree").refresh();
};

var logic = {
    init: function () {
        $$("createTodoButton").attachEvent("onItemClick", onCreateTodoButtonClick);
        $$("createTodoConfirmButton").attachEvent("onItemClick", onCreateTodoConfirmButtonClick);
        $$("deleteTodoButton").attachEvent("onItemClick", onDeleteTodoButtonClick);
        $$("todoList").attachEvent("onSelectChange", onTodoListSelectChange);
        $$("todoName").attachEvent("onChange", onTodoNameChange);
        $$("todoDescription").attachEvent("onChange", onTodoDescriptionChange);
        $$("createTaskButton").attachEvent("onItemClick", onCreateTaskButtonClick);
        $$("createTaskConfirmButton").attachEvent("onItemClick", onCreateTaskConfirmButtonClick);
        $$("tasksTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("tasksTree").openAll();
        $$("tasksTree").attachEvent("onSelectChange", onTaskTreeSelectChange);
        $$("tasksTree").attachEvent("onItemCheck", onTaskTreeItemCheck);
        $$("tasksTree").on_click.delete = onTaskTreeItemDeleteIconClick;
        $$("taskName").attachEvent("onChange", onTaskNameChange);
        $$("taskDescription").attachEvent("onChange", onTaskDescriptionChange);
    }
};