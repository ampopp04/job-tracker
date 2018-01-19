/**
 * The <class>System.view.type.TypeSetupWindow</class> defines the main
 * window for setting up job related items such as job statuses, job types.
 *
 * @author Andrew
 */
Ext.define('System.view.type.TypeSetupWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',


    title: 'Type Management',
    width: 1200,

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Task Status',
                modelName: 'TaskStatuses'
            },
            {
                title: 'Task Types',
                modelName: 'TaskTypes'
            },
            {
                title: 'Project Types',
                modelName: 'ProjectTypes'
            },
            {
                title: 'Departments',
                modelName: 'Departments'
            },
            {
                title: 'Note Types',
                modelName: 'SystemEntityNoteTypes'
            },
            {
                title: 'Hours',
                xtype: 'base-system-grid-panel',
                detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                modelName: 'TaskHours'
            },
            {
                title: 'Notes',
                xtype: 'base-system-grid-panel',
                modelName: 'SystemEntityNotes'
            }
        ];

        this.callParent([config]);
    }

});