package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;

@XmlType
public class SimpleObject {

    private int id;
    private String name;
    private String parameter;
    private String value;
    private String type;
    private String path;

    public SimpleObject(String parameter, String value, String type) {
        this.parameter = parameter;
        this.value = value;
        this.type = type;
    }

    public SimpleObject(String parameter, String name, String value, String type) {
        this.parameter = parameter;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public SimpleObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SimpleObject cloneObject() {
        SimpleObject temp = new SimpleObject();
        temp.setId(id);
        temp.setName(name);
        temp.setParameter(parameter);
        temp.setType(type);
        //temp.setValue(value);

        return temp;
    }

    public String getFullParameterName() {
        if (StringUtils.isNotBlank(path)) {
            return StringUtils.stripEnd(path, ".") + "." + parameter;
        } else {
            return parameter;
        }
    }
}
