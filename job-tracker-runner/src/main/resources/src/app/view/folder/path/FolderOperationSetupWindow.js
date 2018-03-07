/**
 * The <class>System.view.folder.path.FolderOperationSetupWindow</class> defines the main
 * setup for folder path operations that can occur against various entities within the system.
 *
 * It also denotes various types of operations that are supported.
 *
 * to entities within the system.
 *
 * @author Andrew
 */
Ext.define('System.view.folder.path.FolderOperationSetupWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Expression Operations',

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Expression Operations',
                modelName: 'EntityExpressionOperations'
            },
            {
                title: 'Expression Operation Types',
                modelName: 'EntityExpressionOperationTypes'
            }
        ];

        this.callParent([config]);
    },

    width: 1200
});