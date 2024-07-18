# Battle-Recruitment_Task


<h2>Endpointy</h2>

<code>POST /api/game/new</code> - Tworzenie nowej gry
<br>Przykładowy obiekt żądania:
```json
{
    "boardSize": {
        "width": 10,
        "height": 10
    },
    "archerAmount": 20,
    "transportAmount": 5,
    "cannonAmount": 2
}
```
<br><br>
<code>POST /api/unit/list</code> - Zwraca listę jednostek danego koloru dla aktywnej gry
<br>Przykładowy obiekt żądania:
```json
{
    "color": "WHITE"
}
```
<br><br>
<code>GET /api/unit/listAll</code> - Zwraca listę wszystkich jednostek dla aktywnej gry

<br><br>
<code>GET /api/unit/info/{id_jednostki}</code> - Zwraca informacje o jednostce dla podanego id
<br>Przykładowa odpowiedź:
```json
{
    {
    "unitId": 19,
    "position": {
        "x": 1,
        "y": 1
    },
    "type": "ARCHER",
    "color": "WHITE",
    "status": "ACTIVE",
    "moveCount": 0,
    "possibleMoves": [
        {
            "x": 0,
            "y": 1
        },
        {
            "x": 2,
            "y": 1
        },
        {
            "x": 1,
            "y": 0
        },
        {
            "x": 1,
            "y": 2
        }
    ],
    "possibleShots": [
        {
            "x": 1,
            "y": 2
        },
        {
            "x": 1,
            "y": 0
        },
        {
            "x": 2,
            "y": 1
        },
        {
            "x": 0,
            "y": 1
        }
    ]
}
}
```

<br><br>
<code>POST /api/unit/move</code> - Przemieszcza daną jednostkę na odpowiednią pozycję
<br>Przykładowy obiekt żądania:
```json
{
    "color": "WHITE",
    "unitId": 19,
    "destination": {
        "x": 2,
        "y": 1
    }
}
```

<br><br>
<code>POST /api/unit/shoot</code> - Strzela daną jednostką na określoną pozycję
<br>Przykładowy obiekt żądania:
```json
{
    "color": "WHITE",
    "unitId": 19,
    "destination": {
        "x": 2,
        "y": 1
    }
}
```

<br><br>
<code>POST /api/unit/randomAction</code> - Wykonanie losowej akcji dla losowej jednostki o podanym kolorze
<br>Przykładowy obiekt żądania:
```json
{
    "color": "WHITE",
    "ignoreCooldown": true
}
```
*ignoreCooldown - Umożliwia sprawniejsze testowanie bez konieczności czekania na możliwość wykonania kolejnej akcji.
