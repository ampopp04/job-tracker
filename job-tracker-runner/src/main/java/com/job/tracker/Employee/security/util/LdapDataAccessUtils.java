package com.job.tracker.Employee.security.util;

import com.system.logging.util.LogUtils;
import com.system.security.role.SystemSecurityRoles;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.core.DirContextOperations;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;
import static com.system.util.string.StringUtils.*;

/**
 * The <class>LdapDataAccessUtils</class> defines
 * utility methods for access specific pieces of data
 * from an LDAP directory data response context.
 *
 * @author Andrew
 */
public class LdapDataAccessUtils {

    public static String getValue(DirContextOperations ctx, String identifier) {
        try {
            return ctx.getStringAttribute(identifier);
        } catch (Exception e) {
            LogUtils.logInfo("Error Getting LDAP Value - Identifier [" + identifier + "] - Error: " + e.getLocalizedMessage());
            return null;
        }
    }

    public static String getFirstName(DirContextOperations ctx) {
        //firstName or common name (cn)
        String firstName = getValue(ctx, "givenname");
        return isEmpty(firstName) ? getValue(ctx, "cn") : firstName;
    }

    public static String getLastName(DirContextOperations ctx) {
        //surname
        return getValue(ctx, "sn");
    }

    public static String getFullName(DirContextOperations ctx) {
        return getFirstName(ctx) + " " + getLastName(ctx);
    }

    public static String getUsername(DirContextOperations ctx) {
        return getValue(ctx, "samaccountname");
    }

    public static String getDistinguishedName(DirContextOperations ctx) {
        return getValue(ctx, "distinguishedName");
    }

    public static String getUserPrincipalName(DirContextOperations ctx) {
        return getValue(ctx, "userPrincipalName");
    }

    public static String getUserBranchName(DirContextOperations ctx) {
        //physicaldeliveryofficename else getUserCity
        String branchName = getValue(ctx, "physicaldeliveryofficename");
        return isEmpty(branchName) ? getUserCity(ctx) : branchName;
    }


    public static String getUserDepartmentName(DirContextOperations ctx) {
        return getValue(ctx, "department");
    }

    public static String getUserCompanyName(DirContextOperations ctx) {
        return getValue(ctx, "company");
    }

    public static String getUserEmailAddress(DirContextOperations ctx) {
        return getValue(ctx, "mail");
    }

    public static String getUserJobTitle(DirContextOperations ctx) {
        return getValue(ctx, "title");
    }

    public static String getUserStreetAddress(DirContextOperations ctx) {
        return getValue(ctx, "streetaddress");
    }

    public static String getUserCity(DirContextOperations ctx) {
        return getValue(ctx, "l");
    }

    public static String getUserState(DirContextOperations ctx) {
        return getValue(ctx, "st");
    }

    public static String getUserZipCode(DirContextOperations ctx) {
        return getValue(ctx, "postalCode");
    }

    public static String getUserCountry(DirContextOperations ctx) {
        return getValue(ctx, "c");
    }

    public static String getUserBusinessPhone(DirContextOperations ctx) {
        return getValue(ctx, "telephoneNumber");
    }

    public static String getUserMobilePhone(DirContextOperations ctx) {
        return getValue(ctx, "mobile");
    }

    public static String getUserManagerFullName(DirContextOperations ctx) {
        String managerDn = getValue(ctx, "manager");
        String managerFullName = null;

        if (!isEmpty(managerDn) && contains(managerDn, "CN=")) {
            try {
                managerFullName = StringUtils.split(managerDn, ",")[0];
                managerFullName = removeBeforeInclusive(managerFullName, "CN=").trim();
            } catch (Exception e) {
                LogUtils.logInfo("Error Updating User Manager - Error: " + e.getLocalizedMessage());
                managerFullName = null;
            }
        }

        return managerFullName;
    }

    public static String getUserMemberOf(DirContextOperations ctx) {
        return getValue(ctx, "memberof");
    }

    /**
     * Can't really determine accurately
     */
    public static Boolean isUserEnabled(DirContextOperations ctx) {
        return null;
    }

    /**
     * Let's leave this as manual, don't automatically set
     */
    public static Boolean isUserAdmin(DirContextOperations ctx) {
        return null;
    }

    /**
     * Leave this as false for now default, we can technically determine this
     * but it might be erroneous
     */
    public static boolean isPasswordExpired(DirContextOperations ctx) {
        return false;
    }

    public static List<String> getRoleNameList(DirContextOperations ctx) {
        return asList(SystemSecurityRoles.ROLE_USER.toString());
    }

}
