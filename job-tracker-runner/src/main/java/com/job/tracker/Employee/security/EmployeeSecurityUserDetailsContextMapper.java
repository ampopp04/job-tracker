package com.job.tracker.Employee.security;

import com.job.tracker.Employee.Employee;
import com.job.tracker.branch.Branch;
import com.job.tracker.department.Department;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.logging.util.LogUtils;
import com.system.security.role.SystemSecurityRole;
import com.system.security.role.SystemSecurityRoles;
import com.system.security.user.SystemSecurityUser;
import com.system.security.user.SystemSecurityUserRepository;
import com.system.security.user.detail.mapper.SystemSecurityUserDetailsContextMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.system.security.ldap.LdapDataAccessUtils.*;
import static com.system.util.string.StringUtils.isEmpty;

/**
 * The <class>EmployeeSecurityUserDetailsContextMapper</class> defines
 * the interceptor for user login to auto create/update employee entries
 *
 * @author Andrew
 */
public class EmployeeSecurityUserDetailsContextMapper extends SystemSecurityUserDetailsContextMapper {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<Employee> employeeRepository;

    @Autowired
    private NamedEntityRepository<Branch> branchRepository;

    @Autowired
    private NamedEntityRepository<Department> departmentRepository;

    @Autowired
    private SystemSecurityUserRepository systemSecurityUserRepository;

    @Autowired
    private NamedEntityRepository<SystemSecurityRole> systemSecurityRoleRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    public EmployeeSecurityUserDetailsContextMapper(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                   Mapping Method                                             //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {

        try {
            LogUtils.logInfo("Updating User Employee Details with Context - [" + attributesToString(ctx.getAttributes()) + "]");
            createOrUpdateSecurityData(ctx, getUsername(ctx), authorities);
        } catch (Exception e) {
            LogUtils.logInfo("Error Updating User Employee Details  - [" + username + "] - Error: " + e.getLocalizedMessage());
        }

        return getUserDetailsService().loadUserByUsername(username);
    }

