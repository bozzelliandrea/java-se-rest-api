function createCar() {
    fetch("/car", {
      method: "POST",
      body: JSON.stringify({
        description: document.getElementById("cdesc").value,
        name: document.getElementById("cname").value,
        color: document.getElementById("ccolor").value
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8"
      }
    }).then((json) => {
        fetch("/car", {
            headers: {
                "Content-type": "application/json; charset=UTF-8"
            }
        }).then(response => response.json())
          .then((json) =>  document.getElementById("listContent").value = json[0].name);
    });
}