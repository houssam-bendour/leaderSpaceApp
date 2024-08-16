document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("qrCodeModal");
    var btn = document.getElementById("qrCodeBtn");
    var span = document.getElementsByClassName("close")[0];
    var downloadBtn = document.getElementById("downloadBtn");
    var qrCodeImg = document.getElementById("qrCodeImg");

    btn.onclick = function () {
        modal.style.display = "block";
    }

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    downloadBtn.onclick = function () {
        var link = document.createElement("a");
        link.href = qrCodeImg.src;
        var fileName = qrCodeImg.getAttribute("data-filename") + ".png";
        link.download = fileName;
        link.click();
    }

});
