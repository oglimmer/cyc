<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<meta charset="utf-8" />
<title>CodeYourRestaurant</title>
<link rel="stylesheet" type="text/css" href="styles.css" />
</head>
<body>
	<div class="head">
		<h1>Welcome to "CodeYourRestaurant"</h1>
	</div>

	<div class="center">

		<div class="centerElement">
			<span style="float:left">Details</span> 
			<span style="float:right"><a href="javascript:void(0);" onclick="history.back()">Back</a></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>

		<div class="centerElement">
			
			Start: <fmt:formatDate type="both" dateStyle="short" timeStyle="medium" value="${actionBean.startTime}" />
			 (ran for <fmt:formatNumber value="${(actionBean.endTime.time-actionBean.startTime.time)/1000 }" type="number" maxFractionDigits="0"/> secs and used <fmt:formatNumber value="${actionBean.memUsed/1024 }" type="number"/> kBytes of RAM)
		</div>	

		<div class="centerElement">
			<table>
				<tr>
					<th>Participating companies for ${actionBean.result.totalDays} days</th>
				</tr>
				<c:forEach items="${actionBean.participants}" var="element">
					<tr> 
	  					<td>${element}</td>
	  					<td>
	  						<c:set var="tmpAsset">${actionBean.result.playerResults[element].totalAssets}</c:set>
							<c:if test = "${tmpAsset != -1}">
								<fmt:formatNumber value="${tmpAsset}" type="currency" pattern="¤#,##0.00;-¤#,##0.00"/>
							</c:if>
							<c:if test = "${tmpAsset == -1}">Bankrupt</c:if>	  					
	  					</td>
	  				</tr>
				</c:forEach>
			</table> 
		</div>	
		

		<div class="centerElement">
			<table>
				<tr>
				<c:forEach items="${actionBean.result.guestsTotalPerCity}" var="entry">
					<tr> 
	  					<td>Total guest for ${entry.key}</td>
	  					<td><fmt:formatNumber value="${entry.value}" type="number"/></td>
	  				</tr>
				</c:forEach>
			</table> 
		</div>	
		
		<div class="centerElement">
				
				<c:forEach items="${actionBean.result.playerResults}" var="entry">
					<c:set var="counter" value="0"/>
					<div>Company: ${entry.key}</div>
					<table>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total Assets (cash & real estates):</td>
							<td align="right" width="170">
								<c:if test = "${entry.value.totalAssets != -1}">
									<fmt:formatNumber value="${entry.value.totalAssets }" type="currency"/>
								</c:if>
								<c:if test = "${entry.value.totalAssets == -1}">Bankrupt</c:if>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Initial credit:</td>
							<td align="right"><fmt:formatNumber value="50000" type="currency"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Credit pay back (incl. interest)</td>
							<td align="right"><fmt:formatNumber value="-55000" type="currency" pattern="¤#,##0.00;-¤#,##0.00"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total on renting real estates:</td>
							<td align="right"><fmt:formatNumber value="${entry.value.totalOnRent }" type="currency"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total on buying real estates:</td>
							<td align="right"><fmt:formatNumber value="${entry.value.totalRealEstate}" type="currency"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Avg restaurants per day:</td>
							<td align="right"><fmt:formatNumber value="${entry.value.establishmentsByDays / actionBean.result.totalDays}" type="number"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<c:forEach items="${entry.value.staffByDays}" var="entryStaff">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Avg number of ${entryStaff.key}'s per day:</td>
								<td align="right"><fmt:formatNumber value="${entryStaff.value/actionBean.result.totalDays}" type="number"/></td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.totalOnSalaries}" var="entryStaff">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total salary for ${entryStaff.key}</td>
								<td align="right"><fmt:formatNumber value="${entryStaff.value}" type="currency"/></td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>

						<c:forEach items="${entry.value.totalPurchasedFoodUnits}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total purchased food units of ${entryUnits.key}</td>
								<td align="right"><fmt:formatNumber value="${entryUnits.value}" type="number"/></td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.totalPurchasedFoodCosts}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total purchased food costs for ${entryUnits.key}</td>
								<td align="right"><fmt:formatNumber value="${entryUnits.value}" type="currency"/></td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.totalRottenFood}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total rotten food units of ${entryUnits.key}</td>
								<td align="right"><fmt:formatNumber value="${entryUnits.value}" type="number"/></td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.servedFoodPerTypeUnits}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total served meals of ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
									(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodUnitsTotal}" type="percent"/>)
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.servedFoodPerTypeRevenue}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total revenue for ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="currency"/>
									(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodRevenueTotal}" type="percent"/>)
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.servedFoodPerEstablishmentUnits}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total served meals in location ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
									(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodEstablishmentUnitsTotal}" type="percent"/>)
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.servedFoodPerEstablishmentRevenue}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total revenue in location ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="currency"/>
									(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodEstablishmentRevenueTotal}" type="percent"/>)									
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>

						<c:forEach items="${entry.value.guestsYouPerCity}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total visitors in ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
									(<fmt:formatNumber value="${entryUnits.value/actionBean.result.guestsTotalPerCity[entryUnits.key] }" type="percent"/>)									
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.guestsLeftPerCity}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total visitors who left without ordering anything in ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
									(<fmt:formatNumber value="${entryUnits.value/entry.value.guestsYouPerCity[entryUnits.key] }" type="percent"/>)									
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.guestsOutOfIngPerCity}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total visitors who got nothing since you ran out of ingredients in ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
									(<fmt:formatNumber value="${entryUnits.value/entry.value.guestsYouPerCity[entryUnits.key] }" type="percent"/>)
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.missingIngredients}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Missing ${entryUnits.key} for x times </td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						
					</table>
					<hr/>		
				</c:forEach>	
				
		</div>	
				
	</div>
	
	<div class="footer">
		Fork me on github.com/oglimmer/cyc
	</div>
</body>
</html>