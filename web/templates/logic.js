var onTaskTreeLoad = function () {
    this.openAll();
};

var logic = {
    init: function () {
        $$("tasksTree").attachEvent("onAfterLoad", onTaskTreeLoad);
        $$("tasksTree").openAll();
    }
};