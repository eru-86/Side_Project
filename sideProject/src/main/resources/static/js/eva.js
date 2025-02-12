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
	var ratingValue = 0;

	// 當滑鼠移動到星星時
	$('.fa-star').hover(function() {
		var index = $(this).data('index');
		// 更新顯示的評分數字
		$('#rating-value').text(index);

		// 根據當前星星的索引，改變星星的顏色
		$('.fa-star').each(function(i) {
			if (i < index) {
				$(this).addClass('checked');
			} else {
				$(this).removeClass('checked');
			}
		});
	});

	// 當用戶點擊星星時
	$('.fa-star').click(function() {
		ratingValue = $(this).data('index');
		$('#rating-value').text(ratingValue);

		// 更新選中的星星顏色
		$('.fa-star').each(function(i) {
			if (i < ratingValue) {
				$(this).addClass('checked');
			} else {
				$(this).removeClass('checked');
			}
		});
	});

	// 當滑鼠離開星星時，恢復顯示當前選中的評分
	$('.fa-star').mouseleave(function() {
		$('#rating-value').text(ratingValue); // 顯示當前的選中評分
		$('.fa-star').each(function(i) {
			if (i < ratingValue) {
				$(this).addClass('checked');
			} else {
				$(this).removeClass('checked');
			}
		});
	});

	// 當模態框顯示時，重置評分
	$('#MyModal').on('show.bs.modal', function(e) {
		ratingValue = 0;
		$('#rating-value').text(ratingValue);
		$('.fa-star').removeClass('checked');
		$("#message-text").val("");
		var button = $(e.relatedTarget);
		orderid = button.data('order-id'); // 取得訂單 ID
		// 根據訂單 ID 載入對應的資料
		loadOrderData(orderid);
	});
	async function loadOrderData(orderid) {
		//顯示發案者頭像和名字
		let ordermemberUrl = `http://localhost:8080/api/memberOrders/getMembersByOrderid/${orderid}`;
		let responseOrdermember = await fetch(ordermemberUrl);
		let responseOrdermemberToJSON = await responseOrdermember.json();
		var usermemberid = responseOrdermemberToJSON[0].memberid;
		console.log("測:" + memberid);
		console.log(responseOrdermemberToJSON);
		imgBorder.innerHTML = `<img class="img-fluid object-fit-contain" src="${responseOrdermemberToJSON[0].picurl !== null ? order.picurl : '../img/caseImg.jpg'}">`;
		modalName.innerText = `${responseOrdermemberToJSON[0].name}`;
		//顯示專案名字
		let orderUrl = `http://localhost:8080/api/orders/getOrderById/${orderid}`;
		let responseOrder = await fetch(orderUrl);
		let responseOrderToJSON = await responseOrder.json();
		console.log(responseOrderToJSON[0]);
		console.log(responseOrderToJSON);
		projectTitle.innerText = `${responseOrderToJSON.name}`;
		$("#personalPage").click(function() {
			console.log('我被點到了');
			event.preventDefault();
			var memberid = usermemberid;
			console.log(memberid);
			subMemberid(memberid);
		});
	}


	//送出
	evaluateGo.onclick = async function() {
		var rating = $('#rating-value').text();
		var reviewText = $('#message-text').val();

		// 檢查是否已經選擇了評分
		if (rating == 0) {
			alert("至少請填入分數");
			return;
		}

		//評分部分
		//評論部分
		// 動態設置 URL，根據實際訂單 ID
		let evaluateUrl = `http://localhost:8080/api/memberOrders/addEvaluate/${orderId}`;
		// 準備傳送的資料
		let data = {
			evaluate: reviewText,
			rank: rating,
			memberId: memberid
		};
		// 發送 POST 請求
		await fetch(evaluateUrl, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(data), // 注意這裡需要轉換為 JSON 字串
		}).then(response => {
			if (response.ok) { // 檢查是否成功
				alert("已成功提交評分!"); // 顯示成功提示
			} else {
				alert("資料送出失敗，請再試一次。"); // 顯示失敗提示
			}
		})
			.catch(error => {
				console.error("發生錯誤：", error);
				alert("發生錯誤，請稍後再試。"); // 顯示錯誤提示
			});
		// 打印出評分和評價
		console.log("評分: " + rating + ", 評價: " + reviewText);
		alert("評分已成功送出");
	};
});