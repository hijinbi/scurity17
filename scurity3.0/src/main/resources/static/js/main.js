function passwordCheckFunction(){
	var password1 = $("#password").val();
	var re_pass = $("#re_pass").val();
	if(password1 != re_pass){
		$("#checkMessage").html("비밀번호가 일치하지 않습니다.");
	} else {
		$("#checkMessage").html("");
	}
}