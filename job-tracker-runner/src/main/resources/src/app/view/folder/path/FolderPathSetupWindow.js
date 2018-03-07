/**
 * The <class>System.view.folder.path.FolderPathSetupWindow</class> defines the main
 * setup window for managing folder path expressions and their assignments
 * to entities within the system.
 *
 * @author Andrew
 */
Ext.define('System.view.folder.path.FolderPathSetupWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Entity Expressions',

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Expressions',
                modelName: 'EntityExpressions'
            },
            {
                title: 'Expression Assignments',
                modelName: 'EntityExpressionAssignments'
            }
        ];

        this.callParent([config]);
    },

    width: 1200
});