var onCreateTodoConfirmButtonClick = function () {
    var form = this.getParentView();
    if (form.validate()) {
        var values = form.getValues();
        var popup = form.getParentView();
        form.clear();
        popup.hide();
    }
};

var onDeleteTodoButtonClick = function () {
    todoList = $$("todoList");
    var selectedItem = todoList.getSelectedItem();
    if (!selectedItem) {
        return;
    }
    var message = "Are you sure you want to delete todoo \"" + selectedItem.name + "\"?";
    webix.confirm({
        ok: "Yes",
        cancel: "No",
        text: message,
        callback: function (result) {
            if (result) {
                todoList.remove(selectedItem.id);
            }
        }
    });
};

var onTodoListSelectChange = function () {
    var selectedItem = this.getSelectedItem();
    todoName = $$("todoName");
    todoName.blockEvent();
    todoName.setValue(selectedItem.name || "");
    todoName.unblockEvent();
    todoName.define('readonly', !selectedItem);
    todoName.refresh();
    todoDescription = $$("todoDescription");
    todoDescription.blockEvent();
    todoDescription.setValue(selectedItem.description || "");
    todoDescription.unblockEvent();
    todoDescription.define('readonly', !selectedItem);
    todoDescription.refresh();
};

var onTodoNameChange = function (newv, oldv) {
    todoList = $$("todoList");
    var selectedItem = todoList.getSelectedItem();
    selectedItem.name = newv;
    todoList.refresh();
};

var onTodoDescriptionChange = function (newv, oldv) {
    todoList = $$("todoList");
    var selectedItem = todoList.getSelectedItem();
    selectedItem.description = newv;
    todoList.refresh();
};

var onCreateTaskConfirmButtonClick = function () {
    var form = this.getParentView();
    if (form.validate()) {
        var values = form.getValues();
        var popup = form.getParentView();
        form.clear();
        popup.hide();
    }
};

var onTaskTreeLoad = function() {
    this.openAll();
};

var onTaskTreeSelectChange = function () {
    var selectedItem = this.getSelectedItem();
    taskName = $$("taskName");
    taskName.blockEvent();
    taskName.setValue(selectedItem.name || "");
    taskName.unblockEvent();
    taskName.define('readonly', !selectedItem);
    taskName.refresh();
    taskDescription = $$("taskDescription");
    taskDescription.blockEvent();
    taskDescription.setValue(selectedItem.description || "");
    taskDescription.unblockEvent();
    taskDescription.define('readonly', !selectedItem);
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
    var taskTree = $$("tasksTree");
    var selectedItem = taskTree.getSelectedItem();
    selectedItem.name = newv;
    taskTree.refresh();
};

var onTaskDescriptionChange = function (newv, oldv) {
    var taskTree = $$("tasksTree");
    var selectedItem = taskTree.getSelectedItem();
    selectedItem.description = newv;
    taskTree.refresh();
};

var logic = {
    attachEvents: function () {
        createTodoButton.popup.body.elements[2].on = {
            onItemClick: onCreateTaskConfirmButtonClick
        };
        deleteTodoButton.on = {
            onItemClick: onDeleteTodoButtonClick
        };
        todoList.on = {
            onSelectChange: onTodoListSelectChange
        };
        todoName.on = {
            onChange: onTodoNameChange
        };
        todoDescription.on = {
            onChange: onTodoDescriptionChange
        };
        createTaskButton.popup.body.elements[2].on = {
            onItemClick: onCreateTaskConfirmButtonClick
        };
        tasksTree.onClick = {
            delete: onTaskTreeItemDeleteIconClick
        };
        tasksTree.on = {
            onAfterLoad: onTaskTreeLoad,
            onSelectChange: onTaskTreeSelectChange,
            onItemCheck: onTaskTreeItemCheck
        };
        taskName.on = {
            onChange: onTaskNameChange
        };
        taskDescription.on = {
            onChange: onTaskDescriptionChange
        };
    }
};

