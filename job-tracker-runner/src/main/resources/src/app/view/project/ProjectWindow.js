/**
 * The <class>System.view.project.ProjectWindow</class> defines the main
 * job.tracker project window.
 *
 * @author Andrew
 */
Ext.define('System.view.project.ProjectWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Projects',

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Projects',
                modelName: 'Projects'
            }
        ];

        this.callParent([config]);
    }

});