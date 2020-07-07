/*
    配置直接写到dom上
    本组件语法继承validform组件
    dataType:s字符串 n数字 double|double2|amounts 是保留2位小数的数字  * 是必填  ipv4是ip地址
             m 手机号 p 座机  tel_p座机+手机号  e email  url网址
    需要在class 上加上 fill 才可以自动填充



 */
var fillFormExtends = {uiplugins:{}};
(function($,win,undef){

    var inputPlugin ={
        setVal:function(_node,_val){
            $(_node).val(_val);
        }
    };

    var FillForm=function(forms,settings,inited){
        var settings=$.extend({},FillForm.defaults,settings);
        var brothers=this;
        brothers.forms=forms;
        brothers.objects=[];
        forms.each(function(){
            if(this.fillform_inited=="inited"){return true;}
            this.fillform_inited="inited";
            var curform = this;
            curform.fill_settings=$.extend({},settings);
            curform.nodes = [];
            $(this).find('.fill').each(function(){
                var _node = this;
                // 判断是否是必填
                _node.required = FillForm.util.checkRequired.call(this);
                // 判断当前是个什么类型 如果没有自定义类型则统一按照input来
                for (let uipluginKey in fillFormExtends.uiplugins) {
                    var _plugin = fillFormExtends.uiplugins[uipluginKey];
                    //判断当前类型
                    if(_plugin.matching(this)){
                        _node.plugin = _plugin;
                    }
                }
                if(!_node.plugin){
                    _node.plugin = inputPlugin;
                }
                curform.nodes.push(_node);
            });
            $(this).append('<a href="javascript:void(0)" class="fillAll">填充所有</a><a href="javascript:void(0)" class="fillRequired">填充必填</a>');
            $('.fillAll').click(function(){
                FillForm.util.fillData.call(this,curform.nodes,false);
            });
            $('.fillRequired').click(function(){
                FillForm.util.fillData.call(this,curform.nodes,true);
            });
        });
    }

    FillForm.defaults={

    }

    FillForm.util={
        fillData:function(_nodes,_onlyRequired){
            _nodes.forEach(function(_node){
                if(_node.required || !_onlyRequired){
                    var _val = null;
                    if(_node.plugin && _node.plugin.getDftVal){
                        _val = _node.plugin.getDftVal(_node);
                    }else{
                        _val = FillForm.util.getDftVal.call(_node);
                    }
                    if(_node.plugin && _node.plugin.setVal){
                        _node.plugin.setVal(_node,_val);
                    }else{
                        $(_node).val(_val);
                    }
                }
            });
        },
        getDftVal:function(){
            var _node = this;
            var _dataType = $(_node).attr('datatype');
            // 0- 190的随机数
            var _tempIndex = Math.ceil(Math.random()*190);
            if(_dataType==='*'){
                return fill_form_data_ext.name[_tempIndex];
            }
            var _dataTypes = FillForm.util.getDataTypes.call(_node);
            if(FillForm.util.checkDataType.call(this,_dataTypes,'double') ||
                FillForm.util.checkDataType.call(this,_dataTypes,'double2') ||
                FillForm.util.checkDataType.call(this,_dataTypes,'amounts')){
                return '1.01';
            }

            if(FillForm.util.checkDataType.call(this,_dataTypes,'m') || FillForm.util.checkDataType.call(this,_dataTypes,'tel_p')){
                return fill_form_data_ext.mobile[_tempIndex];
            }
            if(FillForm.util.checkDataType.call(this,_dataTypes,'p') ){
                return '6840123';
            }
            if(FillForm.util.checkDataType.call(this,_dataTypes,'e') ){
                return fill_form_data_ext.email[_tempIndex];
            }
            if(FillForm.util.checkDataType.call(this,_dataTypes,'url') ){
                return 'http://temp.fhs.com';
            }
            if(FillForm.util.checkDataType.call(this,_dataTypes,'ip4') ){
                return '192.0.0.1';
            }
            if(FillForm.util.checkDataType.call(this,_dataTypes,'idNumber') ){
                return fill_form_data_ext.idNumber[_tempIndex];
            }
            return 1;
        },
        getDataTypes:function(){
            var _node = this;
            var _dataType = $(_node).attr('datatype');
            if(!_dataType){
                return [];
            }
            var _dataTypes = _dataType.split('|');
            for(var i=0;i<_dataTypes.length;i++){
                _dataTypes[i] = _dataTypes[i].trim();
            }
            return _dataTypes;
        },
        checkDataType:function(_dataTypes,_target){
            for(var i=0;i<_dataTypes.length;i++){
                if(_dataTypes[i]===_target){
                    return true;
                }
            }
            return false;
        },
        //判断一个对象是否必填
        checkRequired:function(){
            var _node = this;
            var _dataType = $(_node).attr('datatype');
            // 为空为非必填
            if(!_dataType){
                return false;
            }
            // 非空为必填
            if(_dataType==='*'){
                return true;
            }
            // 如果包含| 又包含 empty的话则是可为空 否则不能为空
            if(_dataType.indexOf('|')!=-1 && _dataType.indexOf('empty')!=-1){
                return false;
            }
            return true;
        },
    }

    $.fn.FillForm=function(settings){
        return new FillForm(this,settings);
    };
})(jQuery,window);