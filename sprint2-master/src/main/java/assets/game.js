var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var sonar = false;
var shipSunk = 0;

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK"){
            shipSunk++;
            className = "hit"
        }
        else if (attack.result === "SURRENDER")
            alert(surrenderText);
        else if (attack.result === "SCANNED")
            className = "scanned";
        else if (attack.result === "DETECTED")
            className = "detected";
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("miss");
        if(square.hit){
            document.getElementById("player").rows[square.row - 1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("hit");
        }
    }));
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            if(document.getElementById("player").rows[i].cells[j].classList.contains("hit") && !document.getElementById("player").rows[i].cells[j].classList.contains("occupied")){
                document.getElementById("player").rows[i].cells[j].classList.replace("hit", "miss");
            }
        }
    }
}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            placedShips++;
            if (placedShips == 4) {
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    } else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col, sonarCheck: sonar, sunk: shipSunk}, function(data) {
            game = data;
            redrawGrid();
        })
        sonar = false;
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            if(req.responseURL == "http://localhost:8080/place"){
                alert("Invalid ship placement");
            }
            else{
                alert("Please select a valid cell")
            }

            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        if(shipType != "SUBMARINE") {
            for (let i=0; i<size; i++) {
                        let cell;
                        if(vertical) {
                            let tableRow = table.rows[row+i];
                            if (tableRow === undefined) {
                                // ship is over the edge; let the back end deal with it
                                break;
                            }
                            cell = tableRow.cells[col];
                        } else {
                            cell = table.rows[row].cells[col+i];
                        }
                        if (cell === undefined) {
                            // ship is over the edge; let the back end deal with it
                            break;
                        }
                        cell.classList.toggle("placed");
            }
        }
        else {
            for (let i=0; i<4; i++) {
                        let cell;
                        if(vertical) {
                            let tableRow = table.rows[row+i];
                            if (tableRow === undefined) {
                                // ship is over the edge; let the back end deal with it
                                break;
                            }
                            cell = tableRow.cells[col];
                        } else {
                            cell = table.rows[row].cells[col+i];
                        }
                        if (cell === undefined) {
                            // ship is over the edge; let the back end deal with it
                            break;
                        }
                        cell.classList.toggle("placed");
            }
        }

    }
}




function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
    });
    document.getElementById("place_sub").addEventListener("click", function(e) {
            shipType = "SUBMARINE";
           registerCellListener(place(5));
        });
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};

function sonarCount(){
    var count = document.getElementById('sonar_Count').innerHTML;
    if(placedShips == 4){
        if(count==0)
            {
            alert("You have run out of Sonar Pulse");
            }
        else
            {
            if(shipSunk >= 1){

                count--;
                sonar = true;
                document.getElementById('sonar_Count').innerHTML = count;
            }
            else
            {
                alert("You must destroy a ship first!");
            }
        }
    }
    else{
        alert("Place your ships first to start the game!");
    }
};

function moveCount(){
    console.log("Porque?");
    var count = document.getElementById('move_Count').innerHTML;
    if(placedShips == 4){
        if(count==0){
            alert("You have moved your fleet twice already");
        }
        else{
            if(shipSunk>=2){
                //count--;
                //document.getElementById('move_Count').innerHTML = count;
                //DO SHIT HERE
                var dir;
                var radios = document.getElementsByName('move_dir');
                for(var i = 0, length = radios.length; i < length; i++) {
                    if (radios[i].checked){
                        dir = radios[i].value;
                        break;
                    }
                }
                var canplace = true;
                if(dir.localeCompare("North") == 0){
                    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
                        if(square.row == 1){canplace = false;}
                    }));
                    if(!canplace){
                        alert("You cannot move ships off the map");
                    }else{
                        game.playersBoard.ships.forEach((ship) => {
                            ship.occupiedSquares.forEach((square) => {
                            document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("occupied");
                    })});

                        count--;
                        document.getElementById('move_Count').innerHTML = count;
                        sendXhr("POST", "/move", {game: game, direction: dir}, function(data) {
                            game = data;
                            redrawGrid();
                        })
                    }
                }
                else if(dir.localeCompare("South") == 0){
                    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
                        if(square.row == 10){canplace = false;}
                    }));
                    if(!canplace){
                        alert("You cannot move ships off the map");
                    }else{
                        game.playersBoard.ships.forEach((ship) => {ship.occupiedSquares.forEach((square) => {
                            document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("occupied");
                        })});
                        count--;
                        document.getElementById('move_Count').innerHTML = count;
                        sendXhr("POST", "/move", {game: game, direction: dir}, function(data) {
                            game = data;
                            redrawGrid();
                        })
                    }
                }
                else if(dir.localeCompare("East") == 0){
                    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
                        if(square.column.charAt(0) == "J".charAt(0)){canplace = false;}
                    }));
                    if(!canplace){
                        alert("You cannot move ships off the map");
                    }else{
                        game.playersBoard.ships.forEach((ship) => {ship.occupiedSquares.forEach((square) => {
                            document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("occupied");
                        })});
                        count--;
                        document.getElementById('move_Count').innerHTML = count;
                        sendXhr("POST", "/move", {game: game, direction: dir}, function(data) {
                            game = data;
                            redrawGrid();
                        })
                    }
                }
                else if(dir.localeCompare("West") == 0) {
                    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
                        if(square.column.charAt(0) == "A".charAt(0)){canplace = false;}
                    }));
                    if(!canplace){
                        alert("You cannot move ships off the map");
                    }
                    else{
                        game.playersBoard.ships.forEach((ship) => {ship.occupiedSquares.forEach((square) => {
                            document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.remove("occupied");
                        })});
                        count--;
                        document.getElementById('move_Count').innerHTML = count;
                        sendXhr("POST", "/move", {game: game, direction: dir}, function(data) {
                            game = data;
                            redrawGrid();
                        })

                    }
                }
            }
            else
            {
                alert("You must destroy two ships first!");
            }
        }
    }
    else{
        alert("Place your ships first to start the game!");
    }
};

