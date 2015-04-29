<!DOCTYPE html> 
<html lang="en"> 
    <head>
        <title>$title$</title>

        <meta charset="UTF-8">

        <script src="./js/jquery/jquery-1.6.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="./js/discojuice/discojuice.misc.js"></script>
        <script type="text/javascript" src="./js/discojuice/discojuice.control.js"></script>
        <script type="text/javascript" src="./js/discojuice/discojuice.ui.js"></script>
        <!--<script type="text/javascript" src="./js/discojuice/discojuice.dict.en.js"></script>-->
        $discojuice_language_inject$
        <script type="text/javascript" src="./js/discojuice/idpdiscovery.js"></script>

        
        <link rel="stylesheet" type="text/css" href="./js/discojuice/css/discojuice.css" />
        <link rel="stylesheet" type="text/css" href="style.css" />

        <script type="text/javascript">
            $discojuice_inject$
        </script>
    </head>
    <body><!-- style="background: #ccc">-->

        <p style="text-align: right"><a class="signin" href="/"></a></p>

        <noscript>
            Your browser is not support javascript. To use the fallback discovery service <a href="$noscript$">click here</a>.
        </noscript>

        <!-- Piwik -->
	<script type="text/javascript">
  		var _paq = _paq || [];
  		_paq.push(['trackPageView']);
 		 _paq.push(['enableLinkTracking']);
  		(function() {
    			var u="//stats.clarin.eu/";
    			_paq.push(['setTrackerUrl', u+'piwik.php']);
    			_paq.push(['setSiteId', 7]);
    			var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
    			g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
  		})();
	</script>
<!--
	<noscript>
		<p><img src="//stats.clarin.eu/piwik.php?idsite=7" style="border:0;" alt="" /></p>
	</noscript>
-->
	<!-- End Piwik Code -->
    </body>
</html>