package org.apache.jsp.WEB_002dINF.pages.device;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class deviceMgtForm_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("    var deviceMgtForm_Store = Ext.create('Ext.data.Store', {\r\n");
      out.write("        fields: ['id', 'mac', 'serialNumber', 'modelId', 'modelName',\r\n");
      out.write("            'provinceId', 'province', 'districtId', 'district', 'ram', 'cpu', 'rom',\r\n");
      out.write("            'firmwareVersion', 'firmwareStatus', 'cpeStatus', 'stbUsername', 'stbPassword', 'ipAddress', 'homepageUrl', 'upgradeUrl','connectionReq'],\r\n");
      out.write("        proxy: {\r\n");
      out.write("            type: 'ajax',\r\n");
      out.write("            url: 'loadDevice',\r\n");
      out.write("            reader: {\r\n");
      out.write("                root: 'list',\r\n");
      out.write("                type: 'json',\r\n");
      out.write("                totalProperty: 'totalCount'\r\n");
      out.write("            }\r\n");
      out.write("        },\r\n");
      out.write("        pageSize: 20,\r\n");
      out.write("        autoLoad: false,\r\n");
      out.write("        listeners: {\r\n");
      out.write("            'beforeload': function(store, options) {\r\n");
      out.write("                this.proxy.extraParams.deviceMAC = deviceMAC_Search.getValue();\r\n");
      out.write("                this.proxy.extraParams.deviceSerialNumber = deviceSerialNumber_Search.getValue();\r\n");
      out.write("                this.proxy.extraParams.deviceStatus = deviceStatus_Search.getValue();\r\n");
      out.write("                this.proxy.extraParams.deviceFirmwareStatus = deviceFirmwareStatus_Search.getValue();\r\n");
      out.write("                this.proxy.extraParams.deviceModel = deviceModel_Search.getValue();\r\n");
      out.write("                this.proxy.extraParams.deviceFirmware = deviceFirmware_Search.getRawValue();\r\n");
      out.write("                this.proxy.extraParams.deviceProvince = deviceProvince_Search.getValue();\r\n");
      out.write("                this.proxy.extraParams.deviceDistrict = deviceDistrict_Search.getValue();\r\n");
      out.write("            },\r\n");
      out.write("            load: function(store, records, success) {\r\n");
      out.write("                if (!success) {\r\n");
      out.write("                    var msgr = 'LoadDevice fail !!!';\r\n");
      out.write("                    Ext.MessageBox.show({title: 'Warning', msg: msgr, buttons: Ext.MessageBox.OK, icon: Ext.Msg.WARNING});\r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("        }\r\n");
      out.write("    });\r\n");
      out.write("    deviceMgtForm_Store.loadPage(1, {});\r\n");
      out.write("    var smDevice = Ext.create('Ext.selection.CheckboxModel', {\r\n");
      out.write("        checkOnly: true\r\n");
      out.write("    });\r\n");
      out.write("    var deviceMgtForm_Panel = Ext.create('Ext.grid.Panel', {\r\n");
      out.write("        title: '");
      if (_jspx_meth_fmt_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("        store: deviceMgtForm_Store,\r\n");
      out.write("        selType: 'cellmodel',\r\n");
      out.write("        selModel: smDevice,\r\n");
      out.write("        id: 'deviceMgtForm_PanelId',\r\n");
      out.write("        tbar: [\r\n");
      out.write("            {\r\n");
      out.write("                text: '");
      if (_jspx_meth_fmt_005fmessage_005f1(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                icon: iconForder + 'delete.gif', // Use a URL in the icon config\t\t\t                \r\n");
      out.write("                handler: function() {\r\n");
      out.write("                    var idList = '';\r\n");
      out.write("                    var itemNo = 0;\r\n");
      out.write("                    selected = deviceMgtForm_Panel.getView().getSelectionModel().getSelection();\r\n");
      out.write("                    Ext.each(selected, function(item) {\r\n");
      out.write("                        itemNo++;\r\n");
      out.write("                        if (idList == '')\r\n");
      out.write("                            idList = idList + (item.data.id);\r\n");
      out.write("                        else\r\n");
      out.write("                            idList = idList + ',' + (item.data.id);\r\n");
      out.write("                    });\r\n");
      out.write("                    if (itemNo == 0)\r\n");
      out.write("                        return;\r\n");
      out.write("                    var msg = '");
      if (_jspx_meth_fmt_005fmessage_005f2(_jspx_page_context))
        return;
      out.write("';\r\n");
      out.write("                    Ext.MessageBox.confirm('");
      if (_jspx_meth_fmt_005fmessage_005f3(_jspx_page_context))
        return;
      out.write("', msg, function(btn) {\r\n");
      out.write("                        if (btn == 'yes')\r\n");
      out.write("                            deleteDevice(idList);\r\n");
      out.write("                        if (btn == 'no' || btn == 'cancel')\r\n");
      out.write("                            smDevice.deselectAll(true);\r\n");
      out.write("                    });\r\n");
      out.write("                }\r\n");
      out.write("            },\r\n");
      out.write("                {\r\n");
      out.write("                text: '");
      if (_jspx_meth_fmt_005fmessage_005f4(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                icon: iconForder + 'edit.gif', // Use a URL in the icon config\t\t\t                \r\n");
      out.write("                handler: function() {\r\n");
      out.write("                    upgradeFirmware();\r\n");
      out.write("                    \r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("        ],\r\n");
      out.write("        columns: [\r\n");
      out.write("            Ext.create('Ext.grid.RowNumberer', {\r\n");
      out.write("                header: 'No.',\r\n");
      out.write("                width: 30,\r\n");
      out.write("                align: 'center',\r\n");
      out.write("                sortable: false,\r\n");
      out.write("                renderer: function(v, p, record, rowIndex) {\r\n");
      out.write("                    if (this.rowspan) {\r\n");
      out.write("                        p.cellAttr = 'rowspan=\"' + this.rowspan + '\"';\r\n");
      out.write("                    }\r\n");
      out.write("                    return rowIndex + 1;\r\n");
      out.write("                }\r\n");
      out.write("            }),\r\n");
      out.write("            \r\n");
      out.write("            {\r\n");
      out.write("                text: 'Id',\r\n");
      out.write("                dataIndex: 'id',\r\n");
      out.write("                hidden: true,\r\n");
      out.write("                sortable: false,\r\n");
      out.write("            },\r\n");
      out.write("            {\r\n");
      out.write("                text: '");
      if (_jspx_meth_fmt_005fmessage_005f5(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                dataIndex: 'ipAddress',\r\n");
      out.write("                sortable: false,\r\n");
      out.write("                flex: .5,\r\n");
      out.write("                hidden: true,\r\n");
      out.write("                renderer: function(value, metadata) {\r\n");
      out.write("                    metadata.tdAttr = 'data-qtip=\"' + value + '\"';\r\n");
      out.write("                    return value;\r\n");
      out.write("                }\r\n");
      out.write("            },\r\n");
      out.write("            {\r\n");
      out.write("                text: '");
      if (_jspx_meth_fmt_005fmessage_005f6(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                dataIndex: 'serialNumber',\r\n");
      out.write("                flex: .5,\r\n");
      out.write("                sortable: false,\r\n");
      out.write("                renderer: function(value, metadata) {\r\n");
      out.write("                    metadata.tdAttr = 'data-qtip=\"' + value + '\"';\r\n");
      out.write("                    return value;\r\n");
      out.write("                }\r\n");
      out.write("            }, {\r\n");
      out.write("                text: '");
      if (_jspx_meth_fmt_005fmessage_005f7(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                dataIndex: 'modelName',\r\n");
      out.write("                flex: .5,\r\n");
      out.write("                sortable: false,\r\n");
      out.write("                renderer: function(value, metadata) {\r\n");
      out.write("                    metadata.tdAttr = 'data-qtip=\"' + value + '\"';\r\n");
      out.write("                    return value;\r\n");
      out.write("                }\r\n");
      out.write("            }, {\r\n");
      out.write("                text: '");
      if (_jspx_meth_fmt_005fmessage_005f8(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                dataIndex: 'connectionReq',\r\n");
      out.write("                flex: 1,\r\n");
      out.write("                sortable: false,\r\n");
      out.write("                renderer: function(value, metadata) {\r\n");
      out.write("                    metadata.tdAttr = 'data-qtip=\"' + value + '\"';\r\n");
      out.write("                    return value;\r\n");
      out.write("                }\r\n");
      out.write("            },{\r\n");
      out.write("                text: '");
      if (_jspx_meth_fmt_005fmessage_005f9(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                dataIndex: 'firmwareVersion',\r\n");
      out.write("                flex: 1,\r\n");
      out.write("                sortable: false,\r\n");
      out.write("                renderer: function(value, metadata) {\r\n");
      out.write("                    metadata.tdAttr = 'data-qtip=\"' + value + '\"';\r\n");
      out.write("                    return value;\r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("        ],\r\n");
      out.write("        listeners: {\r\n");
      out.write("        },\r\n");
      out.write("        dockedItems: [{\r\n");
      out.write("                xtype: 'pagingtoolbar',\r\n");
      out.write("                store: deviceMgtForm_Store, // same store GridPanel is using\r\n");
      out.write("                dock: 'bottom',\r\n");
      out.write("                displayInfo: true\r\n");
      out.write("            }]\r\n");
      out.write("    });\r\n");
      out.write("</script>");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(48,16) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f0.setKey("management.device.gridPanel.title");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(55,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f1.setKey("button.delete");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(70,31) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f2.setKey("message.delete.confirm");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(71,44) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f3.setKey("message.confirm");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(80,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f4.setKey("Upgrade Firmware");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(109,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f5.setKey("management.device.gridPanel.columns.ip");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(120,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f6.setKey("management.device.gridPanel.columns.serialNumber");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(129,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f7.setKey("management.device.gridPanel.columns.modelName");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(138,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f8.setKey("management.device.gridPanel.columns.connectionRequest");
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
    // /WEB-INF/pages/device/deviceMgtForm.jsp(147,23) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f9.setKey("management.device.gridPanel.columns.firmwareVersion");
    int _jspx_eval_fmt_005fmessage_005f9 = _jspx_th_fmt_005fmessage_005f9.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f9);
    return false;
  }
}
