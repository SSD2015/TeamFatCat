// User Properties
var userIsMember = false;
var userID = "5610546257";

// Project Properties
var projectName = "GurongGuro";
var description = "Once upon a time there was a dear little girl who was loved by everyone who looked at her, but most of all by her grandmother, and there was nothing that she would not have given to the child. Once she gave her a little riding hood of red velvet, which suited her so well that she would never wear anything else; so she was always called 'Little Red Riding Hood.'";
var averageStar = 3.66; // To Show the Average Star
var projectMembers = 	[
							"5610546702",
							"5610545684",
							"5610546222",
							"5610546257",
							"5610546699"
						];

function showAverageStar() {
	var fullStar = Math.floor(averageStar);
	var half = Math.round((averageStar-fullStar)*2)/2; //0,0.5,1
	for(var i=0;i<fullStar;i++) {
		var star = document.getElementById("star"+i);
		star.src = "./img/star/2.png";    
	}
	if(fullStar < 5) {
		var star = document.getElementById("star"+fullStar);
		star.src = "./img/star/"+(half*2)+".png";
	}
}
function checkIsMember() {
	for(var i=0;i<projectMembers.length;i++) {
		if( userID == projectMembers[i] ) {
			userIsMember = true;
			break;
		}
	}
}

checkIsMember();
if( !userIsMember ) {
	$("#editProject").text("");
}
$("#projectName").text(projectName);
$("#description").text(description);
showAverageStar();