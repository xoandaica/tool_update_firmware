package org.apache.jsp.WEB_002dINF.pages.common;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class sampleItem_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
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

      out.write("<script type=\"text/javascript\">\r\n");
      out.write("    Ext.Loader.setConfig({\r\n");
      out.write("        enabled: true,\r\n");
      out.write("        paths: {\r\n");
      out.write("            'Ext.ux': '/scripts/extjs/ux'\r\n");
      out.write("        }\r\n");
      out.write("    });\r\n");
      out.write("//    myMask = new Ext.LoadMask(Ext.getBody(), {\r\n");
      out.write("//        msg: \"Please wait...\"\r\n");
      out.write("//    });\r\n");
      out.write("    var iconForder = '../../../images/icons/';\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("    //Lib\r\n");
      out.write("    NMS_STB = {};\r\n");
      out.write("    NMS_STB.createTextField = function (fieldName, width, labelAlign, isReadOnly) {\r\n");
      out.write("        var textField = Ext.create('Ext.form.TextField', {\r\n");
      out.write("            fieldLabel: fieldName,\r\n");
      out.write("            width: width,\r\n");
      out.write("            labelWidth: 150,\r\n");
      out.write("            labelAlign: labelAlign,\r\n");
      out.write("            padding: 10,\r\n");
      out.write("            readOnly: isReadOnly\r\n");
      out.write("        });\r\n");
      out.write("        return textField;\r\n");
      out.write("    };\r\n");
      out.write("    NMS_STB.createBoxSelectedWithoutListener = function (fieldLabel, width, labelAlign, storeName, valueField, displayField, isMultiSelect, isReadOnly) {\r\n");
      out.write("        var boxSelected = Ext.create('Ext.ux.form.field.BoxSelect', {\r\n");
      out.write("            fieldLabel: fieldLabel,\r\n");
      out.write("            width: width,\r\n");
      out.write("            labelWidth: 150,\r\n");
      out.write("            padding: 10,\r\n");
      out.write("            labelAlign: labelAlign,\r\n");
      out.write("            store: storeName,\r\n");
      out.write("            valueField: valueField,\r\n");
      out.write("            displayField: displayField,\r\n");
      out.write("            readOnly: isReadOnly,\r\n");
      out.write("            multiSelect: isMultiSelect,\r\n");
      out.write("            queryMode: 'local'\r\n");
      out.write("        });\r\n");
      out.write("        return boxSelected;\r\n");
      out.write("    };\r\n");
      out.write("    NMS_STB.createNumberFieldValidate = function (fieldLabel, width, labelAlign, defaultValue, min, max, isReadOnly) {\r\n");
      out.write("        var numberField = Ext.create('Ext.form.TextField', {\r\n");
      out.write("            xtype: 'numberfield',\r\n");
      out.write("            fieldLabel: fieldLabel,\r\n");
      out.write("            width: width,\r\n");
      out.write("            labelWidth: 150,\r\n");
      out.write("            value: defaultValue,\r\n");
      out.write("            padding: 10,\r\n");
      out.write("            labelAlign: labelAlign,\r\n");
      out.write("            readOnly: isReadOnly,\r\n");
      out.write("            validator: function (val) {\r\n");
      out.write("                if (!Ext.isEmpty(val)) {\r\n");
      out.write("                    if (!Ext.isNumeric(val)) {\r\n");
      out.write("                        return \"Must be a number\";\r\n");
      out.write("                    } else if (parseInt(val) > max) {\r\n");
      out.write("                        return \"Must less than \" + max;\r\n");
      out.write("                    } else if (parseInt(val) < min) {\r\n");
      out.write("                        return \"Must greater than \" + min;\r\n");
      out.write("                    } else\r\n");
      out.write("                        return true;\r\n");
      out.write("                }\r\n");
      out.write("            }\r\n");
      out.write("        });\r\n");
      out.write("        return numberField;\r\n");
      out.write("    };\r\n");
      out.write("\r\n");
      out.write("    function checkFieldIsEmpty(val) {\r\n");
      out.write("        if ((val == null) || (val == \"\"))\r\n");
      out.write("            return true;\r\n");
      out.write("        else\r\n");
      out.write("            return false;\r\n");
      out.write("    }\r\n");
      out.write("  \r\n");
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
}
