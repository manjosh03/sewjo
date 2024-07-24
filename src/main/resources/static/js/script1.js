document.addEventListener("DOMContentLoaded", () => {
    const fabricsLink = document.getElementById("fabrics-link");
    const dynamicContent = document.getElementById("dynamic-content");
  
    const profileContent = `
      <div class="photos">
        <img src="img/photo1.jpeg" alt="Photo 1" />
        <img src="img/photo2.jpeg" alt="Photo 2" />
        <img src="img/photo3.jpeg" alt="Photo 3" />
      </div>
    `;
  
    fabricsLink.addEventListener("click", (event) => {
      event.preventDefault();
      dynamicContent.innerHTML = profileContent;
    });
  });
  