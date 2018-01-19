/**
 * The <class>System.util.system.JobUtils</class> defines
 *  utility methods for working with job UI components.
 *
 * @author Andrew
 */
Ext.define('System.util.system.JobUtils', {

    requires: ['System.view.system.window.SchemaTabGridSystemWindow'],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {
        /**
         * Create the job windows
         */
        createJobSchemaTabGridSystemWindow: function () {
            var win = Ext.create("System.view.job.JobWindow", {});
            win.show();
        },
        createWindow: function (windowPath) {
            var win = Ext.create(windowPath, {});
            win.show();
        }

    }
});