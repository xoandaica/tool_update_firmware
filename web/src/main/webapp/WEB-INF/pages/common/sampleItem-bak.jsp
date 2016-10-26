<%-- 
    Document   : sampleItem
    Created on : May 24, 2015, 6:23:24 PM
    Author     : Dell
--%>


<script type="text/javascript">
     <c:set var="context2" value="${pageContext.request.contextPath}" />
    Ext.Loader.setConfig({
        enabled: true,
        paths: {
            'Ext.ux': '${context2}/scripts/extjs/ux'
        }
    });
//    myMask = new Ext.LoadMask(Ext.getBody(), {
//        msg: "Please wait..."
//    });
    var iconForder = '${context2}/images/icons/';


    //Lib
    NMS_STB = {};
    NMS_STB.createTextField = function (fieldName, width, labelAlign, isReadOnly) {
        var textField = Ext.create('Ext.form.TextField', {
            fieldLabel: fieldName,
            width: width,
            labelWidth: 150,
            labelAlign: labelAlign,
            padding: 10,
            readOnly: isReadOnly
        });
        return textField;
    };
    NMS_STB.createBoxSelectedWithoutListener = function (fieldLabel, width, labelAlign, storeName, valueField, displayField, isMultiSelect, isReadOnly) {
        var boxSelected = Ext.create('Ext.ux.form.field.BoxSelect', {
            fieldLabel: fieldLabel,
            width: width,
            labelWidth: 150,
            padding: 10,
            labelAlign: labelAlign,
            store: storeName,
            valueField: valueField,
            displayField: displayField,
            readOnly: isReadOnly,
            multiSelect: isMultiSelect,
            queryMode: 'local'
        });
        return boxSelected;
    };
    NMS_STB.createNumberFieldValidate = function (fieldLabel, width, labelAlign, defaultValue, min, max, isReadOnly) {
        var numberField = Ext.create('Ext.form.TextField', {
            xtype: 'numberfield',
            fieldLabel: fieldLabel,
            width: width,
            labelWidth: 150,
            value: defaultValue,
            padding: 10,
            labelAlign: labelAlign,
            readOnly: isReadOnly,
            validator: function (val) {
                if (!Ext.isEmpty(val)) {
                    if (!Ext.isNumeric(val)) {
                        return "Must be a number";
                    } else if (parseInt(val) > max) {
                        return "Must less than " + max;
                    } else if (parseInt(val) < min) {
                        return "Must greater than " + min;
                    } else
                        return true;
                }
            }
        });
        return numberField;
    };

    function checkFieldIsEmpty(val) {
        if ((val == null) || (val == ""))
            return true;
        else
            return false;
    }
  
</script>
