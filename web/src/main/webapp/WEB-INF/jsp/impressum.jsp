<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement">
			<span style="float:left">Impressum / Kontakt</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" >Back to portal</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
				
		<div class="centerElement">
			<img src="images/img.png" />
		</div>

		<div class="centerElement">
			<h1>
			Hinweis zur Speicherung, Verarbeitung und Übermittlung personenbezogener Daten
			</h1>
			<div>
			Im Rahmen dieser Webseite werden Daten gespeichert, verarbeitet.
			</div>
			
			<h1> 
			Welche Daten werden übermittelt?
			</h1>
			<div>
			Diese Webseite benötigt einen Namen und eine gültige Email-Adresse zur Kontoerstellung. Im Rahmen des Anmeldevorgangs 
			wird ein Cookie auf deinem Computer gespeichert, der bis zum Beenden des Browser eine ID enthält, die dich eindeutig 
			identifiziert. Dieses Cookie wird Sitzung-Cookie genannt und enthält keine weiteren Informationen, als die Zuordnung 
			deines Anmeldevorgangs. Im Rahmen des Spiels erstellst und übermittelst du JavaScript-Code der die Geschäftslogik deines 
			Restaurants enthalt.
			</div>
			
			<h1> 
			Wer hat Einsicht in deine Dokumente und Daten?
			</h1>
			<div> 
			Der Name des Kontos wird öffentlich auf dieser Webseite gezeigt. Die Email-Adresse kann nur vom Administrator/Inhaber 
			(Oliver Zimpasser) dieser Seite eingesehen werden. 
			Der von dir erstellte JavaScript Programmcode kann immer von Administratoren und Inhabern dieser Seite gesehen werden, 
			falls du es möchtest hast du allerdings auch die Möglichkeit deinen Programmcode öffentlich zu machen.
			</div>
			 
			<h1>
			An wen werden deine Daten weitergegeben?
			</h1>
			<div>
			Deine Daten werden innerhalb dieser Webseite gespeichert. An andere Dritte erfolgt keine Weitergabe.
			</div> 
			
			<h1>
			Wie lange erfolgt die Speicherung?
			</h1>
			<div>
			Deine Daten werden auf diesem Server solange gespeichert, wie dein Konto hier am Spiel teilnimmt. 
			</div>
			 
			<h1>
			Einverständniserklärung zur Datenspeicherung und Datenverwendung:
			</h1>
			<div>
			<i>Während der Kontoeröffnung, erklärt der Benutzer:</i> Hiermit erkläre ich mich einverstanden, dass meine personenbezogenen 
			Daten, die ich im Zusammenhang mit der Kontoeröffnung für diese Webseite offenbart habe, elektronisch gespeichert und im 
			weiteren Spielverlauf verwendet werden. Andere Firmen und Personen dürfen meine Daten nicht erhalten. Außerdem stimme ich dem speichern des 
			Session Cookies während des Anmeldevorgangs zu. Der von mir erstellte JavaScript Programmcode darf von dieser Webseite 
			gespeichert, ausgeführt und eingesehen werden und sofern ich der Veröffentlichung zugestimmt habe, auch veröffentlich werden. 
			Mir ist bekannt, dass ich meine Einwilligung jederzeit durch entsprechende Erklärung per E-Mail an webmaster@codeyourrestaurant.com 
			oder auf dem Postweg (${actionBean.addressPageOwner}) für die Zukunft widerrufen kann. Um meine Rechte auf Auskunft, Berichtigung und Löschung 
			geltend zu machen, kann ich mich an dieselben Adressen wenden.							
			</div>
		</div>
		
		<div style="margin-bottom:40px;">				
		</div>
	
	</s:layout-component>
</s:layout-render>