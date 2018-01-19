/**
 * The <class>System.view.folder.path.FolderSetupWindow</class> defines a
 * simplified view for managing and changing folder and number expressions
 * to entities within the system.
 *
 * @author Andrew
 */
Ext.define('System.view.folder.path.FolderSetupWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Folder Setup',

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Folders',
                modelName: 'EntityExpressions',

                groupByField: {
                    entityPropertyPath: '',
                    displayField: 'name',
                    property: 'entityExpressionType'
                }


            }
        ];

        this.callParent([config]);
    },

    width: 1200
});