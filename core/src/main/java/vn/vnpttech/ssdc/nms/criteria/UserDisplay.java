package vn.vnpttech.ssdc.nms.criteria;

// default package
// Generated Jan 3, 2013 1:59:14 PM by Hibernate Tools 3.4.0.CR1

public class UserDisplay extends BaseCriterialObj{

	private String accountEnabled;
	private Boolean accountExpired;

	private Boolean accountLocked;
	private String arrayRole;
	private String confirmPassword;
	private Long contactId;
	private String contactName;
	private Boolean credentialsExpired;
	private Long[] deleteUserIds;
	private Long districtId;
	private String districtName;
	private Boolean enabled;
	private String firstName;
	private Long groupRoleId;
	private Long id;
	private String lastName;

	private String password;
	private String passwordHint;
	private Long provinceId;
	private String provinceName;
	private Long[] roleArray;
	private String department;
	private String description;

	private String provinceIds;
	private String districtIds;

	private String sortDirection;
	private String sortProperty;
	
	
	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	public String getProvinceIds() {
		return provinceIds;
	}

	public void setProvinceIds(String provinceIds) {
		this.provinceIds = provinceIds;
	}

	public String getDistrictIds() {
		return districtIds;
	}

	public void setDistrictIds(String districtIds) {
		this.districtIds = districtIds;
	}

	private String roles;

	private String status;

	private String username;

	private Integer version;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDisplay() {
	}

	public UserDisplay(Long id, Integer version, String confirmPassword, Boolean accountExpired, Boolean accountLocked,
			Boolean credentialsExpired, String accountEnabled, String firstName, String lastName, String password,
			String passwordHint, String username, Long provinceId, Long districtId, Long contactId, Long groupRoleId,
			String provinceName, String districtName, String arrayRole, String contactName, String status, Long[] deleteUserIds,
			Boolean enabled, String roles, Long[] roleArray) {
		super();
		this.id = id;
		this.version = version;
		this.confirmPassword = confirmPassword;
		this.accountExpired = accountExpired;
		this.accountLocked = accountLocked;
		this.credentialsExpired = credentialsExpired;
		this.accountEnabled = accountEnabled;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.passwordHint = passwordHint;
		this.username = username;
		this.provinceId = provinceId;
		this.districtId = districtId;
		this.contactId = contactId;
		this.groupRoleId = groupRoleId;
		this.provinceName = provinceName;
		this.districtName = districtName;
		this.arrayRole = arrayRole;
		this.contactName = contactName;
		this.status = status;
		this.deleteUserIds = deleteUserIds;
		this.enabled = enabled;
		this.roles = roles;
		this.roleArray = roleArray;
	}

	public String getAccountEnabled() {
		return accountEnabled;
	}

	public Boolean getAccountExpired() {
		return accountExpired;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public String getArrayRole() {
		return arrayRole;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public Long getContactId() {
		return contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	public Long[] getDeleteUserIds() {
		return deleteUserIds;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public Long getGroupRoleId() {
		return groupRoleId;
	}

	public Long getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordHint() {
		return passwordHint;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public Long[] getRoleArray() {
		return roleArray;
	}

	public String getRoles() {
		return roles;
	}

	public String getStatus() {
		return status;
	}

	public String getUsername() {
		return username;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAccountEnabled(String accountEnabled) {
		this.accountEnabled = accountEnabled;
	}

	public void setAccountExpired(Boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setArrayRole(String arrayRole) {
		this.arrayRole = arrayRole;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public void setDeleteUserIds(Long[] deleteUserIds) {
		this.deleteUserIds = deleteUserIds;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGroupRoleId(Long groupRoleId) {
		this.groupRoleId = groupRoleId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public void setRoleArray(Long[] roleArray) {
		this.roleArray = roleArray;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
