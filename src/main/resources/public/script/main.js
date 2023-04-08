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
    }).then(response => response.json())
      .then((json) => {
        insertCarRow(json);
        document.getElementById("carForm").reset();
    });
}

function getCars() {
        fetch("/car", {
            headers: {
                "Content-type": "application/json; charset=UTF-8"
            }
        }).then(response => response.json())
        .then((jsonArr) => {
            for(let json of jsonArr) {
                insertCarRow(json);
            }
        });
}

function insertCarRow(json) {
    const table = document.getElementById("carTable");
    const row = table.insertRow();
    const cell1 = row.insertCell(0);
    const cell2 = row.insertCell(1);
    const cell3 = row.insertCell(2);
    cell1.innerHTML = json.name;
    cell2.innerHTML = json.description;
    cell3.innerHTML = json.color;
}

getCars();