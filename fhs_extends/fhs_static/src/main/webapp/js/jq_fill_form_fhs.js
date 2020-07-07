fillFormExtends.uiplugins.Wdate={
    getDftVal:function(_node){
        var _html = _node.outerHTML;
        if(_html.indexOf('yyyy-MM-dd')!=-1){
            return '2020-07-07';
        }else if(_html.indexOf('yyyy-MM')!=-1){
            return '2020-07';
        }else{
            return '2020';
        }
    },
    matching:function(_node){
        $(_node).hasClass('Wdate');
    }
};

fillFormExtends.uiplugins.combobox={
    getDftVal:function(_node){
       var _datas = $(_node).combobox('getData');
       if(_datas &&_datas.length>0){
           return _datas[0][$(_node).combobox('options').valueField]
       }
    },
    setVal:function(_node,_val){
        $(_node).combobox('setValue',_val);
    },
    matching:function(_node){
        $(_node).hasClass('easyui-combobox');
    }
};

