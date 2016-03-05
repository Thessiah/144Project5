//build autosuggest control, using the textbox and the suggestor
function AutoSuggestControl(oTextbox, oProvider) {
	//current suggestion indicator
	this.cur = -1;
	//create a layer for dropdown menu
	this.layer = null;
    this.provider = oProvider;
    this.textbox = oTextbox;
    //call the initialization function
    this.init();
}

//hide dropdown
AutoSuggestControl.prototype.hideSuggestions = function () {
    this.layer.style.visibility = "hidden";
};

//highlight suggestion for mouseover
AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) {

    for (var i=0; i < this.layer.childNodes.length; i++) {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode) {
            oNode.className = "current"
        } else if (oNode.className == "current") {
            oNode.className = "";
        }
    }
};

//create the dropdown menu
AutoSuggestControl.prototype.createDropDown = function () {

    this.layer = document.createElement("div");
    this.layer.className = "suggestions";
    this.layer.style.visibility = "hidden";
    this.layer.style.width = this.textbox.offsetWidth;
    document.body.appendChild(this.layer);

    var oThis = this;

    this.layer.onmousedown = this.layer.onmouseup = 
    this.layer.onmouseover = function (oEvent) {
        oEvent = oEvent || window.event;
        oTarget = oEvent.target || oEvent.srcElement;

        if (oEvent.type == "mousedown") {
            oThis.textbox.value = oTarget.firstChild.nodeValue;
            oThis.hideSuggestions();
        } else if (oEvent.type == "mouseover") {
            oThis.highlightSuggestion(oTarget);
        } else {
            oThis.textbox.focus();
        }
    };

};

//line up the dropdown to the searchbox (left)
AutoSuggestControl.prototype.getLeft = function () {

    var oNode = this.textbox;
    var iLeft = 0;

    while(oNode.tagName != "BODY") {
        iLeft += oNode.offsetLeft;
        oNode = oNode.offsetParent; 
    }

    return iLeft;
};

//line up the dropdown to the searchbox (top)
AutoSuggestControl.prototype.getTop = function () {

    var oNode = this.textbox;
    var iTop = 0;

    while(oNode.tagName != "BODY") {
        iTop += oNode.offsetTop;
        oNode = oNode.offsetParent; 
    }

    return iTop;
};

//looks at the array of suggestions and puts them into the dropdown list
AutoSuggestControl.prototype.showSuggestions = function (aSuggestions) {

    var oDiv = null;
    this.layer.innerHTML = "";

    for (var i=0; i < aSuggestions.length; i++) {
        oDiv = document.createElement("div");
        oDiv.appendChild(document.createTextNode(aSuggestions[i]));
        this.layer.appendChild(oDiv);
    }

    this.layer.style.left = this.getLeft() + "px";
    this.layer.style.top = (this.getTop()+this.textbox.offsetHeight) + "px";
    this.layer.style.visibility = "visible";
};

//select specific characters in text, for both IE and Mozilla
AutoSuggestControl.prototype.selectRange = function (iStart, iLength) {
	//for internet explorer
    if (this.textbox.createTextRange) {
        var oRange = this.textbox.createTextRange(); 
        oRange.moveStart("character", iStart); 
        oRange.moveEnd("character", iLength - this.textbox.value.length); 
        oRange.select();
    //for the fire foxes
    } else if (this.textbox.setSelectionRange) {
        this.textbox.setSelectionRange(iStart, iLength);
    }
    //focus on the text
    this.textbox.focus(); 
};

//accepts a suggestion and selects it
AutoSuggestControl.prototype.typeAhead = function (sSuggestion) {
    if (this.textbox.createTextRange || this.textbox.setSelectionRange) {
    	//current length
        var iLen = this.textbox.value.length; 
        //add the suggestions into box
        this.textbox.value = sSuggestion;
        //select the new stuff
        this.selectRange(iLen, sSuggestion.length);
    }
};

//accepts suggestion array and passes the first one if there is one
AutoSuggestControl.prototype.autosuggest = function (aSuggestions, bTypeAhead) {
    if (aSuggestions.length > 0) {
        if (bTypeAhead) {
            this.typeAhead(aSuggestions[0]);
        }
        this.showSuggestions(aSuggestions);
    } else {
        this.hideSuggestions();
    }
};

//handler for keyup keypresses for characters
AutoSuggestControl.prototype.handleKeyUp = function (oEvent) {

    var iKeyCode = oEvent.keyCode;
	//if it is actually not a character
    if (iKeyCode == 8 || iKeyCode == 46) {
        this.provider.requestSuggestions(this, false);

    } else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
        //ignore it and don't do anything
    //if it is actually a key character
    } else {
        this.provider.requestSuggestions(this, true);
    }
};

//handler for keydown presses
AutoSuggestControl.prototype.handleKeyDown = function (oEvent) {
    switch(oEvent.keyCode) {
        case 38: //up arrow
            this.previousSuggestion();
            break;
        case 40: //down arrow 
            this.nextSuggestion();
            break;
        case 13: //enter
            this.hideSuggestions();
            break;
    }
};

//initialize the control unit
AutoSuggestControl.prototype.init = function () {
    var oThis = this;
    this.textbox.onkeyup = function (oEvent) {
        if (!oEvent) {
            oEvent = window.event;
        } 
        oThis.handleKeyUp(oEvent);
    };
    this.textbox.onkeydown = function (oEvent) {
        if (!oEvent) {
            oEvent = window.event;
        } 
        oThis.handleKeyDown(oEvent);
    };
    this.textbox.onblur = function () {
        oThis.hideSuggestions();
    };
    this.createDropDown();
};

//down arrow push -> select next suggestion
AutoSuggestControl.prototype.nextSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur < cSuggestionNodes.length-1) {
        var oNode = cSuggestionNodes[++this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue; 
    }
};

//up arrow push -> select previous suggestion
AutoSuggestControl.prototype.previousSuggestion = function () {
    var cSuggestionNodes = this.layer.childNodes;

    if (cSuggestionNodes.length > 0 && this.cur > 0) {
        var oNode = cSuggestionNodes[--this.cur];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue; 
    }
};

//initialize xml object
var xmlA;
if (window.ActiveXObject) {
    xmlA = new ActiveXObject("Microsoft.XMLHTTP");
}
else if (window.XMLHttpRequest) {
    xmlA = new XMLHttpRequest();
}

//create a suggestion provider
function SuggestionProvider() {
    //any initializations needed go here
    //don't need any
}

//build array of suggestions
SuggestionProvider.prototype.requestSuggestions = function(oAutoSuggestControl, bTypeAhead) {
    var text = oAutoSuggestControl.textbox.value;
    //set up query
    var query = "suggest?q=" + encodeURI(text);

    //pass query to xml
    xmlA.open("GET", query);
    xmlA.onreadystatechange = function () {
        //create result array
        var result = new Array();

        //if state 4
        if (xmlA.readyState == 4) {
            var servletresponse = xmlA.responseXML.getElementsByTagName('CompleteSuggestion');

            //iterate through responses
            for (var i = 0; i < servletresponse.length; i++) {
                //push each into the result array
                var temp = servletresponse[i].childNodes[0].getAttribute("data");
                result.push(temp);
            }
            //return result
            oAutoSuggestControl.autosuggest(result, bTypeAhead);
        }
    }
    xmlA.send();
};