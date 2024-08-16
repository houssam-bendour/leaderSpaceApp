document.addEventListener("DOMContentLoaded", function() {
    const downloadButtons = document.querySelectorAll(".downloadBtn");
    const downloadAllButton = document.getElementById("downloadAllBtn");

    downloadButtons.forEach(button => {
        button.addEventListener("click", function() {
            const qrCodeImg = this.previousElementSibling;
            const imgSrc = qrCodeImg.getAttribute("src");
            const link = document.createElement("a");
            link.href = imgSrc;
            link.download = "qrcode.png";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });
    });

    downloadAllButton.addEventListener("click", function() {
        const zip = new JSZip();
        const qrCodeImgs = document.querySelectorAll("#qrCodeImg");

        qrCodeImgs.forEach((qrCodeImg, index) => {
            const imgSrc = qrCodeImg.getAttribute("src");
            const base64String = imgSrc.split(',')[1];
            zip.file(`qrcode_${index + 1}.png`, base64String, {base64: true});
        });

        zip.generateAsync({type: "blob"}).then(function(content) {
            const link = document.createElement("a");
            link.href = URL.createObjectURL(content);
            link.download = "qrcodes.zip";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });
    });
});
