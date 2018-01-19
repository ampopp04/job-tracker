Ext.define('System.view.job.data.store.JobTreeStore', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'System.view.job.data.model.JobTreeModel',
        'System.view.job.data.store.proxy.JobTreeProxy'
    ],

    storeId: 'JobTreeStore',
    model: 'System.view.job.data.model.JobTreeModel',
    defaultRootText: '',

    proxy: {
        type: 'jobTreeProxy',
        url: '/ajax/job/search'
    },

    //pageSize: 25,
    autoLoad: false,

    remoteFilter: true,
    remoteSort: true,

    folderSort: true

});