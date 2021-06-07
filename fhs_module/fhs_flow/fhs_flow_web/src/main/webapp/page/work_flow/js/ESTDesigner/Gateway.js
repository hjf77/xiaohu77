ESTDesigner.event.BaseEvent=draw2d.shape.basic.Image.extend({
	init:function(attr){
		this._super($.extend({
			id:ESTDesigner.tool.UUID.create(),
			width:40,
			height:40,
			resizeable:false
		},attr));
	},
	toXML:function(){
		return "";
	},
	toBpmnDI:function(){
		var w=this.getWidth();
		var h=this.getHeight();
		var x=this.getAbsoluteX();
		var y=this.getAbsoluteY();
		var xml='<bpmndi:BPMNShape bpmnElement="'+this.id+'" id="BPMNShape_'+this.id+'">\n';
		xml=xml+'<omgdc:Bounds height="'+h+'" width="'+w+'" x="'+x+'" y="'+y+'"/>\n';
		xml=xml+'</bpmndi:BPMNShape>\n';
		return xml;
	}
});
ESTDesigner.gateway.ParallelGateway=ESTDesigner.event.BaseEvent.extend({
	init:function(attr){
		this._super($.extend({
			path:"/page/work_flow/js/ESTDesigner/icons/type.gateway.parallel.png"
				},attr));
		this.createPort("hybrid", new draw2d.layout.locator.LeftLocator());
		this.createPort("hybrid", new draw2d.layout.locator.RightLocator());
		this.createPort("hybrid", new draw2d.layout.locator.BottomLocator());
		this.createPort("hybrid", new draw2d.layout.locator.TopLocator());
	},
	toXML:function(){
		if(this.id.indexOf("parallelGateway")<0){
			this.id = "parallelGateway"+this.id;
		}
		var xml='<parallelGateway id="'+this.id+'" name=" "></parallelGateway>\n';
		return xml;
	}
});
ESTDesigner.gateway.ExclusiveGateway=ESTDesigner.event.BaseEvent.extend({
	init:function(attr){
		this._super($.extend({
			path:"/page/work_flow/js/ESTDesigner/icons/type.gateway.exclusive.png"
				},attr));
		this.createPort("hybrid", new draw2d.layout.locator.LeftLocator());
		this.createPort("hybrid", new draw2d.layout.locator.RightLocator());
		this.createPort("hybrid", new draw2d.layout.locator.BottomLocator());
		this.createPort("hybrid", new draw2d.layout.locator.TopLocator());
	},
	toXML:function(){
		if(this.id.indexOf("exclusiveGateway")<0){
			this.id  = "exclusiveGateway"+this.id;
		}
		var xml='<exclusiveGateway id="'+this.id+'" name=" "></exclusiveGateway>\n';
		return xml;
	}
});