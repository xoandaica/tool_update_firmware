package org.apache.jsp.WEB_002dINF.pages.personal;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class editProfileLayout_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("    Ext.onReady(function () {\r\n");
      out.write("        Ext.tip.QuickTipManager.init();\r\n");
      out.write("        Ext.create('Ext.Viewport', {\r\n");
      out.write("            title: 'Ext Layout Browser',\r\n");
      out.write("            hideCollapseTool: true,\r\n");
      out.write("            titleCollapse: true,\r\n");
      out.write("            layout: {\r\n");
      out.write("                type: 'border',\r\n");
      out.write("                padding: 1\r\n");
      out.write("            },\r\n");
      out.write("            collapsible: true,\r\n");
      out.write("            defaults: {\r\n");
      out.write("                collapsible: true,\r\n");
      out.write("                split: true\r\n");
      out.write("            },\r\n");
      out.write("            items: [\r\n");
      out.write("                Ext.create('Ext.tab.Panel',\r\n");
      out.write("                        {\r\n");
      out.write("                            region: 'center', // a center region is ALWAYS required for border layout\r\n");
      out.write("                            deferredRender: false,\r\n");
      out.write("                            margins: '45 0 30 0',\r\n");
      out.write("                            activeTab: 0, // first tab initially active\r\n");
      out.write("                            items: [\r\n");
      out.write("                                {\r\n");
      out.write("                                    contentEl: 'tabs-1',\r\n");
      out.write("                                    id: 'tabs1Id',\r\n");
      out.write("                                    title: '");
      if (_jspx_meth_fmt_005fmessage_005f0(_jspx_page_context))
        return;
      out.write("',\r\n");
      out.write("                                    autoScroll: true\r\n");
      out.write("                                }\r\n");
      out.write("                            ]\r\n");
      out.write("                        })\r\n");
      out.write("            ],\r\n");
      out.write("            listeners: {\r\n");
      out.write("                resize: function (width, height, oldWidth, oldHeight, eOpts) {\r\n");
      out.write("                    setTimeout(function () {\r\n");
      out.write("                        //DO SOMETHING\r\n");
      out.write("                        Ext.getCmp(\"editProfileForm_PanelId\").setHeight(Ext.getCmp(\"tabs1Id\").getHeight());\r\n");
      out.write("                        editProfileForm_Panel.doLayout();\r\n");
      out.write("                    }, 1);\r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("        editProfileForm_Panel.render('editProfilePanel');\r\n");
      out.write("        //LoadData\r\n");
      out.write("    });\r\n");
      out.write("\r\n");
      out.write("\r\n");
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
    // /WEB-INF/pages/personal/editProfileLayout.jsp(35,44) name = key type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_fmt_005fmessage_005f0.setKey("management.user.formTitle");
    int _jspx_eval_fmt_005fmessage_005f0 = _jspx_th_fmt_005fmessage_005f0.doStartTag();
    if (_jspx_th_fmt_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
    return false;
  }
}
