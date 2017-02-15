package org.apache.jsp.WEB_002dINF.pages.device;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class deviceMgtPopupForm_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<script>\r\n");
      out.write("    var deviceId = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        fieldLabel: 'Id',\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write("    var deviceMAC = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        readOnly: true,\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write("    var deviceSerialNumber = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f1(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        readOnly: true,\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write(" \r\n");
      out.write("    var deviceModel = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f2(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        readOnly: true,\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write("    var deviceUsername = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f3(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        readOnly: true,\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write("    var devicePassword = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f4(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        readOnly: true,\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write("    var deviceHomepageUrl = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f5(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        readOnly: true,\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write("     var deviceUpgradeUrl = Ext.create('Ext.form.TextField', {\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f6(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        readOnly: true,\r\n");
      out.write("        anchor: '99%'\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("    var deviceDistrict_Store = Ext.create('Ext.data.Store', {\r\n");
      out.write("        fields: ['id', 'name'],\r\n");
      out.write("        proxy: {\r\n");
      out.write("            type: 'ajax',\r\n");
      out.write("            url: 'loadDistrictByProvinceIdCommon',\r\n");
      out.write("            reader: {\r\n");
      out.write("                root: 'listDistrict',\r\n");
      out.write("                type: 'json'\r\n");
      out.write("            }\r\n");
      out.write("        },\r\n");
      out.write("        autoLoad: false\r\n");
      out.write("    });\r\n");
      out.write("    var deviceDistrict = Ext.create('Ext.ux.form.field.BoxSelect', {\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f7(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        anchor: '99%',\r\n");
      out.write("        multiSelect: false,\r\n");
      out.write("        displayField: 'name',\r\n");
      out.write("        valueField: 'id',\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        store: deviceDistrict_Store,\r\n");
      out.write("        queryMode: 'local'\r\n");
      out.write("    });\r\n");
      out.write("    var deviceProvince_Store = Ext.create('Ext.data.Store', {\r\n");
      out.write("        fields: ['id', 'name'],\r\n");
      out.write("        proxy: {\r\n");
      out.write("            type: 'ajax',\r\n");
      out.write("            url: 'loadProvinceCommon',\r\n");
      out.write("            reader: {\r\n");
      out.write("                root: 'listProvince',\r\n");
      out.write("                type: 'json'\r\n");
      out.write("            }\r\n");
      out.write("        },\r\n");
      out.write("        autoLoad: true\r\n");
      out.write("    });\r\n");
      out.write("    var deviceProvince = Ext.create('Ext.ux.form.field.BoxSelect', {\r\n");
      out.write("        fieldLabel: '");
      if (_jspx_meth_fmt_005fmessage_005f8(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        labelAlign: 'left',\r\n");
      out.write("        anchor: '99%',\r\n");
      out.write("        multiSelect: false,\r\n");
      out.write("        displayField: 'name',\r\n");
      out.write("        valueField: 'id',\r\n");
      out.write("        width: 550,\r\n");
      out.write("        labelWidth: 150,\r\n");
      out.write("        padding: 10,\r\n");
      out.write("        store: deviceProvince_Store,\r\n");
      out.write("        queryMode: 'local',\r\n");
      out.write("        listeners: {\r\n");
      out.write("            'change': function(combo, newValue, oldValue, eOpts) {\r\n");
      out.write("                deviceDistrict.setValue(null);\r\n");
      out.write("                deviceDistrict_Store.removeAll();\r\n");
      out.write("                if (deviceProvince.getValue() != null) {\r\n");
      out.write("                    deviceDistrict_Store.getProxy().extraParams = {\r\n");
      out.write("                        provinceId: deviceProvince.getValue()\r\n");
      out.write("                    };\r\n");
      out.write("                    deviceDistrict_Store.load();\r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("        }\r\n");
      out.write("    });\r\n");
      out.write("    var saveBnt = Ext.create('Ext.Button', {\r\n");
      out.write("        text: '");
      if (_jspx_meth_fmt_005fmessage_005f9(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        handler: function() {\r\n");
      out.write("            if (validateDeviceMgtPopupForm() != '') {\r\n");
      out.write("                Ext.MessageBox.alert('");
      if (_jspx_meth_fmt_005fmessage_005f10(_jspx_page_context))
        return;
      out.write("', validateDeviceMgtPopupForm(), function() {\r\n");
      out.write("                });\r\n");
      out.write("                return;\r\n");
      out.write("            }\r\n");
      out.write("            saveDevice(actionType, deviceId.getValue(), deviceProvince.getValue(), deviceDistrict.getValue());\r\n");
      out.write("        }\r\n");
      out.write("    });\r\n");
      out.write("    var cancelBnt = Ext.create('Ext.Button', {\r\n");
      out.write("        text: '");
      if (_jspx_meth_fmt_005fmessage_005f11(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        handler: function() {\r\n");
      out.write("            deviceMgtPopupForm.hide();\r\n");
      out.write("        }\r\n");
      out.write("    });\r\n");
      out.write("   \r\n");
      out.write("    var deviceMgtPopupForm = Ext.create('Ext.window.Window', {\r\n");
      out.write("        closeAction: 'show',\r\n");
      out.write("        autoHeight: true,\r\n");
      out.write("        autoWidth: true,\r\n");
      out.write("        autoScroll: true,\r\n");
      out.write("        layout: 'vbox',\r\n");
      out.write("        items: [deviceMAC, deviceSerialNumber, deviceModel, deviceUsername,devicePassword,deviceHomepageUrl,deviceUpgradeUrl,deviceProvince, deviceDistrict],\r\n");
      out.write("        buttons: [saveBnt, cancelBnt]\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_fmt_005fmessage_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f0.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f0.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(22,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f0.setKey("management.device.gridPanel.columns.mac");
    int _jspx_eval_fmt_005fmessage_005f0 = _jspx_th_fmt_005fmessage_005f0.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f1.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f1.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(31,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f1.setKey("management.device.gridPanel.columns.serialNumber");
    int _jspx_eval_fmt_005fmessage_005f1 = _jspx_th_fmt_005fmessage_005f1.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f2.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f2.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(41,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f2.setKey("management.device.gridPanel.columns.modelName");
    int _jspx_eval_fmt_005fmessage_005f2 = _jspx_th_fmt_005fmessage_005f2.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f3 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f3.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f3.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(50,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f3.setKey("management.device.gridPanel.columns.username");
    int _jspx_eval_fmt_005fmessage_005f3 = _jspx_th_fmt_005fmessage_005f3.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f3);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f4 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f4.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f4.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(59,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f4.setKey("management.device.gridPanel.columns.password");
    int _jspx_eval_fmt_005fmessage_005f4 = _jspx_th_fmt_005fmessage_005f4.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f4);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f5 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f5.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f5.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(68,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f5.setKey("management.device.gridPanel.columns.homepageUrl");
    int _jspx_eval_fmt_005fmessage_005f5 = _jspx_th_fmt_005fmessage_005f5.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f5);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f6 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f6.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f6.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(77,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f6.setKey("management.device.gridPanel.columns.upgradeUrl");
    int _jspx_eval_fmt_005fmessage_005f6 = _jspx_th_fmt_005fmessage_005f6.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f6);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f7 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f7.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f7.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(96,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f7.setKey("management.device.gridPanel.columns.district");
    int _jspx_eval_fmt_005fmessage_005f7 = _jspx_th_fmt_005fmessage_005f7.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f7);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f8 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f8.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f8.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(121,21) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f8.setKey("management.device.gridPanel.columns.province");
    int _jspx_eval_fmt_005fmessage_005f8 = _jspx_th_fmt_005fmessage_005f8.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f8);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f9 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f9.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f9.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(146,15) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f9.setKey("button.save");
    int _jspx_eval_fmt_005fmessage_005f9 = _jspx_th_fmt_005fmessage_005f9.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f9);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f10 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f10.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f10.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(149,38) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f10.setKey("message.error");
    int _jspx_eval_fmt_005fmessage_005f10 = _jspx_th_fmt_005fmessage_005f10.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f10);
    return false;
  }

  private boolean _jspx_meth_fmt_005fmessage_005f11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  fmt:message
    org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f11 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
    _jspx_th_fmt_005fmessage_005f11.setPageContext(_jspx_page_context);
    _jspx_th_fmt_005fmessage_005f11.setParent(null);
    // /WEB-INF/pages/device/deviceMgtPopupForm.jsp(157,15) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f11.setKey("button.cancel");
    int _jspx_eval_fmt_005fmessage_005f11 = _jspx_th_fmt_005fmessage_005f11.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f11);
    return false;
  }
}
