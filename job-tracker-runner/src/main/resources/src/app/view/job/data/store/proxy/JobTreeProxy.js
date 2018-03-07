/**
 * The <class>System.model.proxy.HalProxy</class> defines
 * proxy used for reading and writing json messages to and from the server
 * in the hal format
 *
 * @author Andrew
 */
Ext.define('System.view.job.data.store.proxy.JobTreeProxy', {
    extend: 'Ext.data.proxy.Ajax',

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                         //////////
    //////////////////////////////////////////////////////////////////////

    alternateClassName: 'System.view.job.data.store.proxy.JobTreeProxy',
    alias: 'proxy.jobTreeProxy',

    noCache: false,

    ///////////////////////////////////////////////////////////////////////
    ////////                                                        Methods                                                         //////////
    //////////////////////////////////////////////////////////////////////

    encodeSorters: function (sorters, preventArray) {
        var out = '',
            length = sorters.length,
            i;

        for (i = 0; i < length; i++) {
            if (i > 0) {
                out = out + '&sort=';
            }
            out = out + sorters[i].property + ',' + sorters[i].direction;
        }

        return out;
    },

    encodeFilters: function (filters) {
        var out = [],
            length = filters.length,
            i, op;

        for (i = 0; i < length; i++) {
            var property = filters[i]._property;

            if (filters[i].defaultSubRoot) {
                property = filters[i]._root + '.' + filters[i].defaultSubRoot
            }

            var operator = filters[i]._operator;
            var value = filters[i]._value;

            out[i] = {operator: operator, value: value, property: property};
        }

        return this.applyEncoding(out);
    }

});