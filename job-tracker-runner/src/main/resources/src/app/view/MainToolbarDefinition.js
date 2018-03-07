/**
 * The <class>System.MainToolbarDefinition</class> defines
 *  the main toolbar menu.
 *
 * @author Andrew
 */
Ext.define('System.view.MainToolbarDefinition', {
    extend: 'Ext.container.Container',

    requires: [
        'System.util.system.JobUtils',
        'System.util.system.UserUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    xtype: 'main-toolbar-definition',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    constructor: function (config) {
        System.util.system.UserUtils.setLoggedInEmployee();
        this.callParent([config]);
    },

    listeners: {
        added: function (container, index, eOpts) {

            container.up().insert(0, [
                {xtype: 'tbspacer'},
                {
                    text: 'Admin',
                    iconCls: 'x-fa fa-user-plus',
                    menu: new Ext.menu.Menu({
                        items: [{
                            text: 'Setup',
                            iconCls: 'x-fa fa-gears',
                            menu: new Ext.menu.Menu({
                                items: [{
                                    text: 'Expression Management',
                                    iconCls: 'x-fa fa-folder-open-o',
                                    menu: new Ext.menu.Menu({
                                        items: [{
                                            text: 'Entity Expressions',
                                            iconCls: 'x-fa fa-files-o',
                                            handler: function () {
                                                System.util.system.JobUtils.createWindow('System.view.folder.path.FolderPathSetupWindow');
                                            }
                                        }, {
                                            text: 'Expression Operations',
                                            iconCls: 'x-fa fa-cogs',
                                            handler: function () {
                                                System.util.system.JobUtils.createWindow('System.view.folder.path.FolderOperationSetupWindow');
                                            }
                                        }]
                                    })
                                }]
                            })
                        }]
                    })
                }]);

            Ext.TaskManager.start({
                run: function () {
                    var task = this;
                    var mainToolbar = task.mainToolbar;

                    if (mainToolbar.userIsLoaded == undefined) {
                        var loggedInUserFirstName = System.util.system.UserUtils.getLoggedInEmployee().firstName;

                        if (!Ext.isEmpty(loggedInUserFirstName)) {
                            System.util.application.Util.showToast('Welcome ' + loggedInUserFirstName);
                            mainToolbar.userIsLoaded = true;
                        }
                    }

                    if (task.taskRunCount > 5) {
                        task.interval = 150;
                    }
                },
                interval: 5,
                repeat: 50,
                mainToolbar: window
            });

            System.util.system.JobUtils.createWindow('System.view.job.tracker.dashboard.DashboardWindow');

        }
    }
});