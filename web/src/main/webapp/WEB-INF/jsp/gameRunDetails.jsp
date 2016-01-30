<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
  
		<div class="centerElement">
			<span style="float:left">Details</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.action.RunHistoryActionBean">Back</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>

		<div class="centerElement">
			Start: <fmt:formatDate type="both" dateStyle="short" timeStyle="medium" value="${actionBean.startTime}" /> UTC
			 (ran for <fmt:formatNumber value="${(actionBean.endTime.time-actionBean.startTime.time)/1000 }" type="number" maxFractionDigits="0"/> secs)
		</div>	

		<div class="centerElement">
			<table>
				<tr>
					<th>Participating companies for ${actionBean.result.totalDays} days</th>
				</tr>
				<c:forEach items="${actionBean.participants}" var="element">
					<c:set var="elementLowerCase" value="${fn:toLowerCase(element)}" />
					<tr> 
	  					<td>${element}</td>
	  					<td>
	  						<c:set value="${actionBean.result.playerResults[element].totalAssets}" var="tmpAsset"/>
							<c:if test = "${tmpAsset != -1}">
								<fmt:formatNumber value="${tmpAsset}" type="currency" pattern="¤#,##0.00;-¤#,##0.00"/>
							</c:if>
							<c:if test = "${tmpAsset == -1}">
								Bankrupt on day ${actionBean.result.playerResults[element].bankruptOnDay}
								<c:if test="${actionBean.result.errors.contains(element) }">(JS error)</c:if>
							</c:if>
							<c:if test="${actionBean.showCode[elementLowerCase] }">
								<s:link beanclass="de.oglimmer.cyc.web.action.ShowCodeActionBean">
									<s:param name="username">${element }</s:param>
									<s:param name="gameRunId">${param.gameRunId }</s:param>
									[Code]
								</s:link>
							</c:if>	  					
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
	  					<td><fmt:formatNumber value="${entry.value}" type="number"/> (<fmt:formatNumber value="${entry.value/actionBean.result.totalDays}" maxFractionDigits="0" type="number"/> p.day)</td>
	  				</tr>
				</c:forEach>
			</table> 
		</div>	
		
		<div class="centerElement">
			<table>
				<tr>
					<th>Popularity of meals</th>
				</tr>
				<tr>
				<c:forEach items="${actionBean.result.foodChart}" var="entry">
					<tr> 
	  					<td>${entry.key}</td>
	  					<td><fmt:formatNumber value="${entry.value}" type="percent" maxFractionDigits="0"/></td>
	  				</tr>
				</c:forEach>
			</table> 
		</div>	
		
		<div class="centerElement">
			<table>
				<tr>
					<th>Establishment's appeal</th>
				</tr>
				<tr>
				<c:forEach items="${actionBean.result.establishmentChart}" var="entry">
					<tr> 
	  					<td>${entry.key}</td> 
	  					<td><fmt:formatNumber value="${entry.value}" type="percent" maxFractionDigits="0"/></td>
	  				</tr>
				</c:forEach>
			</table> 
		</div>
		
		<script>
			function toggleData(toogleId) {
				$("#"+toogleId).toggle();
			}
		</script>
		
		<div class="centerElement" style="font-size:0.8em">
				
				<h3>Detailed company statistics (click a name to expand)</h3>
				
				<c:forEach items="${actionBean.result.playerResults}" var="entry">
					<c:set var="expenses" value="${5000+entry.value.totalOnRent+entry.value.salariesTotal+entry.value.purchasedFoodCostsTotal+entry.value.totalBribe+entry.value.totalInterior }"/>
					<c:set var="daysInPlay" value="${actionBean.result.totalDays<entry.value.bankruptOnDay||entry.value.bankruptOnDay==0?actionBean.result.totalDays:entry.value.bankruptOnDay}"/>
					<fmt:formatNumber var="monthInPlay" value="${daysInPlay/30}" maxFractionDigits="0" />
					<c:set var="counter" value="0"/>
					<div onclick="toggleData('dataCompany${entry.value.nameSimplified}');">Company: ${entry.key}</div>
					<table style="display:none" id="dataCompany${entry.value.nameSimplified}">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total Assets (cash &amp; real estates):</td>
							<td align="right" width="170">
								<c:if test = "${entry.value.totalAssets != -1}">
									<fmt:formatNumber value="${entry.value.totalAssets }" type="currency"/>
								</c:if>
								<c:if test = "${entry.value.totalAssets == -1}">Bankrupt</c:if>
							</td>							
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total Earnings:</td>
							<td align="right" width="170">
								<fmt:formatNumber value="${entry.value.servedFoodRevenueTotal}" type="currency"/>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total Expenses:</td>
							<td align="right" width="170">
								<fmt:formatNumber value="${expenses}" type="currency"/>
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
							<td align="right">
								<fmt:formatNumber value="${entry.value.totalOnRent}" type="currency"/>
								/ <fmt:formatNumber value="${entry.value.totalOnRent / monthInPlay }" type="currency"/>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total on buying real estates:</td>
							<td align="right"><fmt:formatNumber value="${entry.value.totalRealEstate}" type="currency"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total on real estate broker bribes:</td>
							<td align="right"><fmt:formatNumber value="${entry.value.totalBribe}" type="currency"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total on interior accessories:</td>
							<td align="right"><fmt:formatNumber value="${entry.value.totalInterior}" type="currency"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Avg restaurants per day:</td>
							<td align="right"><fmt:formatNumber value="${entry.value.establishmentsByDays / daysInPlay }" type="number"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						<c:forEach items="${entry.value.staffByDays}" var="entryStaff">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Avg number of ${entryStaff.key}'s per day:</td>								
								<td align="right"><fmt:formatNumber value="${entryStaff.value/ daysInPlay }" type="number"/></td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.totalOnSalaries}" var="entryStaff">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total salary for ${entryStaff.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryStaff.value}" type="currency"/>
									/ <fmt:formatNumber value="${entryStaff.value /monthInPlay }" type="currency"/>
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>

						<c:forEach items="${entry.value.totalPurchasedFoodUnits}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total purchased food units of ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
									/ <fmt:formatNumber value="${entryUnits.value / daysInPlay}" type="number" maxFractionDigits="1"/>
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.totalPurchasedFoodCosts}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total purchased food costs for ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="currency"/>
									/ <fmt:formatNumber value="${entryUnits.value / entry.value.totalPurchasedFoodUnits[entryUnits.key]}" type="currency"/>
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<c:forEach items="${entry.value.totalRottenFood}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>Total rotten food units of ${entryUnits.key}</td>
								<td align="right">
									<fmt:formatNumber value="${entryUnits.value}" type="number"/>
									/ <fmt:formatNumber value="${entryUnits.value / daysInPlay}" type="number" maxFractionDigits="1" />
								</td>
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
						<c:forEach items="${entry.value.runtimes}" var="entryUnits">
							<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
								<td>
									Method ${entryUnits.key} took									
								</td>
								<td align="right">
									<c:if test="${entryUnits.key != 'init' and entryUnits.key != 'launch' }">ø</c:if>
									<fmt:formatNumber value="${entryUnits.value.average()}" type="number"/> µs
								</td>
							</tr>
							<c:set var="counter" value="${counter + 1}"/>
						</c:forEach>
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Code length (without white spaces)</td>
							<td align="right">
								<fmt:formatNumber value="${entry.value.codeLength}" type="number"/>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
						
						
					</table>
					<hr/>		
				</c:forEach>	
				
		</div>	
		
		<div class="centerElement">
		
			Top 15 players cash flow over time:<br/>
			<span style="font-size:10px;">(as lowest figure per day)</span>
			
			<c:set var="userTop15" value="${actionBean.result.playerResultsTop15}" />
			
			<script src="js/RGraph.common.core.js" ></script>
	    	<script src="js/RGraph.line.js" ></script>
	    	<canvas id="cvs" width="700" height="600">[No canvas support]</canvas>
			<div id="labels" style="font-family:Arial;font-size:12px;">Legend:<br/> </div>
			<script>
				var jsonData = [<c:forEach items="${userTop15}" var="play">${play.value.statistics.cashHtml},</c:forEach>];
				var labelData = [<c:forEach items="${userTop15}" var="play">'${fn:replace(play.key, "'", "\\'")}',</c:forEach>];
				var labelColors = ['blue', 'white', 'yellow', 'fuchsia', 'gray', 'green', 'lime', 'maroon', 'navy', 'olive', 'black', 'orange', 'purple', 'silver', 'aqua'];
			</script>


			<c:if test="${not empty actionBean.username }">
				<%-- HACK/TODO: you must not use playerResults[actionBean.username] as it creates the current user in the result list, but this user might haven't participated --%>
	    		<c:set var="currentUserParticipated" value="${actionBean.result.playerResultsCopy[actionBean.username]}" />
	    		<c:set var="currentUserInTop15" value="${userTop15[actionBean.username]}" />
	    		<c:if test="${empty currentUserInTop15 && not empty currentUserParticipated}">
			    	<script>
			    		jsonData.push(${currentUserParticipated.statistics.cashHtml});
			    		labelData.push("${actionBean.username}");
			    		labelColors.push('red');
			    	</script>
		    	</c:if>
	    	</c:if>
			
	    	<script>
	    		var maxMoney = ${actionBean.result.upperCashBoundary};
		    	var line = new RGraph.Line('cvs', jsonData)
		    	.Set('ymax', maxMoney)
		    	.Set('ylabels.count', maxMoney/50000)
		        .Set('numxticks', 12)
		        .Set('numyticks', maxMoney/50000)
		        .Set('hmargin', 0)
		        .Set('background.grid.autofit.numvlines', 12)
		        .Set('background.grid.autofit.numhlines', maxMoney/50000)
		        .Set('background.grid.dotted', true)
		        .Set('colors', labelColors)
		        .Set('linewidth', 1)
		        .Set('gutter.left', 70)
		        .Set('gutter.right', 15)
		        .Set('labels',['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec','Jan'])
		        .Draw();
		    	
		    	for(var i = 0 ; i < labelData.length ; i++) {
		    		$("#labels").append("<span style='color:"+labelColors[i]+"'>"+labelData[i]+"</span> - ");
		    	}
		    	
	    	</script>	    	
	    </div>

		<c:if test="${not empty currentUserParticipated }">

			<div id="customGraph" class="centerElement">
		    </div>
			<script>
				var jsonDataCustom = [${currentUserParticipated.statistics.getCustomHtml()}];
				var jsonDataCustomNames = [<c:forEach var="i" begin="0" end="4">"${currentUserParticipated.statistics.getCustomStatisticsName(i)}",</c:forEach>];				
				
				for(var i = 0 ; i < jsonDataCustom.length ; i++) {
					
					$("#customGraph").append("<div>Custom statistics graph for "+jsonDataCustomNames[i]+":<br/><canvas id='cvsCustom"+i+"' width='700' height='600'>[No canvas support]</canvas></div><br/>");
				
			    	var line = new RGraph.Line('cvsCustom'+i, jsonDataCustom[i])
				        .Set('numxticks', 12)
				        .Set('hmargin', 0)
				        .Set('background.grid.autofit.numvlines', 12)
				        .Set('background.grid.dotted', true)
				        .Set('linewidth', 1)
				        .Set('gutter.left', 70)
				        .Set('gutter.right', 15)
				        .Set('colors', ['blue'])
				        .Set('labels',['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec','Jan'])
				        .Draw();
			    	
				}
	    	</script>	    	


			<div class="log" style="padding-bottom:40px;">
				<div>Your log output:</div>
				<div style="padding-left:20px;">
					${currentUserParticipated.debug }
				</div>
			</div>
				
		</c:if>

	</s:layout-component>
</s:layout-render>