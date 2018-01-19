/**
 * The <class>System.view.job.JobWindow</class> defines the main
 * job.tracker job window
 *
 * @author Andrew
 */
Ext.define('System.view.job.JobWindow', {
    extend: 'System.view.component.window.tab.grid.TabGridSystemWindow',

    title: 'Job Management',

    constructor: function (config) {
        var me = config;

        me.tabs = [
            {
                title: 'Jobs',
                modelName: 'Jobs'
            }
        ];

        this.callParent([config]);
    }

});