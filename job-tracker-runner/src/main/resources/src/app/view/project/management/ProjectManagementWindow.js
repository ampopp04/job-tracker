/**
 * The <class>System.view.project.management.ProjectManagementWindow</class> defines
 * a simplified view of the project management related entities.
 *
 * @author Andrew
 */
Ext.define('System.view.project.management.ProjectManagementWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Project Management',

    width: 1200,

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Tasks',
                detailFormWindow: 'System.view.system.detail.window.TaskDetailFormWindow',
                modelName: 'Tasks'
            }, {
                title: 'Revisions',
                detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                modelName: 'TaskRevisions'
            }, {
                title: 'Jobs',
                detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                modelName: 'Jobs'
            }, {
                title: 'Projects',
                detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                modelName: 'Projects'
            }
        ];

        this.callParent([config]);
    }

});