    private String attributesToString(Attributes attributes) throws NamingException {
        String result = "";
        NamingEnumeration<String> namingEnumeration = attributes.getIDs();

        while (namingEnumeration.hasMore()) {
            String nextName = namingEnumeration.next();
            Attribute nameAttribute = attributes.get(nextName);
            String nameValue = nameAttribute.toString();
            result = result + ", [name: " + nextName + ", toString: " + nameValue + "]";
        }

        return result;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Helper Methods                                           //////////
    //////////////////////////////////////////////////////////////////////

    private void createOrUpdateSecurityData(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        SystemSecurityUser systemSecurityUser = getOrCreateSystemSecurityUser(ctx, username, authorities);
        createOrUpdateEmployeeData(ctx, username, authorities, systemSecurityUser);
    }

    private SystemSecurityUser getOrCreateSystemSecurityUser(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        SystemSecurityUser systemSecurityUser = getSystemSecurityUserRepository().findByUsername(username);

        if (systemSecurityUser == null) {
            systemSecurityUser = createSystemSecurityUser(ctx, username, authorities);
        } else {
            systemSecurityUser = updateSystemSecurityUser(systemSecurityUser, ctx, username, authorities);
        }

        return systemSecurityUser;
    }

    private void createOrUpdateEmployeeData(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities, SystemSecurityUser systemSecurityUser) {
        String fullName = getFullName(ctx);
        Employee employee = getEmployeeRepository().findByName(fullName);

        if (employee == null) {
            createEmployee(ctx, username, authorities, systemSecurityUser);
        } else {
            updateEmployee(employee, systemSecurityUser, ctx, username, authorities);
        }

    }

    private Employee createEmployee(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities, SystemSecurityUser systemSecurityUser) {
        Employee employee = new Employee();
        employee = updateEmployee(employee, systemSecurityUser, ctx, username, authorities);
        return employee;
    }

    private SystemSecurityUser createSystemSecurityUser(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        SystemSecurityUser systemSecurityUser = new SystemSecurityUser();
        updateSystemSecurityUser(systemSecurityUser, ctx, username, authorities);
        return systemSecurityUser;
    }

    private SystemSecurityUser updateSystemSecurityUser(SystemSecurityUser systemSecurityUser, DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        systemSecurityUser.setFirstName(getFirstName(ctx));
        systemSecurityUser.setLastName(getLastName(ctx));
        systemSecurityUser.setUsername(username);

        systemSecurityUser.setDistinguishedName(getDistinguishedName(ctx));
        systemSecurityUser.setEnabled(systemSecurityUser.isNew() ? true : systemSecurityUser.getEnabled());
        systemSecurityUser.setRoles(systemSecurityUser.isNew() ? getSystemSecurityRoleList() : systemSecurityUser.getRoles());
        systemSecurityUser.setUserPrincipalName(getUserPrincipalName(ctx));
        systemSecurityUser.setMemberOf(getUserMemberOf(ctx));

        return getSystemSecurityUserRepository().save(systemSecurityUser);
    }

    private List<SystemSecurityRole> getSystemSecurityRoleList() {
        SystemSecurityRole userRole = getSystemSecurityRoleRepository().findByName(SystemSecurityRoles.ROLE_USER.toString());
        return Arrays.asList(userRole);
    }

    private Employee updateEmployee(Employee employee, SystemSecurityUser systemSecurityUser, DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        employee.setFirstName(getFirstName(ctx));
        employee.setLastName(getLastName(ctx));

        employee.setCompany(getUserCompanyName(ctx));

        try {
            String userBranchName = getUserBranchName(ctx);
            if (!isEmpty(userBranchName)) {
                employee.setBranch(getBranchRepository().findByName(userBranchName));
            }
        } catch (Exception e) {
            LogUtils.logInfo("Error Updating User Details  - [" + username + "] - Error: " + e.getLocalizedMessage());
        }

        try {
            String userDepartmentName = getUserDepartmentName(ctx);
            if (!isEmpty(userDepartmentName)) {
                employee.setDepartment(getDepartmentRepository().findByName(userDepartmentName));
            }
        } catch (Exception e) {
            LogUtils.logInfo("Error Updating User Details  - [" + username + "] - Error: " + e.getLocalizedMessage());
        }

        employee.setEmailAddress(getUserEmailAddress(ctx));
        employee.setJobTitle(getUserJobTitle(ctx));

        employee.setStreetAddress(getUserStreetAddress(ctx));
        employee.setCity(getUserCity(ctx));
        employee.setState(getUserState(ctx));
        employee.setZipCode(getUserZipCode(ctx));
        employee.setCountry(getUserCountry(ctx));

        employee.setBusinessPhone(getUserBusinessPhone(ctx));
        employee.setMobilePhone(getUserMobilePhone(ctx));

        try {
            String managerFullName = getUserManagerFullName(ctx);
            if (!isEmpty(managerFullName)) {
                employee.setManager(getEmployeeRepository().findByName(managerFullName));
            }
        } catch (Exception e) {
            LogUtils.logInfo("Error Updating User Details  - [" + username + "] - Error: " + e.getLocalizedMessage());
        }

        employee.setEnabled(systemSecurityUser.getEnabled());
        employee.setSystemSecurityUser(systemSecurityUser);

        return getEmployeeRepository().save(employee);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<Employee> getEmployeeRepository() {
        return employeeRepository;
    }

    public void setEmployeeRepository(NamedEntityRepository<Employee> employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public NamedEntityRepository<Branch> getBranchRepository() {
        return branchRepository;
    }

    public void setBranchRepository(NamedEntityRepository<Branch> branchRepository) {
        this.branchRepository = branchRepository;
    }

    public NamedEntityRepository<Department> getDepartmentRepository() {
        return departmentRepository;
    }

    public void setDepartmentRepository(NamedEntityRepository<Department> departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public SystemSecurityUserRepository getSystemSecurityUserRepository() {
        return systemSecurityUserRepository;
    }

    public void setSystemSecurityUserRepository(SystemSecurityUserRepository systemSecurityUserRepository) {
        this.systemSecurityUserRepository = systemSecurityUserRepository;
    }

    public NamedEntityRepository<SystemSecurityRole> getSystemSecurityRoleRepository() {
        return systemSecurityRoleRepository;
    }

    public void setSystemSecurityRoleRepository(NamedEntityRepository<SystemSecurityRole> systemSecurityRoleRepository) {
        this.systemSecurityRoleRepository = systemSecurityRoleRepository;
    }
}
