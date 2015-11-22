var onTaskTreeLoad = function() {
    this.openAll();
};

var logic = {
    attachEvents: function () {
        tasksTree.on = {
            onAfterLoad: onTaskTreeLoad
        };
    }
};

