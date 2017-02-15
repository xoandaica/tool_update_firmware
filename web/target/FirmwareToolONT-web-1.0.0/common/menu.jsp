<%@ include file="/common/taglibs.jsp"%>

<menu:useMenuDisplayer name="Velocity" config="navbarMenu.vm"
                       permissions="rolesAdapter">
    <div class="nav-collapse collapse">
        <ul class="nav">
            <%--<menu:displayMenu name="Home"/>--%>
            <menu:displayMenu name="DeviceMenu"/>
            <menu:displayMenu name="AdminMenu"/>
            <menu:displayMenu name="ManagementMenu"/>
            <menu:displayMenu name="UserMenu"/>
            <menu:displayMenu name="reportMenu" />
            <menu:displayMenu name="Logout"/>
        </ul>
    </div>
</menu:useMenuDisplayer>
