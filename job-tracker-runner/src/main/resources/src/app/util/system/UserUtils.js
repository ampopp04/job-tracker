/**
 * The <class>System.util.system.UserUtils</class> defines
 *  utility methods for working with users.
 *
 *  Specifically users related to the currently logged in user and their associated information.
 *
 * @author Andrew
 */
Ext.define('System.util.system.UserUtils', {

    requires: [
        'System.util.component.GridColumnUtils',
        'System.util.data.StoreUtils'
    ],

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                      //////////
    /////////////////////////////////////////////////////////////////////

    statics: {

        /**
         * Get Logged in Employee Information
         */
        getLoggedInEmployee: function () {
            var loggedInEmployeeJson = localStorage.getItem("loggedInEmployee");

            if (!Ext.isEmpty(loggedInEmployeeJson)) {
                try {
                    return Ext.JSON.decode(loggedInEmployeeJson);
                } catch (e) {
                    return {};
                }
            }

            return {};
        },


        /**
         * Set User Information
         */
        setLoggedInEmployee: function () {

            if (window.loggingInEmployee == true) {
                return;
            }
            window.loggingInEmployee = true;

            var loggedInEmployeeDecoded = System.util.system.UserUtils.getLoggedInEmployee();
            if (!Ext.isEmpty(loggedInEmployeeDecoded) && !Ext.isEmpty(loggedInEmployeeDecoded.name) && !Ext.isEmpty(localStorage.username)) {
                window.loggingInEmployee = false;
                return;
            }

            var configureLoggedInEmployee = function (securityUserRecord) {

                localStorage.setItem("loggedInEmployee", Ext.JSON.encode({
                    systemSecurityUserId: securityUserRecord.data.id,
                    admin: securityUserRecord.data.admin
                }));

                //Get Employee Store
                System.util.component.GridColumnUtils.getStoreByModelName('Employees', function (store, scope) {
                    //Retrieve the associated employee record for this logged in user
                    System.util.data.StoreUtils.queryStoreByEntityName(
                        store,
                        scope.data.name,
                        function (records, operation, success, scope) {
                            var branch = undefined;
                            var employee = {};

                            //Set Logged in Employee
                            if (success && records && records[0] && records[0].data) {
                                var employeeRecord = records[0];
                                employee = employeeRecord.data;

                                branch =
                                    employeeRecord._branch && employeeRecord._branch.data && employeeRecord._branch.data.name ?
                                        employeeRecord._branch.data.name : employeeRecord.branch && employeeRecord.branch && employeeRecord.branch.name ?
                                        employeeRecord.branch.name : employeeRecord.data && employeeRecord.data.branch && employeeRecord.data.branch.name ?
                                            employeeRecord.data.branch.name : "La Mirada";
                            }

                            localStorage.setItem("loggedInEmployee", Ext.JSON.encode({
                                name: employee.name,
                                firstName: employee.firstName,
                                lastName: employee.lastName,
                                branch: branch,
                                systemSecurityUserId: scope.data.id,
                                admin: scope.data.admin,
                                id: employee.id
                            }));

                            window.loggingInEmployee = true;

                        },
                        undefined,
                        scope
                    );


                }, securityUserRecord);

            };

            try {

                System.util.component.GridColumnUtils.getStoreByModelName('SystemSecurityUsers', function (store, scope) {
                    //Get username
                    var username = localStorage.getItem("username");

                    //Get SecurityUser
                    System.util.data.StoreUtils.queryStoreByPropertyNameValue(
                        store,
                        {username: username},
                        'search/findByUsername',
                        function (records, operation, success, scope) {

                            if (records && records[0] && records[0].data) {
                                //We have the currently logged in security user
                                var securityUser = records[0];
                                scope(securityUser);
                            }

                        },
                        undefined,
                        scope
                    );
                }, configureLoggedInEmployee);

            } catch (e) {
                //User not found
            }

        }

    }
})
;