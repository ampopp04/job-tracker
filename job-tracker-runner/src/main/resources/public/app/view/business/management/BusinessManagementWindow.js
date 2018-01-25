/**
 * The <class>System.view.business.management.BusinessManagementWindow</class> defines
 * a simplified view of the business management related entities.
 *
 * @author Andrew
 */
Ext.define('System.view.business.management.BusinessManagementWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Business Management',
    
    width: 1200,

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Customers',
                detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                modelName: 'Customers'
            }, {
                title: 'Employees',
                detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                modelName: 'Employees'
            }, {
                title: 'Branches',
                detailFormWindow: 'System.view.system.detail.window.NoteDetailFormWindow',
                modelName: 'Branches'
            }
        ];

        this.callParent([config]);
    }

});