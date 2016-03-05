function AutoSuggestControl(oTextbox, oProvider)
{
    this.provider = oProvider;
    this.textbox = oTextbox;
    this.cur = -1;
	this.layer = null;
    this.init();
}

AutoSuggestControl.prototype.init = function ()
{
    var oThis = this;
    this.textbox.onkeyup = function (oEvent)
    {
        if (!oEvent)
        {
            oEvent = window.event;
        } 
        oThis.handleKeyUp(oEvent);
    };
    this.textbox.onkeydown = function (oEvent)
    {
        if (!oEvent) {
            oEvent = window.event;
        } 
        oThis.handleKeyDown(oEvent);
    };
    this.textbox.onblur = function ()
    {
        oThis.layer.style.visibility = "hidden";
    };
    this.createDropDown();
};

AutoSuggestControl.prototype.createDropDown = function ()
{
    this.layer = document.createElement("div");
    this.layer.className = "suggestions";
    this.layer.style.visibility = "hidden";
    this.layer.style.width = this.textbox.offsetWidth;
    document.body.appendChild(this.layer);
    var oThis = this;
    this.layer.onmousedown = this.layer.onmouseup = this.layer.onmouseover = function (oEvent)
    {
        oEvent = oEvent || window.event;
        oTarget = oEvent.target || oEvent.srcElement;
        if (oEvent.type == "mousedown")
        {
            oThis.textbox.value = oTarget.firstChild.nodeValue;
            oThis.layer.style.visibility = "hidden";
        }
        else if (oEvent.type == "mouseover")
        {
            oThis.highlightSuggestion(oTarget);
        }
        else
        {
            oThis.textbox.focus();
        }
    };

};

AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode)
{
    for (var i=0; i < this.layer.childNodes.length; i++)
    {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode)
        {
            oNode.className = "current"
        } 
        else if (oNode.className == "current")
        {
            oNode.className = "";
        }
    }
};

AutoSuggestControl.prototype.showSuggestions = function (aSuggestions)
{
    var oDiv = null;
    this.layer.innerHTML = "";
    for (var i=0; i < aSuggestions.length; i++)
    {
        oDiv = document.createElement("div");
        oDiv.appendChild(document.createTextNode(aSuggestions[i]));
        this.layer.appendChild(oDiv);
    }
    var oNode = this.textbox;
    var left = 0;
    var top = 0;
    while(oNode.tagName != "BODY")
    {
        left += oNode.offsetLeft;
        top += oNode.offsetTop;
        oNode = oNode.offsetParent; 
    }
    this.layer.style.left = left + "px";
    this.layer.style.top = (top + this.textbox.offsetHeight) + "px";
    this.layer.style.visibility = "visible";
};

AutoSuggestControl.prototype.handleKeyUp = function (oEvent) {

    var iKeyCode = oEvent.keyCode;
    if (iKeyCode == 8 || iKeyCode == 46) 
    {
        this.provider.requestSuggestions(this, false);

    } else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {

    } else {
        this.provider.requestSuggestions(this, true);
    }
};

AutoSuggestControl.prototype.handleKeyDown = function (oEvent)
{
    switch(oEvent.keyCode) {
        case 38:
            this.previousSuggestion();
            break;
        case 40:
            this.nextSuggestion();
            break;
        case 13:
            this.layer.style.visibility = "hidden";
            break;
    }
};

AutoSuggestControl.prototype.nextSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length-1) {
        var oNode = cSuggestionNodes[++this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue; 
    }
};

AutoSuggestControl.prototype.previousSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur > 0) {
        var oNode = cSuggestionNodes[--this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue; 
    }
};

function SuggestionProvider()
{
}

SuggestionProvider.prototype.requestSuggestions = function(oAutoSuggestControl)
{
    var xmlA;
    if (window.ActiveXObject)
    {
        xmlA = new ActiveXObject("Microsoft.XMLHTTP");
    }
    else if (window.XMLHttpRequest)
    {
        xmlA = new XMLHttpRequest();
    }
    var text = oAutoSuggestControl.textbox.value;
    var query = "suggest?q=" + encodeURI(text);
    xmlA.open("GET", query);
    xmlA.onreadystatechange = function () 
    {
        var result = new Array();
        if (xmlA.readyState == 4)
        {
            var servletresponse = xmlA.responseXML.getElementsByTagName('CompleteSuggestion');
            for (var i = 0; i < servletresponse.length; i++) 
            {
                var temp = servletresponse[i].childNodes[0].getAttribute("data");
                result.push(temp);
            }
            if (result.length > 0)
            {
                oAutoSuggestControl.showSuggestions(result);
            } 
            else
            {
                oAutoSuggestControl.layer.style.visibility = "hidden";
            }
        }
    }
    xmlA.send();
};