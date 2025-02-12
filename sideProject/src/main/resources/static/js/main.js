function subMemberid(memberid) {
	console.log('進入傳輸表單的function')
	var form = $('<form>', {
		'method': 'POST',
		'action': '/memberShow'
	});

	var input = $('<input>', {
		'type': 'hidden',
		'name': 'memberid',
		'value': memberid
	});

	form.append(input);
	$('body').append(form);
	form.submit();
}
$(document).ready(async function() {

	let orderId = document.querySelector('meta[name="orderId"]').getAttribute('content');
	let memberId = document.querySelector('meta[name="memberId"]').getAttribute('content');
	//let memberId = 1;
	console.log('memberId: ' + memberId);
	console.log('orderId:' + orderId);

	let orderUrl = `http://localhost:8080/api/orders/getOrderById/${orderId}`;
	let responseOrder = await fetch(orderUrl);
	let responseOrderToJSON = await responseOrder.json();

	console.log(responseOrderToJSON);
	console.log(responseOrderToJSON.category);
	switch (responseOrderToJSON.category) {
		case 'web':
			category.innerText = '網頁';
			break;
		case 'game':
			category.innerText = '遊戲';
			break;
		case 'app':
			category.innerText = 'APP';
			break;
		default:
			category.innerText = '其他';
			break;
	}
	release.innerText = responseOrderToJSON.upload;
	projectTitle.innerText = responseOrderToJSON.name;
	updateDate.innerText = '最新更新時間:' + responseOrderToJSON.newdate;
	budget.innerText = '預算 : ' + responseOrderToJSON.budget;
	//地點換算
	console.log(responseOrderToJSON.location);
	switch (responseOrderToJSON.location) {
		case 'taipei':
			area.innerText = '地點 : 台北市';
			break;
		case 'newTaipei':
			area.innerText = '地點 : 新北市';
			break;
		case 'taoyuan':
			area.innerText = '地點 : 桃園市';
			break;
		case 'taichung':
			area.innerText = '地點 : 台中市';
			break;
		case 'tainan':
			area.innerText = '地點 : 台南市';
			break;
		case 'kaohsiung':
			area.innerText = '地點 : 高雄市';
			break;
		case 'keelung':
			area.innerText = '地點 : 基隆市';
			break;
		case 'hsinchuCity':
			area.innerText = '地點 : 新竹市';
			break;
		case 'chiayiCity':
			area.innerText = '地點 : 嘉義市';
			break;
		case 'yilan':
			area.innerText = '地點 : 宜蘭縣';
			break;
		case 'hsinchuCounty':
			area.innerText = '地點 : 新竹縣';
			break;
		case 'miaoli':
			area.innerText = '地點 : 苗栗縣';
			break;
		case 'changhua':
			area.innerText = '地點 : 彰化縣';
			break;
		case 'nantou':
			area.innerText = '地點 : 南投縣';
			break;
		case 'yunlin':
			area.innerText = '地點 : 雲林縣';
			break;
		case 'chiayiCounty':
			area.innerText = '地點 : 嘉義縣';
			break;
		case 'pingtung':
			area.innerText = '地點 : 屏東縣';
			break;
		case 'hualien':
			area.innerText = '地點 : 花蓮縣';
			break;
		case 'taitung':
			area.innerText = '地點 : 台東縣';
			break;
		case 'penghu':
			area.innerText = '地點 : 澎湖縣';
			break;
		case 'kinmen':
			area.innerText = '地點 : 金門縣';
			break;
		case 'lienchiang':
			area.innerText = '地點 : 連江縣';
			break;
		case 'remote':
			area.innerText = '地點 : 遠端工作';
			break;
		default:
			area.innerText = '地點 : 公司';
			break;
	}
	head.innerText = '建議人數 : ' + responseOrderToJSON.people + '人';
	deadline.innerText = '申請截止日 : ' + responseOrderToJSON.deadline;
	//需要技能
	let skillsUrl = `http://localhost:8080/api/tags/getTag/${orderId}`;
	let responseSkill = await fetch(skillsUrl);
	let responseSkillToJSON = await responseSkill.json();
	console.log(responseSkillToJSON);
	skill.innerText = '需要技能 : ' + responseSkillToJSON;

	//會員資料
	//顯示發案者頭像和名字
	//1.抓取會員資料
	let memberUrl = `http://localhost:8080/api/memberOrders/getMemberIdByOrderId/${orderId}`;
	let responseMember = await fetch(memberUrl);
	let responseMemberToJSON = await responseMember.json();
	console.log(responseMemberToJSON);
	console.log(responseMemberToJSON[0].memberid);
	//2.以會員id去拿個人資料
	if (!responseMemberToJSON[0].picurl) {
		imgBorder.innerHTML = `<img class="img-fluid object-fit-contain" src="/img/icon.jpg">`;
	} else {
		imgBorder.innerHTML = `<img class="img-fluid object-fit-contain" src="${responseMemberToJSON[0].picurl}">`;
	}
	memberName.innerText = `${responseMemberToJSON[0].account}`;
	//if (!responseMemberToJSON[0].intro) {
	//	introduce.innerText = '這個人很懶什麼都沒有寫';
	//} else {
	//	introduce.innerText = `${responseMemberToJSON[0].intro}`;
	//}

	memberArea.innerHTML += `<button id='goMember' class='btn btn-outline-primary' data-memberid="${responseMemberToJSON[0].memberid}" data-orderid="orderId">詳細資料</button>`;
	//專案會員點擊 詳細資料Btn 跳轉到memberShow(最上面的function記得複製)
	goMember.onclick = function() {
		console.log('我被點到了');
		event.preventDefault();
		var memberid = $(this).data('memberid');
		console.log(memberid);
		subMemberid(memberid);
	};

	detail.innerHTML = responseOrderToJSON.detail;

	//看目前使用者是誰來決定要顯示其他案件或是決定接案者
	if (responseMemberToJSON[0].memberid == `${memberId}`) {
		$('#another').css('display', 'none');
		//列出已決定的申請案件人
		let getedUrl = `http://localhost:8080/api/memberOrders/allMemberGeted/${orderId}`;
		let responseGeted = await fetch(getedUrl);
		let responseGetedToJSON = await responseGeted.json();
		console.log(responseGetedToJSON);

		responseGetedToJSON.forEach(item => {
			console.log(item.memberid);

			let another2 = $('<div>', {
				class: 'mx-auto rounded-2 another',
			});

			let anotherTitle2 = $('<div>', {
				class: 'anotherTitle',
				text: item.name,
			});

			let anotherButtonArea = $('<div>', {
				id: `ButtonArea${item.memberid}`,
				class: "mt-2 "
			});

			let anotherButton = $('<button>', {
				id: 'anotherButton',
				text: "已決定",
				class: "btn btn-outline-primary",
				disabled: 'true'
			});

			another2.append(anotherTitle2, anotherButtonArea);

			let anotherpro2 = $('<div>', {
				class: 'd-inline col-md-3 mt-1'
			});

			anotherpro2.append(another2);
			$('#getProject').append(anotherpro2);

			$(`#ButtonArea${item.memberid}`).html(`
			<button id='anotherabd' class="btn btn-outline-primary showMemberBtn" data-memberid="${item.memberid}">詳細資料</button>
			`);
			anotherButtonArea.append(anotherButton);

		});
		//列出來還未決定但來申請案件的人
		let getUrl = `http://localhost:8080/api/memberOrders/allMemberWanted/${orderId}`;
		let responseGet = await fetch(getUrl);
		let responseGetToJSON = await responseGet.json();
		console.log("BUG1");
		console.log(responseGetToJSON);

		responseGetToJSON.forEach(item => {
			console.log("BUG2");
			console.log(item);

			let another2 = $('<div>', {
				class: 'mx-auto rounded-2 another',
			});

			let anotherTitle2 = $('<div>', {
				class: 'anotherTitle',
				text: item.name,
			});

			let anotherButtonArea = $('<div>', {
				id: `ButtonAreaV${item.memberid}`,
				class: "mt-2 ButtonArea2"
			});

			let anotherButton = $('<button>', {
				id: 'anotherButton',
				text: "交給他",
				class: "btn btn-outline-primary"
			});

			another2.append(anotherTitle2, anotherButtonArea);

			let anotherpro2 = $('<div>', {
				class: 'd-inline col-md-3 mt-1'
			});

			anotherpro2.append(another2);
			$('#getProject').append(anotherpro2);
			//下方承豪寫$(`#ButtonAreaB${item.memberid}`).html(`
			$(`#ButtonAreaV${item.memberid}`).html(`
						<button id='anotherabd' class="btn btn-outline-primary showMemberBtn" data-memberid="${item.memberid}">詳細資料</button>
						`);

			anotherButtonArea.append(anotherButton);
			//下方承豪寫$(`#anotherButtonOnClick${item.memberid}`).on('click', function() {
			anotherButton[0].onclick = function() {
				let ButtonUrl = 'http://localhost:8080/api/memberOrders/getproject';
				fetch(ButtonUrl, {
					method: 'PUT',
					headers: {
						'Content-Type': 'application/json',
					},
					body: JSON.stringify({
						memberId: item.memberid,
						orderId: orderId,
						"getproject": 1
					})
				})
				alert('就決定是他了');
				window.location.reload();
			}//});
		});

		if (responseGetedToJSON.length || responseGetToJSON.length) {
			$('#applyPepple').css('display', 'none');
		}

		//$('#evaluate').css('display', 'none');
		//if (new Date(responseOrderToJSON.deadline) < new Date()) {
		//	console.log('已截止');
		// $('#apply').text('已結束');
		$('#evaluate').css('display', 'block');
		$('#applyevaluate').css('display', 'none');

		//列出評價
		let EvaAndRankUrl = `http://localhost:8080/api/memberOrders/getEvaluateAndRank/${orderId}`;
		let responseEvaAndRank = await fetch(EvaAndRankUrl);
		let responseEvaAndRankToJSON = await responseEvaAndRank.json();
		console.log(responseEvaAndRankToJSON);
		if (responseEvaAndRankToJSON.length) {
			$('#applyevaluate').css('display', 'none');
			responseEvaAndRankToJSON.forEach(item => {
				console.log("評價:" + item);
				let evaArea = `<div class="evaArea mx-auto rounded-2 mb-1">
				<div class="evaAreaName"><span>給評價者：</span><span><a href="" class="memberLink" data-memberid="${item.member.memberid}">${item.member.account}</a></span></div> 
				<div class="evaAreaRank"><span>給分：</span><span>${item.rank}分</span></div> 
				<div class="evaAreaEva"><span>評論內容：</span><span>${item.evaluate}</span></div> 
							</div>`;
				$('#evaluate').append(evaArea);
				//點擊框
			});
		};
		//} else {
		//console.log('進行中');
		//}

		$('.memberLink').click(function(event) {
			console.log('我被點到了');
			event.preventDefault();
			var memberid = $(this).data('memberid');
			console.log(memberid);
			subMemberid(memberid);
		});
	} else {
		$('#getProject').css('display', 'none');
		$('#evaluate').css('display', 'none');


		//推薦其他專案
		let anotherUrl = 'http://localhost:8080/api/orders/getAllOrders';
		let responseAnother = await fetch(anotherUrl);
		let responseAnotherToJSON = await responseAnother.json();
		let availableItems = [...responseAnotherToJSON];

		for (let i = 0; i < 4; i++) {
			if (availableItems.length === 0) break;  // 如果沒有足夠資料，跳出循環

			const index = Math.floor(Math.random() * availableItems.length);
			const element = availableItems[index];
			console.log(element);

			// 從可用的資料中移除選中的元素，避免重複
			availableItems.splice(index, 1);

			//const element = responseAnotherToJSON[i];
			// console.log(element);
			let another1 = $('<div>', {
				id: 'another1',
				class: 'mx-auto rounded-2',
			});

			let anotherTitle = $('<div>', {
				id: 'anotherTitle',
				text: element.name,
			});

			let anotherImg = $('<div>', {
				id: 'anotherImg',
				text: element.intro,
			});
			another1.append(anotherTitle, anotherImg)
			let anotherpro = $('<a>', {
				class: 'd-inline col-md-3 mt-1',
				href: `http://localhost:8080/order_main/${element.orderid}`,
			});
			anotherpro.append(another1);
			$('#another').append(anotherpro);
		}

	}


	//編輯和申請按鈕
	if (!memberId) {
		$('#buttonArea').css('display', 'none');
	}

	if (responseMemberToJSON[0].memberid == `${memberId}`) {
		$('#edit').css('display', 'block');
	} else if (new Date(responseOrderToJSON.deadline) < new Date()) {
		$('#apply').css('display', 'block');
		$('#apply').text('已結束');
		$('#apply').prop('disabled', true);
	} else {
		$('#apply').css('display', 'block');
		let memberWantUrl = `http://localhost:8080/api/memberOrders/memberWanted/${orderId}/${memberId}`
		let responseWant = await fetch(memberWantUrl);
		let responseWantToJSON = await responseWant.json();
		console.log(responseWantToJSON);
		//顯示申請人數
		let applypeopleUrl = `http://localhost:8080/api/memberOrders/wanted/people/${orderId}`;
		let responsePreople = await fetch(applypeopleUrl);
		let responsePeopleToJSON = await responsePreople.json();
		console.log(responsePeopleToJSON);
		let applyPeople = '目前申請人數:' + responsePeopleToJSON + '人';
		$('#applyPeople').text(applyPeople);
		if (responseWantToJSON == true) {
			$('#apply').text('已申請');
		};

	}



	edit.onclick = function() {
		window.location.href = `/order_update/${orderId}`;
	}

	apply.onclick = function() {
		let applyUrl = 'http://localhost:8080/api/memberOrders/addWantedOrder';
		fetch(applyUrl, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				memberId: memberId,
				orderId: orderId,
				"wanted": 1,
			})
		})
		$('#apply').text('已申請');
		$('#apply').prop('disabled', true);
		alert('已提出申請');
		window.location.reload();
	}

	if ($('#apply').text() === '已申請') {
		$('#apply').prop('disabled', true);
	}

	//收藏按鈕

	//先看是不是本人發的
	let collectUrl = `http://localhost:8080/api/memberOrders/getMemberIdByOrderId/${orderId}`;
	let responseCollect = await fetch(collectUrl);
	let responseCollectToJSON = await responseCollect.json();
	console.log('收藏者:' + responseCollectToJSON[0].memberid);

	if (responseCollectToJSON[0].memberid == `${memberId}`) {
		collect.style.visibility = "hidden";
	}

	//查會員是否已收藏
	let collectedUrl = `http://localhost:8080/api/memberOrders/collected/${orderId}/${memberId}`;
	let responseCollected = await fetch(collectedUrl);
	let responseCollectedToJson = await responseCollected.json();
	console.log('有沒有收藏:' + responseCollectedToJson);
	if (responseCollectedToJson === true) {
		collect.innerText = "已收藏";  // 初始顯示為「已收藏」
	} else {
		collect.innerText = "收藏";  // 初始顯示為「收藏」
	}

	collect.onclick = function() {
		let url;
		let collectedStatus;

		// 根據按鈕顯示的文字來判斷操作
		if (collect.innerText === "收藏") {
			url = 'http://localhost:8080/api/memberOrders/collected';
			collectedStatus = true;
			collect.innerText = "已收藏";
		} else {
			url = 'http://localhost:8080/api/memberOrders/removeMemberCollected';
			collectedStatus = false;
			collect.innerText = "收藏";
		}

		fetch(url, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				memberId: memberId,
				orderId: orderId,
				collected: collectedStatus
			})
		}).then(response => response.json())
			.then(data => {
				console.log("收藏狀態已更新:", data);
			}).catch(error => {
				console.error("Error:", error);
			});
	};

	var model = $('#memberModel');
	var span = $('.close');
	$('.showMemberBtn').click(
		function() {
			console.log('案件被點擊');
			var memberid = $(this).data('memberid');
			console.log('會員ID: ' + memberid);
			$.ajax({
				url: '/caseMember',
				type: 'GET',
				data: {
					memberid: memberid
				},
				success: function(data) {
					if (data) {
						console.log(data);
						$('#caseMemberIcon').attr('src',
							data.picurl);
						$('#name').text(data.name);
						$('#account').text(data.account);
						$('#email').text(data.email);
						$('#tel').text(data.tel);
						$('#birthday').text(data.birthday);
						$('#tagNames').html(data.tagNames);
						$('#github').attr('href',
							data.github).text(
								data.name + '的GitHub');
						$('#intro').text(data.intro);
						model.show();
						$('.emailInnerEmail').val(`${data.email}`);

						console.log('彈窗數據注入成功');
					} else {
						console.log('接收到的數據為空');
					}
				},
				error: function(jqXHR, textStatus,
					errorThrown) {
					console.log('請求失敗: ' + textStatus);
				}
			});
		});
	// send彈窗
	// 獲取彈窗和按鈕元素
	var modal = document.getElementById("sendModel"); // 確保名稱一致
	var openBtn = document.getElementById("openModalBtn");
	var closeBtn = document.querySelector(".sendClose");

	// 確保元素都成功選取
	if (modal && openBtn && closeBtn) {
		// 點擊按鈕打開彈窗
		openBtn.onclick = function() {
			modal.style.display = "block";
			$("#sendProjectTitle").val("《來自〈" + responseOrderToJSON.name + "〉專案》");
			$('#sendOrderid').val(orderId);
		}

		// 點擊關閉按鈕關閉彈窗
		closeBtn.onclick = function() {
			modal.style.display = "none";
		}

		// 點擊彈窗以外區域時也關閉彈窗
		window.onclick = function(event) {
			if (event.target === modal) {
				modal.style.display = "none";
			}
		}
	} else {
		console.error("找不到 modal 或按鈕，請檢查 HTML 結構！");
	}
	span.click(function() {
		model.hide();
	});
	$(window).click(function(event) {
		if ($(event.target).is(model)) {
			model.hide();
		}
	});

});