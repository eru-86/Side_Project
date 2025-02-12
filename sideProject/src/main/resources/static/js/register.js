/**
 * 
 */

function validatePassword() {
    var password = document.getElementById('password').value;
    var checkpasswd = document.getElementById('checkpasswd').value;

    if (password !== checkpasswd) {
        alert("輸入密碼不相同，請重新確認！");
      return false;  // 阻止表單提交
    }

    return true;  // 表單可以提交
    }

