/*
 * DiscoJuice
 * Author: Andreas Åkre Solberg, UNINETT, andreas.solberg@uninett.no
 * Licence undecided.
 */
if (typeof DiscoJuice == "undefined") var DiscoJuice = {};

//var first = false;
DiscoJuice.UI = {
	// Reference to the top level DiscoJuice object
	"parent" : DiscoJuice,
	
	// The current data model
	"control": null,
	
	// Reference to the 
	"popup": null,
	
	"alreadyLoaded": {},
	
        "mode": 1, //1: use no default idp, 2: use default idp
        
	// Entities / items
	"resulthtml": 'Loading data…',
        "resulthtmlQuick" : '',
        "preselectResulthtml": 'Loading data…',
        
	"show": function() {
            var that = this;
            this.control.load();
            $("div#discojuice_overlay").show();
            this.popup.fadeIn("slow", function () {that.focusSearch();});
	},
	
	"focusSearch": function() {
            var element = $('input.discojuice_search');
            element.focus();
	},
	
	"hide": function() {x
		$("div#discojuice_overlay").fadeOut("slow");
		this.popup.fadeOut("slow");
	},
	
	"clearItems": function() {
		this.resulthtml = '';
                this.resulthtmlQuick = '';
                this.preselectResulthtml = '';
		this.alreadyLoaded = {};
	},
	
	"sprintf": function() {
	   if (!arguments || arguments.length < 1 || !RegExp)
	   {
	      return;
	   }
	   var str = arguments[0];
	   var re = /([^%]*)%('.|0|\x20)?(-)?(\d+)?(\.\d+)?(%|b|c|d|u|f|o|s|x|X)(.*)/;
	   var a = b = [], numSubstitutions = 0, numMatches = 0;
	   while (a = re.exec(str))
	   {
	      var leftpart = a[1], pPad = a[2], pJustify = a[3], pMinLength = a[4];
	      var pPrecision = a[5], pType = a[6], rightPart = a[7];

	      numMatches++;
	      if (pType == '%')
	      {
	         subst = '%';
	      }
	      else
	      {
	         numSubstitutions++;
	         if (numSubstitutions >= arguments.length)
	         {
	            alert('Error! Not enough function arguments (' + (arguments.length - 1)
	               + ', excluding the string)\n'
	               + 'for the number of substitution parameters in string ('
	               + numSubstitutions + ' so far).');
	         }
	         var param = arguments[numSubstitutions];
	         var pad = '';
	                if (pPad && pPad.substr(0,1) == "'") pad = leftpart.substr(1,1);
	           else if (pPad) pad = pPad;
	         var justifyRight = true;
	                if (pJustify && pJustify === "-") justifyRight = false;
	         var minLength = -1;
	                if (pMinLength) minLength = parseInt(pMinLength);
	         var precision = -1;
	                if (pPrecision && pType == 'f')
	                   precision = parseInt(pPrecision.substring(1));
	         var subst = param;
	         switch (pType)
	         {
	         case 'b':
	            subst = parseInt(param).toString(2);
	            break;
	         case 'c':
	            subst = String.fromCharCode(parseInt(param));
	            break;
	         case 'd':
	            subst = parseInt(param) ? parseInt(param) : 0;
	            break;
	         case 'u':
	            subst = Math.abs(param);
	            break;
	         case 'f':
	            subst = (precision > -1) ? Math.round(parseFloat(param) * Math.pow(10, precision)) / Math.pow(10, precision) : parseFloat(param);
	            break;
	         case 'o':
	            subst = parseInt(param).toString(8);
	            break;
	         case 's':
	            subst = param;
	            break;
	         case 'x':
	            subst = ('' + parseInt(param).toString(16)).toLowerCase();
	            break;
	         case 'X':
	            subst = ('' + parseInt(param).toString(16)).toUpperCase();
	            break;
	         }
	         var padLeft = minLength - subst.toString().length;
	         if (padLeft > 0)
	         {
	            var arrTmp = new Array(padLeft+1);
	            var padding = arrTmp.join(pad?pad:" ");
	         }
	         else
	         {
	            var padding = "";
	         }
	      }
	      str = leftpart + padding + subst + rightPart;
	   }
	   return str;
	},
        
        "generateTextLink": function(item, countrydef, search, distance, quickentry, enabled) {
                var textLink = '';
		var classes = (enabled ? 'enabled' : 'disabled');
		var iconpath = this.parent.Utils.options.get('discoPath', '') + 'logos/';
		var flagpath = this.parent.Utils.options.get('discoPath', '') + 'flags/';
		var clear = false;
		var debugweight = this.parent.Utils.options.get('debug.weight', false);
		var relID = item.entityID;
                
		if (item.subID) {
			relID += '#' + item.subID;
		}
		
		if (this.alreadyLoaded[relID]) return;
		this.alreadyLoaded[relID] = true;
		
                //var maxWidth = 200;
                //var maxHeight = 50;
                
		// Add icon element first
		if (item.icon && this.parent.Utils.options.get('showIcon', true)) {
                    //TODO: replace with regex?
                    if(item.icon.url.startsWith("data:")) {
                        textLink += '<img class="logo" src="' + item.icon.url + '" />';
                    } else if(item.icon.url.startsWith("http://") || item.icon.url.startsWith("https://")) {
                        /*
                        var w = item.icon.width;
                        var h = item.icon.height;
                        
                        var rateX = maxWidth / w;
                        var rateY = maxHeight / h;
                        
                        if(rateX > rateY) {
                            w = w*rateY;
                            h = h*rateY;
                        } else {
                            w = w*rateX;
                            h = h*rateX;
                        }
                        */
                        textLink += '<img class="logo" src="' + item.icon.url + '" />';//width="'+w+'" height="'+h+'"/>';
                    } else {
			textLink += '<img class="logo" src="' + iconpath + item.icon.url + '" />';
                    }
                    clear = true;
		}
		
		if (quickentry) {
                    textLink += '<span style="font-size: 80%; -webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: 3px; border: 1px solid #ccc; background: #eee; color: #777; padding: 3px 2px 0px 2px; margin: 3px; float: left; left: -10px">&#8629;</span>';
		}
		
		// Add title
		textLink += '<span class="title">' + item.title + '</span>';
		
		// Add matched search term
		if (search && search !== true) {
			textLink += '<span class="substring">&#8212; ' + search + '</span>';
		} else if (item.descr) {
			textLink += '<span class="substring">&#8212; ' +  item.descr + '</span>';
		}	

		if ((countrydef || (distance != undefined)) && this.parent.Utils.options.get('showLocationInfo', true)) {				
                    textLink += '<span class="location">';
                    if (countrydef) {
                        textLink += '<span class="country">';
                        if (countrydef.flag) {
                            textLink += '<img src="' + flagpath + countrydef.flag + '" alt="' + escape(countrydef.country) + '" /> ';
                        }
                        textLink += countrydef.country + '</span>';
                    }

                    if (distance != undefined) {
                        if (distance < 1) {
                            textLink += '<span class="distance">' + DiscoJuice.Dict.nearby + '</span>';
                        } else {
                            textLink += '<span class="distance">' +  Math.round(distance) + ' km' + '</span>';
                        }
                    }
                    textLink += '</span>';
		}
		
		if (debugweight) {
                    textLink += '<div class="debug">';

                    if (item.subID) {
                        textLink += '<input value="' + item.subID + '" />';
                    }

                    var w = 0;
                    if (item.weight) {
                        w += item.weight;
                    }
                    if (item.distanceweight) {
                        w += item.distanceweight;
                    }
                    textLink += 'Weight <strong style="color: #888">' + Math.round(100*w)/100 + '</strong> ';

                    if (item.weight) {
                        textLink += ' (base ' + item.weight + ')   ';
                    }
                    if (item.distanceweight) {
                        textLink += '(dist ' + Math.round(100*item.distanceweight)/100 + ')';
                    }

                    textLink += '</div>';
		}
	
		// Add a clear bar. 
		if (clear) {
			textLink += '<hr style="clear: both; height: 0px; visibility:hidden" />';
		}
		
		// Wrap in A element
		textLink = '<a href="" class="' + classes + '" rel="' + escape(relID) + '" title="' + DiscoJuice.Utils.escapeHTML(item.title) + '">' + 
			textLink + '</a>';

		return textLink;
        },
        
	"addPreselectedItem": function(item, countrydef, search, distance, quickentry, enabled) {
            this.preselectResulthtml += this.generateTextLink(item,countrydef,search,distance,quickentry,enabled);
            this.parent.Utils.debug('added preselected idp item');
        },
        
	"addItem": function(item, countrydef, search, distance, quickentry, enabled) {
            this.parent.Utils.debug("added");
            if(quickentry) {
                this.resulthtmlQuick += this.generateTextLink(item,countrydef,search,distance,quickentry,enabled); 
            } else {
                this.resulthtml += this.generateTextLink(item,countrydef,search,distance,quickentry,enabled); 
            }
	},
	
	"setScreen": function (content) {
		$("div.discojuice_listContent").hide();
		$("div#locatemediv").hide();
		$("div#search").hide();

		$("div.filters").hide();
		
                $("div#discojuice_page div.discojuice_content").html(content);		
		
		$("div#discojuice_page").show();
		$("div#discojuice_page_return").show();
		
	},
	
	"returnToProviderList": function () {
		$("div.discojuice_listContent").show();
		$("div#discojuice_page").hide();
		$("div#discojuice_page_return").hide();
		
		if (this.parent.Utils.options.get('location', false) && navigator.geolocation) {
			$("div#locatemediv").show();
		}
		$("div#search").show();
		$("div.filters").show();
	},
		
	"refreshData": function(showmore, show, listcount) {
		var that = this;
		
		this.parent.Utils.info('DiscoJuice.UI refreshData()');
		
                this.popup.find("div#preselectedScroller").empty().append(this.preselectResulthtml);
                this.popup.find("div#preselectedScroller a").each(function() {
			var overthere = that;	// Overthere is a reference to the UI object
			$(this).click(function(event) {
				event.preventDefault();
				event.stopPropagation();
				//overthere.hide();
							
				// The "rel" attribute is containing: 'entityid#subid'
				// THe following code, decodes that.
				var relID = unescape($(this).attr('rel'));
				var entityID = relID;
				var subID = undefined;
				if (relID.match(/^.*#.+?$/)) {
					var matched = /^(.*)#(.+?)$/.exec(relID);
					entityID = matched[1];
					subID = matched[2];
				}
				overthere.control.selectProvider(entityID, subID);
			});
		});
                
                this.popup.find("div#quickscroller").empty().append(this.resulthtmlQuick);
                this.popup.find("div#quickscroller a").each(function() {
			var overthere = that;	// Overthere is a reference to the UI object
			$(this).click(function(event) {
				event.preventDefault();
				event.stopPropagation();
				//overthere.hide();
							
				// The "rel" attribute is containing: 'entityid#subid'
				// THe following code, decodes that.
				var relID = unescape($(this).attr('rel'));
				var entityID = relID;
				var subID = undefined;
				if (relID.match(/^.*#.+?$/)) {
					var matched = /^(.*)#(.+?)$/.exec(relID);
					entityID = matched[1];
					subID = matched[2];
				}
				overthere.control.selectProvider(entityID, subID);
			});
		});
                
		this.popup.find("div#scroller").empty().append(this.resulthtml);
		this.popup.find("div#scroller a").each(function() {
			var overthere = that;	// Overthere is a reference to the UI object
			$(this).click(function(event) {
				event.preventDefault();
				event.stopPropagation();
				//overthere.hide();
							
				// The "rel" attribute is containing: 'entityid#subid'
				// THe following code, decodes that.
				var relID = unescape($(this).attr('rel'));
				var entityID = relID;
				var subID = undefined;
				if (relID.match(/^.*#.+?$/)) {
					var matched = /^(.*)#(.+?)$/.exec(relID);
					entityID = matched[1];
					subID = matched[2];
				}
				overthere.control.selectProvider(entityID, subID);
			});
		});
		
		if (showmore) {
                    var moreLink = '<a class="discojuice_showmore textlink" href="">' + this.sprintf(DiscoJuice.Dict.moreLink, show, this.parent.Control.data.length)+ '…</a>';
                    if(show === 0) {
                        moreLink = '<a class="discojuice_showmore textlink" href="">' + DiscoJuice.Dict.moreLinkPreselected + '</a>';
                    }
                    this.popup.find("p.discojuice_moreLinkContainer").empty().append(moreLink);
                    this.popup.find("p.discojuice_moreLinkContainer a.discojuice_showmore").click(function(event) {
                        event.preventDefault();
                        that.control.increase();
                        that.showProviderList();
                    });
		} else {
                    this.popup.find("p.discojuice_moreLinkContainer").empty();
                    if (listcount > 10) {
                        var moreLink = '<span style="color: #888">' + listcount + ' entries listed</span>';
                        this.popup.find("p.discojuice_moreLinkContainer").append(moreLink);
                    } 
		} 
                
                //if(!this.control.cookieEntityId && show === 0 && this.mode === 1) {
                if(show === 25 && this.mode === 1) {
                    //this.control.increase();
                    this.showProviderList();
                }
                /*
                if(first) {
                    $('#search_box').focus();
                    this.parent.Utils.log('visible='+$('#search_box').is(':visible'));
                    first = false;
                }
                */
	},
        
        "showProviderList": function() {
            $("#search_field").show();
            $("#locatemediv").show();
            $("#filters").show();
        },
	
	"error": function(message) {
		this.parent.Utils.error("error" + message);
		this.popup.find("div#discojuice_error").show();
		this.popup.find("div.discojuice_errortext").append('<p style="border-bottom: 1px dotted #ddd; margin-bottom: 3px" class="discojuice_errortext">' + message + '</p>');
	},

        "getParameterByName": function (name) {
            name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
            var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
            return results === null ? null : decodeURIComponent(results[1].replace(/\+/g, " "));
        },

        "enable": function(control) {
            
            console.log('Enable UI:');
            console.log('version: '+this.parent.Utils.options.get('version', '?'));
            console.log('server: '+this.parent.Utils.options.get('server', 'n/a'));
            
            var returnParam = this.getParameterByName("return") != null;
            
		var imgpath = this.parent.Utils.options.get('discoPath', '') + 'images/';
		
		var textSearch = this.parent.Utils.options.get('textSearch',  DiscoJuice.Dict.orSearch);
                var textHelpPreselected = this.parent.Utils.options.get('textHelpPreselected', DiscoJuice.Dict.textHelpPreselected);
		var textHelp = this.parent.Utils.options.get('textHelpPreselected', DiscoJuice.Dict.textHelpPreselected);

                //Make main title html 
                var titleText = '<strong>'+this.parent.Utils.options.get('title', null)+'</strong>';
                var mainTitleHTML = '<p class="discojuice_maintitle">'+this.sprintf(DiscoJuice.Dict.connectTo, titleText)+'</p>';
                //Make subtitle html
                //var subtitleText = this.parent.Utils.options.get('subtitle', null);
                var subtitleText = this.parent.Utils.options.get('subtitle', DiscoJuice.Dict.subtitle);
		var subtitleHTML = (subtitleText !== null ? '<p class="discojuice_subtitle">' + subtitleText + '</p>' : '');
	
                var accountHelpText = this.parent.Utils.options.get('helpMore', DiscoJuice.Dict.helpMore);
                var accountHelpHtml = (accountHelpText !== null ? '<p class="discojuice_subtitle">' + accountHelpText + '</p>' : '');
                
                if(this.mode === 1) {
                    DiscoJuice.Dict.moreLinkPreselected = DiscoJuice.Dict.allLinkPreselected;
                }
                
		var html = 	
                    '<div style="display: none" class="discojuice" id="discojuice" tabindex="0">' +
                    '<div class="top">' +
                            '<a href="#" class="discojuice_close">&nbsp;</a>' +
                            mainTitleHTML +
                            subtitleHTML +
                    '</div>' + 

                    '<div class="help">'+accountHelpHtml+'</div>';
            
                    if(!returnParam) {
                        html += '<div class="help error">' + DiscoJuice.Dict.noReturnParamError + '</div>';
                    }
                    
                    //preselected idp area. Enabled in mode 2.
                    if(this.mode === 2) {                        
                        html += '<div class="" >' +
                            '<p class="discojuice_whattext">' + textHelpPreselected + '</p>' +
                        '</div>' +

                        '<div class="discojuice_listContent" style="">' +
                            '<div class="scroller" id="preselectedScroller">' +
                                '<div class="loadingData" ><img src="' + imgpath + 'spinning.gif" /> ' + DiscoJuice.Dict.loading + '...</div>' +
                            '</div>' +
                        '</div>' +

                        '<div class="" style="border-top: solid 1px #bbb; height: 3px; padding: 0px; margin: 0px;" ></div>' +

                        '<div class="" >' +
                           '<p class="discojuice_whattext">' + textHelp + '</p>' +
                        '</div>';
                    }  
                    
                    //scrollable external idp list                        
                    html +=                        
                        '<div class="discojuice_listContent" style="">' +
				'<div class="scroller" id="quickscroller">' +
					'<div class="loadingData" ><img src="' + imgpath + 'spinning.gif" /> ' + DiscoJuice.Dict.loading + '...</div>' +
				'</div>' +
			'</div>' +
                        
			'<div class="discojuice_listContent" style="">' +
				'<div class="scroller" id="scroller">' +
					'<div class="loadingData" ><img src="' + imgpath + 'spinning.gif" /> ' + DiscoJuice.Dict.loading + '...</div>' +
				'</div>' +
                                '<p class="discojuice_moreLinkContainer" style="margin: 0px; padding: 4px; text-align: center;">&nbsp;</p>' +
			'</div>' +
			
			'<div id="discojuice_page" style="display: none"  class="" >' +
				'<div class="discojuice_content" style="">' + 
				'</div>' +
			'</div>' +
			
			'<div id="discojuice_page_return" style="display: none"  class="" >' +
				'<div class="" style="">' + 
				'<input id="discojuice_returntoproviderlist" type="submit" value="« ' + DiscoJuice.Dict.pageReturn + '" />' +
				'</div>' +
			'</div>' +
	
			'<div id="search" class="">' +
				'<p id="search_field" style="display: none"><input type="search" id="search_box" class="discojuice_search" results=5 autosave="discojuice" name="searchfield" placeholder="' + textSearch + '" value="" /></p>' +
			'</div>' +
			
			'<div id="discojuice_error" style="display: none"  class="" >' +
				'<img src="' + imgpath + 'error.png" style="float: left" />' +
				'<div class="discojuice_errortext" style="clear: none; margin-top: 0px; margin-left: 30px; font-size: 11px;">' + 
				'</div>' +
			'</div>' +
			
			'<div id="locatemediv" style="display: none;">' +
				'<div class="locatemebefore">' +
					'<p style="margin-top: 10px"><a id="locateme" href="">' +
						'<img style="float: left; margin-right: 5px; margin-top: -10px" src="' + imgpath + 'target.png" alt="locate me..." />' +
						DiscoJuice.Dict.locateMe + '</a>' +
					'</p>' +
					'<p style="color: #999" id="locatemeinfo"></p>' +
				'</div>' +
				'<div style="clear: both" class="locatemeafter"></div>' +
			'</div>' +
			
			'<div style="display: none">' + 
				'<button id="discojuiceextesion_listener" />' +
			'</div>' +
			
			'<div class="bottom">' +
				'<div class="filters" style="padding: 0px; margin: 0px; display: none;" id="filters"></div>' +
                                
                                '<table style="width: 100%">' +
                                    '<tr>' +
                                        '<td>' +
                                            '<p id="dj_help" style="margin 0px; text-align: right; color: #ccc; font-size: 75%">' + 
                                                'Based on DiscoJuice &copy; UNINETT<br />' + 
                                                'Version: ' + this.parent.Utils.options.get('version', '?') +
                                                ', Server ID: ' + this.parent.Utils.options.get('server', 'n/a') +
                                            '</p>' +
                                        '</td><td>' +
                                            '<p id="dj_help" style="margin 0px; text-align: right; color: #ccc; font-size: 75%">' + 
                                                '<img class="" style="position: relative; bottom: -4px; right: -5px" alt="Information" src="' + imgpath + 'info.png" />' +
                                            '</p>' +
                                        '</td>' +
                                    '</tr>' +
                                '</table>' +
                                
			'</div>' +
	

		'</div>';
		var that = this;
		
		var htarget = $("body");
		if (this.parent.Utils.options.get('useTarget', false)) {
			htarget = this.parent.Utils.options.get('target', htarget);				
		}

		if (this.parent.Utils.options.get('overlay', true)) {
                    this.parent.Utils.debug('DiscoJuice Enable: adding overlay');
                    var overlay = '<div id="discojuice_overlay" style="display: none"></div>';
                    $(overlay).appendTo($("body"));
                    //htarget = $("#discojuice_overlay");
                //this.popup = $(html).appendTo(htarget);
		}                     
                
                this.popup = $(html).appendTo(htarget);
		
                
                
                $("#discojuice").keypress(function(event) {
                    var charCode;
                    
                    if (event && event.which){
		        charCode = event.which;
		    } else if(window.event){
		        event = window.event;
		        charCode = event.keyCode;
		    }

		    if(charCode == 13) {
                        that.control.hitEnter();
                        return;
		    }
                    
                });

		if (this.parent.Utils.options.get('always', false) === true) {
                    this.popup.find(".discojuice_close").hide();
                    this.show();
		} else {
                    // Add a listener to the sign in button.
                    $(control).click(function(event) {
                        event.preventDefault();
                        that.show();
                        return false;
                    });
		}
		
		this.popup.find("p#dj_help").click(function() {
                    that.setScreen(
                        '<h2>' + DiscoJuice.Dict.about + '</h2>' +
                        '<p style="margin: .5em 0px">' + that.sprintf(DiscoJuice.Dict.aboutDescr, '<a href="http://uninett.no">', '</a>') + '</p>' +
                        '<p style="margin: .5em 10px"><a href="http://discojuice.org" target="_blank">' + DiscoJuice.Dict.aboutMore + '</a></p>' +
                        '<p style="margin: .5em 0px; font-size: 80%">' + DiscoJuice.Dict.version + ': ' + DiscoJuice.Version);
		});

		this.popup.find("#discojuiceextesion_listener").click(function() {
			that.control.discojuiceextension();
		});

		this.popup.find("#discojuice_page_return input").click(function(e) {
			e.preventDefault();
			that.returnToProviderList();
		});

		// Add listeners to the close button.
		this.popup.find(".discojuice_close").click(function() {
			that.hide();
		});

 		// Add toogle for what is this text.
		this.popup.find(".discojuice_what").click(function() {
			that.popup.find(".discojuice_whatisthis").toggleClass("show");
		});


		if (this.parent.Utils.options.get('location', false) && navigator.geolocation) {
			var that = this;
			$("a#locateme").click(function(event) {
				var imgpath = that.parent.Utils.options.get('discoPath', '') + 'images/';

				that.parent.Utils.debug('Locate me. Detected click event.');
				event.preventDefault();
 				event.stopPropagation();
				$("div.locatemebefore").hide();
				$("div.locatemeafter").html('<div class="loadingData" ><img src="' + imgpath + 'spinning.gif" /> ' + DiscoJuice.Dict.locating + '...</div>');
				that.control.locateMe();
			});
		} 
		
	},
        
	"setLocationText": function(html) {
		return $("div.locatemeafter").html(html);
	},
	
	"addContent": function(html) {
		return $(html).appendTo($("body"));
	},
	"addFilter": function(html) {
		return $(html).prependTo(this.popup.find('.filters'));
	}
};
    