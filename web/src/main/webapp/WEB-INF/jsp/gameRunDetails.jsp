<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement" style="display: flex; align-items: center; justify-content: space-between;">
			<h2 class="mb-0">Game Run Details</h2>
			<s:link beanclass="de.oglimmer.cyc.web.action.RunHistoryActionBean" class="nav-link">Back to history</s:link>
		</div>

		<div class="centerElement">
			<div class="text-mono text-sm text-secondary">
				Started: <fmt:formatDate type="both" dateStyle="short" timeStyle="medium" value="${actionBean.startTime}" /> UTC
				&mdash; ran for <strong class="text-green"><fmt:formatNumber value="${(actionBean.endTime.time-actionBean.startTime.time)/1000}" type="number" maxFractionDigits="0"/> secs</strong>
			</div>
		</div>

		<div class="centerElement">
			<h3>Participating Companies <span class="text-sm text-muted">(${actionBean.result.totalDays} days)</span></h3>
			<table>
				<thead>
					<tr>
						<th>Company</th>
						<th style="text-align: right;">Total Assets</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${actionBean.result.playerResults}" var="mapEntry">
					<c:set var="companyNameLowerCase" value="${fn:toLowerCase(mapEntry.key)}" />
					<tr>
  						<td><strong>${mapEntry.key}</strong></td>
  						<td style="text-align: right;">
  							<c:set value="${mapEntry.value.totalAssets}" var="tmpAsset"/>
							<c:if test="${tmpAsset != -1}">
								<span class="text-green text-mono"><fmt:formatNumber value="${tmpAsset}" type="currency" pattern="¤#,##0.00;-¤#,##0.00"/></span>
							</c:if>
							<c:if test="${tmpAsset == -1}">
								<span class="text-red">Bankrupt on day ${mapEntry.value.bankruptOnDay}</span>
								<c:if test="${actionBean.result.errors.contains(mapEntry.key) }"> <span class="text-amber text-xs">(JS error)</span></c:if>
							</c:if>
  						</td>
  						<td>
							<c:if test="${actionBean.showCode[companyNameLowerCase] }">
								<s:link beanclass="de.oglimmer.cyc.web.action.ShowCodeActionBean" class="nav-link" style="padding: 4px 10px; font-size: 12px;">
									<s:param name="username">${mapEntry.key}</s:param>
									<s:param name="gameRunId">${param.gameRunId}</s:param>
									View Code
								</s:link>
							</c:if>
  						</td>
  					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="centerElement">
			<h3>Guests per City</h3>
			<table>
				<c:forEach items="${actionBean.result.guestsTotalPerCity}" var="entry">
					<tr>
  						<td>Total guests for <strong>${entry.key}</strong></td>
  						<td style="text-align: right;" class="text-mono">
  							<fmt:formatNumber value="${entry.value}" type="number"/>
  							<span class="text-muted text-xs">(<fmt:formatNumber value="${entry.value/actionBean.result.totalDays}" maxFractionDigits="0" type="number"/> p.day)</span>
  						</td>
  					</tr>
				</c:forEach>
			</table>
		</div>

		<div class="stats-grid" style="grid-template-columns: 1fr 1fr;">
			<div class="centerElement">
				<h3>Meal Popularity</h3>
				<table>
					<c:forEach items="${actionBean.result.foodChart}" var="entry">
						<tr>
	  						<td>${entry.key}</td>
	  						<td style="text-align: right;" class="text-mono text-green"><fmt:formatNumber value="${entry.value}" type="percent" maxFractionDigits="0"/></td>
	  					</tr>
					</c:forEach>
				</table>
			</div>

			<div class="centerElement">
				<h3>Establishment Appeal</h3>
				<table>
					<c:forEach items="${actionBean.result.establishmentChart}" var="entry">
						<tr>
	  						<td>${entry.key}</td>
	  						<td style="text-align: right;" class="text-mono text-cyan"><fmt:formatNumber value="${entry.value}" type="percent" maxFractionDigits="0"/></td>
	  					</tr>
					</c:forEach>
				</table>
			</div>
		</div>

		<script>
			function toggleData(toogleId) {
				$("#"+toogleId).toggle();
			}
		</script>

		<div class="centerElement">
			<h3>Detailed Company Statistics</h3>
			<p class="text-sm text-muted">Click a company name to expand details</p>

			<c:forEach items="${actionBean.result.playerResults}" var="entry">
				<c:set var="expenses" value="${5000+entry.value.totalOnRent+entry.value.salariesTotal+entry.value.purchasedFoodCostsTotal+entry.value.totalBribe+entry.value.totalInterior}"/>
				<c:set var="daysInPlay" value="${actionBean.result.totalDays<entry.value.bankruptOnDay||entry.value.bankruptOnDay==0?actionBean.result.totalDays:entry.value.bankruptOnDay}"/>
				<fmt:formatNumber var="monthInPlay" value="${daysInPlay/30}" maxFractionDigits="0" />
				<c:set var="counter" value="0"/>
				<div onclick="toggleData('dataCompany${entry.value.nameSimplified}');">${entry.key}</div>
				<table style="display:none" id="dataCompany${entry.value.nameSimplified}">
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Total Assets (cash &amp; real estates)</td>
						<td align="right" width="170">
							<c:if test="${entry.value.totalAssets != -1}">
								<span class="text-green text-mono"><fmt:formatNumber value="${entry.value.totalAssets}" type="currency"/></span>
							</c:if>
							<c:if test="${entry.value.totalAssets == -1}"><span class="text-red">Bankrupt</span></c:if>
						</td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Total Earnings</td>
						<td align="right" class="text-mono"><fmt:formatNumber value="${entry.value.servedFoodRevenueTotal}" type="currency"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Total Expenses</td>
						<td align="right" class="text-mono text-red"><fmt:formatNumber value="${expenses}" type="currency"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Initial credit</td>
						<td align="right" class="text-mono"><fmt:formatNumber value="50000" type="currency"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Credit pay back (incl. interest)</td>
						<td align="right" class="text-mono text-red"><fmt:formatNumber value="-55000" type="currency" pattern="¤#,##0.00;-¤#,##0.00"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Total on renting real estates</td>
						<td align="right" class="text-mono">
							<fmt:formatNumber value="${entry.value.totalOnRent}" type="currency"/>
							/ <fmt:formatNumber value="${entry.value.totalOnRent / monthInPlay}" type="currency"/>
						</td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Total on buying real estates</td>
						<td align="right" class="text-mono"><fmt:formatNumber value="${entry.value.totalRealEstate}" type="currency"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Total on broker bribes</td>
						<td align="right" class="text-mono"><fmt:formatNumber value="${entry.value.totalBribe}" type="currency"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Total on interior accessories</td>
						<td align="right" class="text-mono"><fmt:formatNumber value="${entry.value.totalInterior}" type="currency"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Avg restaurants per day</td>
						<td align="right" class="text-mono"><fmt:formatNumber value="${entry.value.establishmentsByDays / daysInPlay}" type="number"/></td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
					<c:forEach items="${entry.value.staffByDays}" var="entryStaff">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Avg ${entryStaff.key}'s per day</td>
							<td align="right" class="text-mono"><fmt:formatNumber value="${entryStaff.value/ daysInPlay}" type="number"/></td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.totalOnSalaries}" var="entryStaff">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total salary for ${entryStaff.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryStaff.value}" type="currency"/>
								/ <fmt:formatNumber value="${entryStaff.value / monthInPlay}" type="currency"/>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.totalPurchasedFoodUnits}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total purchased units of ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								/ <fmt:formatNumber value="${entryUnits.value / daysInPlay}" type="number" maxFractionDigits="1"/>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.totalPurchasedFoodCosts}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total food costs for ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="currency"/>
								/ <fmt:formatNumber value="${entryUnits.value / entry.value.totalPurchasedFoodUnits[entryUnits.key]}" type="currency"/>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.totalRottenFood}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total rotten units of ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								/ <fmt:formatNumber value="${entryUnits.value / daysInPlay}" type="number" maxFractionDigits="1"/>
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.servedFoodPerTypeUnits}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total served meals of ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodUnitsTotal}" type="percent"/>)
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.servedFoodPerTypeRevenue}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total revenue for ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="currency"/>
								(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodRevenueTotal}" type="percent"/>)
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.servedFoodPerEstablishmentUnits}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total served in ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodEstablishmentUnitsTotal}" type="percent"/>)
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.servedFoodPerEstablishmentRevenue}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total revenue in ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="currency"/>
								(<fmt:formatNumber value="${entryUnits.value/entry.value.servedFoodEstablishmentRevenueTotal}" type="percent"/>)
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.guestsYouPerCity}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Total visitors in ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								(<fmt:formatNumber value="${entryUnits.value/actionBean.result.guestsTotalPerCity[entryUnits.key]}" type="percent"/>)
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.guestsLeftPerCity}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Visitors who left without ordering in ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								(<fmt:formatNumber value="${entryUnits.value/entry.value.guestsYouPerCity[entryUnits.key]}" type="percent"/>)
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.guestsOutOfIngPerCity}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Out of ingredients in ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/>
								(<fmt:formatNumber value="${entryUnits.value/entry.value.guestsYouPerCity[entryUnits.key]}" type="percent"/>)
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.missingIngredients}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Missing ${entryUnits.key}</td>
							<td align="right" class="text-mono">
								<fmt:formatNumber value="${entryUnits.value}" type="number"/> times
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<c:forEach items="${entry.value.runtimes}" var="entryUnits">
						<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
							<td>Method ${entryUnits.key} took</td>
							<td align="right" class="text-mono">
								<c:if test="${entryUnits.key != 'init' and entryUnits.key != 'launch'}">&#xF8;</c:if>
								<fmt:formatNumber value="${entryUnits.value.average()}" type="number"/> &#xB5;s
							</td>
						</tr>
						<c:set var="counter" value="${counter + 1}"/>
					</c:forEach>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
						<td>Code length (without whitespace)</td>
						<td align="right" class="text-mono">
							<fmt:formatNumber value="${entry.value.codeLength}" type="number"/>
						</td>
					</tr>
					<c:set var="counter" value="${counter + 1}"/>
				</table>
				<div style="height: 1px; background: var(--border-subtle); margin: 8px 0;"></div>
			</c:forEach>
		</div>

		<div class="centerElement">
			<h3>Cash Flow Over Time <span class="text-sm text-muted">(Top 15 players, lowest figure per day)</span></h3>

			<c:set var="userTop15" value="${actionBean.result.playerResultsTop15}" />

			<script src="js/RGraph.common.core.js"></script>
	    	<script src="js/RGraph.line.js"></script>
	    	<canvas id="cvs" width="700" height="600">[No canvas support]</canvas>
			<div id="labels">Legend: </div>
			<script>
				var jsonData = [<c:forEach items="${userTop15}" var="play">${play.value.statistics.cashHtml},</c:forEach>];
				var labelData = [<c:forEach items="${userTop15}" var="play">'${fn:replace(play.key, "'", "\\'")}',</c:forEach>];
				var labelColors = ['#00ff88', '#ffffff', '#ffd700', '#ff44ff', '#888888', '#00cc44', '#88ff00', '#cc0044', '#0044cc', '#888800', '#000000', '#ff8800', '#8800cc', '#c0c0c0', '#00cccc'];
			</script>

			<c:if test="${not empty actionBean.username}">
	    		<c:set var="currentUserParticipated" value="${actionBean.result.playerResults[actionBean.username]}" />
	    		<c:set var="currentUserInTop15" value="${userTop15[actionBean.username]}" />
	    		<c:if test="${empty currentUserInTop15 && not empty currentUserParticipated}">
			    	<script>
			    		jsonData.push(${currentUserParticipated.statistics.cashHtml});
			    		labelData.push("${actionBean.username}");
			    		labelColors.push('#ff4466');
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

		<c:if test="${not empty currentUserParticipated}">
			<div id="customGraph" class="centerElement">
		    </div>
			<script>
				var jsonDataCustom = [${currentUserParticipated.statistics.getCustomHtml()}];
				var jsonDataCustomNames = [<c:forEach var="i" begin="0" end="4">"${currentUserParticipated.statistics.getCustomStatisticsName(i)}",</c:forEach>];

				for(var i = 0 ; i < jsonDataCustom.length ; i++) {
					$("#customGraph").append("<h3>Custom Statistics: "+jsonDataCustomNames[i]+"</h3><canvas id='cvsCustom"+i+"' width='700' height='600'>[No canvas support]</canvas><br/>");

			    	var line = new RGraph.Line('cvsCustom'+i, jsonDataCustom[i])
				        .Set('numxticks', 12)
				        .Set('hmargin', 0)
				        .Set('background.grid.autofit.numvlines', 12)
				        .Set('background.grid.dotted', true)
				        .Set('linewidth', 1)
				        .Set('gutter.left', 70)
				        .Set('gutter.right', 15)
				        .Set('colors', ['#00ff88'])
				        .Set('labels',['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec','Jan'])
				        .Draw();
				}
	    	</script>

			<div class="centerElement">
				<h3>Your Debug Log</h3>
				<div class="log">
					${currentUserParticipated.debug}
				</div>
			</div>
		</c:if>

	</s:layout-component>
</s:layout-render>
